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
package com.alibaba.druid.bvt.sql.oracle.create;

import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

public class OracleCreateFunctionTest_5 extends OracleTest {
    public void test_types() throws Exception {
        String sql = //
                "CREATE OR REPLACE FUNCTION fibonacci wrapped \n" +
                        "a000000\n" +
                        "b2\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "8\n" +
                        "14a fb\n" +
                        "e1Yq3QQJoEoNKIeJlbgLoLdSgogwgxDcf8vWfHSKbuowFOXFKoj9MqYGqWyRxeeCUVqNVIO1\n" +
                        "ICqJa3yPr6e7z8GZpMH3J0Cx0uQ0B1JuysymdNDlzfTvb7QWsrLU4jGs3h8Mm49/L9nyO4Xh\n" +
                        "Ae06nawFpOJIAYpBf9wBVC+ZrjU/nuEtokBqCce6HWIoF6rYgz0V0W/47x5KpOnQ2i7X3kFe\n" +
                        "FR8K7jT7X58k8xK9uYlZv5LhV71a7A==";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);
        print(statementList);

        assertEquals(1, statementList.size());

        assertEquals("CREATE OR REPLACE FUNCTION fibonacci WRAPPED \n" +
                        "b2\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "abcd\n" +
                        "8\n" +
                        "14a fb\n" +
                        "e1Yq3QQJoEoNKIeJlbgLoLdSgogwgxDcf8vWfHSKbuowFOXFKoj9MqYGqWyRxeeCUVqNVIO1\n" +
                        "ICqJa3yPr6e7z8GZpMH3J0Cx0uQ0B1JuysymdNDlzfTvb7QWsrLU4jGs3h8Mm49/L9nyO4Xh\n" +
                        "Ae06nawFpOJIAYpBf9wBVC+ZrjU/nuEtokBqCce6HWIoF6rYgz0V0W/47x5KpOnQ2i7X3kFe\n" +
                        "FR8K7jT7X58k8xK9uYlZv5LhV71a7A==",//
                SQLUtils.toSQLString(stmt, JdbcConstants.ORACLE));

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        assertEquals(0, visitor.getTables().size());
        assertEquals(0, visitor.getColumns().size());

//        assertTrue(visitor.getColumns().contains(new TableStat.Column("orders", "order_total")));
    }
}
