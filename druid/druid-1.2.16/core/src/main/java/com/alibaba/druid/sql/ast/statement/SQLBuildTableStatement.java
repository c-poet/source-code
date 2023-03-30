/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLBuildTableStatement extends SQLStatementImpl {
    private SQLName table;
    private SQLIntegerExpr version;
    private boolean withSplit;
    private boolean force;

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.table);
            acceptChild(visitor, this.version);
        }
        visitor.endVisit(this);
    }

    public SQLName getTable() {
        return table;
    }

    public void setTable(SQLName table) {
        this.table = table;
    }

    public SQLIntegerExpr getVersion() {
        return version;
    }

    public void setVersion(SQLIntegerExpr version) {
        this.version = version;
    }

    public boolean isWithSplit() {
        return withSplit;
    }

    public void setWithSplit(boolean withSplit) {
        this.withSplit = withSplit;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
