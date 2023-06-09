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
package org.flowable.ui.idm.model;

import java.util.ArrayList;
import java.util.List;

import org.flowable.ui.common.model.AbstractRepresentation;

/**
 * @author Joram Barrez
 */
public class UpdateUsersRepresentation extends AbstractRepresentation {

    protected String id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String tenantId;
    protected String password;
    protected List<String> users = new ArrayList<>();

    public UpdateUsersRepresentation() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.isEmpty()) {
            this.tenantId = null;
        } else {
            this.tenantId = tenantId;
        }
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

}
