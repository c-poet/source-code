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
package org.flowable.bpmn.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Tijs Rademakers
 * @author Joram Barrez
 */
public abstract class FlowNode extends FlowElement {

    protected boolean asynchronous;
    protected boolean asynchronousLeave;
    protected boolean notExclusive;

    protected List<SequenceFlow> incomingFlows = new ArrayList<>();
    protected List<SequenceFlow> outgoingFlows = new ArrayList<>();

    @JsonIgnore
    protected Object behavior;

    public FlowNode() {

    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public boolean isAsynchronousLeave() {
        return asynchronousLeave;
    }

    public void setAsynchronousLeave(boolean asynchronousLeave) {
        this.asynchronousLeave = asynchronousLeave;
    }

    public boolean isExclusive() {
        return !notExclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.notExclusive = !exclusive;
    }

    public boolean isNotExclusive() {
        return notExclusive;
    }

    public void setNotExclusive(boolean notExclusive) {
        this.notExclusive = notExclusive;
    }

    public Object getBehavior() {
        return behavior;
    }

    public void setBehavior(Object behavior) {
        this.behavior = behavior;
    }

    public List<SequenceFlow> getIncomingFlows() {
        return incomingFlows;
    }

    public void setIncomingFlows(List<SequenceFlow> incomingFlows) {
        this.incomingFlows = incomingFlows;
    }

    public List<SequenceFlow> getOutgoingFlows() {
        return outgoingFlows;
    }

    public void setOutgoingFlows(List<SequenceFlow> outgoingFlows) {
        this.outgoingFlows = outgoingFlows;
    }

    public void setValues(FlowNode otherNode) {
        super.setValues(otherNode);
        setAsynchronous(otherNode.isAsynchronous());
        setNotExclusive(otherNode.isNotExclusive());
        setAsynchronousLeave(otherNode.isAsynchronousLeave());

        if (otherNode.getIncomingFlows() != null) {
            setIncomingFlows(otherNode.getIncomingFlows()
                    .stream()
                    .map(SequenceFlow::clone)
                    .collect(Collectors.toList()));
        }

        if (otherNode.getOutgoingFlows() != null) {
            setOutgoingFlows(otherNode.getOutgoingFlows()
                    .stream()
                    .map(SequenceFlow::clone)
                    .collect(Collectors.toList()));
        }
    }
}
