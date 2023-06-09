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
package org.flowable.engine.impl.bpmn.listener;

import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.delegate.invocation.ExecutionListenerInvocation;
import org.flowable.engine.impl.delegate.invocation.JavaDelegateInvocation;
import org.flowable.engine.impl.util.CommandContextUtil;

/**
 * {@link ExecutionListener} handling both: {@link ExecutionListener} and {@link JavaDelegate}.
 * Routes invocation through configured <code>DelegateInterceptor</code>.
 *
 * @author Arthur Hupka-Merle
 */
public class DelegateExecutionListener implements ExecutionListener {

    protected Object delegate;

    public DelegateExecutionListener(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public void notify(DelegateExecution execution) {
        if (delegate instanceof ExecutionListener) {
            CommandContextUtil.getProcessEngineConfiguration().getDelegateInterceptor()
                    .handleInvocation(new ExecutionListenerInvocation((ExecutionListener) delegate, execution));
        } else if (delegate instanceof JavaDelegate) {
            CommandContextUtil.getProcessEngineConfiguration().getDelegateInterceptor()
                    .handleInvocation(new JavaDelegateInvocation((JavaDelegate) delegate, execution));
        } else {
            throw new FlowableIllegalArgumentException(
                    delegate.getClass().getName() + " doesn't implement " + ExecutionListener.class + " nor " + JavaDelegate.class);
        }
    }
}
