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
package org.flowable.editor.language.xml;

import static org.assertj.core.api.Assertions.assertThat;
import static org.flowable.editor.language.xml.util.XmlTestUtils.readXMLFile;

import java.util.List;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.junit.jupiter.api.Test;

class InCompleteSignalConverterTest {

    @Test
    void convertXMLToModel() {
        BpmnModel bpmnModel = readXMLFile("incompletesignalmodel.bpmn");
        validateModel(bpmnModel);
    }

    private void validateModel(BpmnModel model) {
        FlowElement flowElement = model.getMainProcess().getFlowElement("task");
        assertThat(flowElement)
                .isInstanceOfSatisfying(UserTask.class, userTask -> {
                    assertThat(userTask.getId()).isEqualTo("task");
                });

        ProcessValidator processValidator = new ProcessValidatorFactory().createDefaultProcessValidator();
        List<ValidationError> errors = processValidator.validate(model);
        assertThat(errors).hasSize(2);
    }
}