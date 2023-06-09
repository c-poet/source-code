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
package org.flowable.editor.language.xml.migration;

import static org.assertj.core.api.Assertions.assertThat;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.editor.language.xml.util.BpmnXmlConverterTest;

class AsyncConverterTest {

    @BpmnXmlConverterTest("migration/camunda_async.bpmn")
    void validateModel(BpmnModel model) {
        FlowElement flowElement = model.getMainProcess().getFlowElement("asyncTask");
        assertThat(flowElement)
                .isInstanceOfSatisfying(ServiceTask.class, serviceTask -> {
                    assertThat(serviceTask.getId()).isEqualTo("asyncTask");
                });

        ServiceTask serviceTask = (ServiceTask) flowElement;
        assertThat(serviceTask.isAsynchronous()).isEqualTo(true);
        assertThat(serviceTask.isExclusive()).isEqualTo(true);
        assertThat(serviceTask.getImplementationType()).isEqualTo(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        assertThat(serviceTask.getImplementation()).isEqualTo("${someBean.test(execution)}");
    }
}
