/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.impl.bpmn.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.impl.bpmn.helper.ErrorPropagation;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmProcessInstance;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.impl.pvm.delegate.SubProcessActivityBehavior;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.MapExceptionEntry;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.data.AbstractDataAssociation;
import org.flowable.engine.repository.ProcessDefinition;

/**
 * Implementation of the BPMN 2.0 call activity (limited currently to calling a subprocess and not (yet) a global task).
 *
 * @author Joram Barrez
 */
public class CallActivityBehavior extends AbstractBpmnActivityBehavior implements SubProcessActivityBehavior {

    protected String processDefinitonKey;
    private List<AbstractDataAssociation> dataInputAssociations = new ArrayList<>();
    private List<AbstractDataAssociation> dataOutputAssociations = new ArrayList<>();
    private Expression processDefinitionExpression;
    protected List<MapExceptionEntry> mapExceptions;
    protected boolean inheritVariables;
    protected boolean sameDeployment;
    protected String businessKey;
    protected boolean inheritBusinessKey;

    public CallActivityBehavior(String processDefinitionKey, List<MapExceptionEntry> mapExceptions) {
        this.processDefinitonKey = processDefinitionKey;
        this.mapExceptions = mapExceptions;
    }

    public CallActivityBehavior(Expression processDefinitionExpression, List<MapExceptionEntry> mapExceptions) {
        super();
        this.processDefinitionExpression = processDefinitionExpression;
        this.mapExceptions = mapExceptions;
    }

    public void addDataInputAssociation(AbstractDataAssociation dataInputAssociation) {
        this.dataInputAssociations.add(dataInputAssociation);
    }

    public void addDataOutputAssociation(AbstractDataAssociation dataOutputAssociation) {
        this.dataOutputAssociations.add(dataOutputAssociation);
    }

    @Override
    public void execute(DelegateExecution execution) {

        String processDefinitonKey = this.processDefinitonKey;
        if (processDefinitionExpression != null) {
            processDefinitonKey = (String) processDefinitionExpression.getValue(execution);
        }

        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        DeploymentManager deploymentManager = processEngineConfiguration.getDeploymentManager();

        ProcessDefinition processDefinition = null;

        if (sameDeployment) {
            String deploymentId = deploymentManager.findDeployedProcessDefinitionById(execution.getProcessDefinitionId()).getDeploymentId();
            processDefinition = Context.getCommandContext().getProcessDefinitionEntityManager().findProcessDefinitionByDeploymentAndKey(deploymentId, processDefinitonKey);
            processDefinition = deploymentManager.findDeployedProcessDefinitionById(processDefinition.getId());
        }

        if (processDefinition == null) {
            if (execution.getTenantId() == null || ProcessEngineConfiguration.NO_TENANT_ID.equals(execution.getTenantId())) {
                processDefinition = deploymentManager.findDeployedLatestProcessDefinitionByKey(processDefinitonKey);
            } else {
                processDefinition = deploymentManager.findDeployedLatestProcessDefinitionByKeyAndTenantId(processDefinitonKey, execution.getTenantId());
            }
        }

        // Do not start a process instance if the process definition is suspended
        if (deploymentManager.isProcessDefinitionSuspended(processDefinition.getId())) {
            throw new ActivitiException("Cannot start process instance. Process definition "
                    + processDefinition.getName() + " (id = " + processDefinition.getId() + ") is suspended");
        }

        ActivityExecution activityExecution = (ActivityExecution) execution;
        PvmProcessInstance subProcessInstance = activityExecution.createSubProcessInstance((ProcessDefinitionEntity) processDefinition);

        if (inheritVariables) {
            Map<String, Object> variables = execution.getVariables();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                subProcessInstance.setVariable(entry.getKey(), entry.getValue());
            }
        }

        String subProcessInstanceBusinessKey = null;
        if (StringUtils.isNotEmpty(businessKey)) {
            ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();
            Expression expression = expressionManager.createExpression(businessKey);
            subProcessInstanceBusinessKey = expression.getValue(execution).toString();

        } else if (inheritBusinessKey) {
            ExecutionEntityManager executionEntityManager = Context.getCommandContext().getExecutionEntityManager();
            ExecutionEntity parentProcessInstance = executionEntityManager.findExecutionById(execution.getProcessInstanceId());
            subProcessInstanceBusinessKey = parentProcessInstance.getBusinessKey();

        }

        if (StringUtils.isNotEmpty(subProcessInstanceBusinessKey) && subProcessInstance instanceof ExecutionEntity) {
            ((ExecutionEntity) subProcessInstance).setBusinessKey(subProcessInstanceBusinessKey);
        }

        // copy process variables
        for (AbstractDataAssociation dataInputAssociation : dataInputAssociations) {
            Object value = null;
            if (dataInputAssociation.getSourceExpression() != null) {
                value = dataInputAssociation.getSourceExpression().getValue(execution);
            } else {
                value = execution.getVariable(dataInputAssociation.getSource());
            }
            subProcessInstance.setVariable(dataInputAssociation.getTarget(), value);
        }

        try {
            subProcessInstance.start();
        } catch (RuntimeException e) {

            Throwable cause = e;
            BpmnError error = null;
            while (cause != null) {
                if (cause instanceof BpmnError) {
                    error = (BpmnError) cause;
                    break;

                } else if (cause instanceof RuntimeException) {
                    if (ErrorPropagation.mapException((RuntimeException) cause, activityExecution, mapExceptions)) {
                        return;
                    }
                }
                cause = cause.getCause();
            }

            if (error != null) {
                ErrorPropagation.propagateError(error, activityExecution);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void completing(DelegateExecution execution, DelegateExecution subProcessInstance) throws Exception {
        // only data. no control flow available on this execution.

        // copy process variables
        for (AbstractDataAssociation dataOutputAssociation : dataOutputAssociations) {
            Object value = null;
            if (dataOutputAssociation.getSourceExpression() != null) {
                value = dataOutputAssociation.getSourceExpression().getValue(subProcessInstance);

            } else {
                value = subProcessInstance.getVariable(dataOutputAssociation.getSource());
            }

            execution.setVariable(dataOutputAssociation.getTarget(), value);
        }
    }

    @Override
    public void completed(ActivityExecution execution) throws Exception {
        // only control flow. no sub process instance data available
        leave(execution);
    }

    public void setProcessDefinitonKey(String processDefinitonKey) {
        this.processDefinitonKey = processDefinitonKey;
    }

    public String getProcessDefinitonKey() {
        return processDefinitonKey;
    }

    public void setInheritVariables(boolean inheritVariables) {
        this.inheritVariables = inheritVariables;
    }

    public void setSameDeployment(boolean sameDeployment) {
        this.sameDeployment = sameDeployment;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public boolean isInheritBusinessKey() {
        return inheritBusinessKey;
    }

    public void setInheritBusinessKey(boolean inheritBusinessKey) {
        this.inheritBusinessKey = inheritBusinessKey;
    }
}
