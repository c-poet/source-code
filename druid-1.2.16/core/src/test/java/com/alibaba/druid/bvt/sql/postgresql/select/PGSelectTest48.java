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
package com.alibaba.druid.bvt.sql.postgresql.select;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import junit.framework.TestCase;

import java.util.List;

public class PGSelectTest48 extends TestCase {
    private final DbType dbType = JdbcConstants.POSTGRESQL;

    public void test_0() throws Exception {
        String sql = "SELECT\n" +
                "\"result\" ->> 'PROD_TYPE_NAME' ,\n" +
                "\"result\" ->> 'IMAGES'\n" +
                "FROM\n" +
                "mobile_info_cnmo";

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        SQLStatement stmt = stmtList.get(0);

        assertEquals("SELECT \"result\" ->> 'PROD_TYPE_NAME', \"result\" ->> 'IMAGES'\n" +
                "FROM mobile_info_cnmo", SQLUtils.toPGString(stmt));

        assertEquals("select \"result\" ->> 'PROD_TYPE_NAME', \"result\" ->> 'IMAGES'\n" +
                "from mobile_info_cnmo", SQLUtils.toPGString(stmt, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION));

        assertEquals(1, stmtList.size());

        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(dbType);
        stmt.accept(visitor);

//        System.out.println("Tables : " + visitor.getTables());
//        System.out.println("fields : " + visitor.getColumns());
//        System.out.println("coditions : " + visitor.getConditions());

        assertEquals(1, visitor.getColumns().size());
        assertEquals(1, visitor.getTables().size());
    }
}
