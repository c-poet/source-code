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
package org.flowable.cmmn.engine.impl.behavior.impl;

import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.cmmn.engine.impl.behavior.CoreCmmnActivityBehavior;
import org.flowable.cmmn.engine.impl.persistence.entity.MilestoneInstanceEntity;
import org.flowable.cmmn.engine.impl.persistence.entity.MilestoneInstanceEntityManager;
import org.flowable.cmmn.engine.impl.persistence.entity.PlanItemInstanceEntity;
import org.flowable.cmmn.engine.impl.util.CommandContextUtil;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.common.engine.impl.interceptor.CommandContext;

/**
 * @author Joram Barrez
 */
public class MilestoneActivityBehavior extends CoreCmmnActivityBehavior {
    
    protected Expression milestoneNameExpression;
    protected String milestoneVariable;
    protected String businessStatus;

    public MilestoneActivityBehavior(Expression milestoneNameExpression, String milestoneVariable, String businessStatus) {
        this.milestoneNameExpression = milestoneNameExpression;
        this.milestoneVariable = milestoneVariable;
        this.businessStatus = businessStatus;
    }
    
    @Override
    public void execute(CommandContext commandContext, PlanItemInstanceEntity planItemInstanceEntity) {
        MilestoneInstanceEntity milestoneInstanceEntity = createMilestoneInstance(planItemInstanceEntity, commandContext);

        ExpressionManager expressionManager = CommandContextUtil.getExpressionManager(commandContext);

        if (StringUtils.isNotEmpty(milestoneVariable)) {
            Expression milestoneVariableExpression = expressionManager.createExpression(milestoneVariable);
            String actualMilestoneVariable = (String) milestoneVariableExpression.getValue(planItemInstanceEntity);
            if (StringUtils.isNotEmpty(actualMilestoneVariable)) {
                planItemInstanceEntity.setVariable(actualMilestoneVariable, true);
            }
        }

        if (StringUtils.isNotEmpty(businessStatus)) {
            Expression businessStatusExpression = expressionManager.createExpression(businessStatus);
            String actualBusinessStatus = (String) businessStatusExpression.getValue(planItemInstanceEntity);
            if (StringUtils.isNotEmpty(actualBusinessStatus)) {
                CommandContextUtil.getCmmnRuntimeService().updateBusinessStatus(milestoneInstanceEntity.getCaseInstanceId(), actualBusinessStatus);
            }
        }

        CommandContextUtil.getCmmnHistoryManager(commandContext).recordMilestoneReached(milestoneInstanceEntity);
        CommandContextUtil.getAgenda(commandContext).planOccurPlanItemInstanceOperation(planItemInstanceEntity);
    }

    protected MilestoneInstanceEntity createMilestoneInstance(PlanItemInstanceEntity planItemInstanceEntity, CommandContext commandContext) {
        CmmnEngineConfiguration cmmnEngineConfiguration = CommandContextUtil.getCmmnEngineConfiguration(commandContext);
        MilestoneInstanceEntityManager milestoneInstanceEntityManager = cmmnEngineConfiguration.getMilestoneInstanceEntityManager();
        MilestoneInstanceEntity milestoneInstanceEntity = milestoneInstanceEntityManager.create();
        milestoneInstanceEntity.setName(milestoneNameExpression.getValue(planItemInstanceEntity).toString());
        milestoneInstanceEntity.setTimeStamp(CommandContextUtil.getCmmnEngineConfiguration(commandContext).getClock().getCurrentTime());
        milestoneInstanceEntity.setCaseInstanceId(planItemInstanceEntity.getCaseInstanceId());
        milestoneInstanceEntity.setCaseDefinitionId(planItemInstanceEntity.getCaseDefinitionId());
        milestoneInstanceEntity.setElementId(planItemInstanceEntity.getElementId());
        milestoneInstanceEntity.setTenantId(planItemInstanceEntity.getTenantId());
        milestoneInstanceEntityManager.insert(milestoneInstanceEntity);
        return milestoneInstanceEntity;
    }
    
}
