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
package org.flowable.cmmn.engine.impl.agenda.operation;

import org.flowable.cmmn.api.runtime.PlanItemInstanceState;
import org.flowable.cmmn.engine.impl.persistence.entity.PlanItemInstanceEntity;
import org.flowable.cmmn.engine.impl.util.CommandContextUtil;
import org.flowable.cmmn.model.PlanItemTransition;
import org.flowable.common.engine.impl.interceptor.CommandContext;

/**
 * @author Joram Barrez
 */
public class EnablePlanItemInstanceOperation extends AbstractChangePlanItemInstanceStateOperation {

    protected String entryCriterionId;

    public EnablePlanItemInstanceOperation(CommandContext commandContext, PlanItemInstanceEntity planItemInstanceEntity, String entryCriterionId) {
        super(commandContext, planItemInstanceEntity);
        this.entryCriterionId = entryCriterionId;
    }
    
    @Override
    public String getLifeCycleTransition() {
        return PlanItemTransition.ENABLE;
    }
    
    @Override
    public String getNewState() {
        return PlanItemInstanceState.ENABLED;
    }
    
    @Override
    protected void internalExecute() {

        // Sentries are not needed to be kept around, as the plan item is being enabled
        removeSentryRelatedData();

        planItemInstanceEntity.setEntryCriterionId(entryCriterionId);
        planItemInstanceEntity.setLastEnabledTime(getCurrentTime(commandContext));
        CommandContextUtil.getCmmnHistoryManager(commandContext).recordPlanItemInstanceEnabled(planItemInstanceEntity);
    }

    @Override
    public String getOperationName() {
        return "[Enable plan item]";
    }

}
