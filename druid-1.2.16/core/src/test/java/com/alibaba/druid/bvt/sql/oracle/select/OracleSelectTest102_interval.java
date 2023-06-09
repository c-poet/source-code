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
package com.alibaba.druid.bvt.sql.oracle.select;

import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import org.junit.Assert;

import java.util.List;

public class OracleSelectTest102_interval extends OracleTest {
    public void test_0() throws Exception {
        String sql = //
                "SELECT id, bill_batch_no, partner_id, currency, original_amount\n" +
                        "  , exchange_rate, real_currency, real_amount, success_count, failure_count\n" +
                        "  , failure_amount, bill_date, expiry_date, status, remit_no\n" +
                        "  , exchange_date, apply_bill_date, check_date, charge_off_date, gmt_create\n" +
                        "  , gmt_modified\n" +
                        "FROM ktv_bill_info\n" +
                        "WHERE status = ?\n" +
                        "  AND gmt_modified > SYSDATE - INTERVAL ? MINUTE\n" +
                        "  AND gmt_modified <= SYSDATE - INTERVAL ? MINUTE\n" +
                        "  AND rownum <= ?\n" +
                        "ORDER BY gmt_modified ASC";

        System.out.println(sql);

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLSelectStatement stmt = (SQLSelectStatement) statementList.get(0);
        System.out.println(stmt.toString());

        Assert.assertEquals(1, statementList.size());

//        SQLMethodInvokeExpr expr = (SQLMethodInvokeExpr) stmt.getSelect().getQueryBlock().getSelectList().get(0).getExpr();
//        SQLMethodInvokeExpr param0 = (SQLMethodInvokeExpr) expr.getParameters().get(0);
//        assertTrue(param0.getParameters().get(0)
//                instanceof SQLAggregateExpr);

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        stmt.accept(visitor);

        {
            String text = SQLUtils.toOracleString(stmt);

            assertEquals("SELECT id, bill_batch_no, partner_id, currency, original_amount\n" +
                    "\t, exchange_rate, real_currency, real_amount, success_count, failure_count\n" +
                    "\t, failure_amount, bill_date, expiry_date, status, remit_no\n" +
                    "\t, exchange_date, apply_bill_date, check_date, charge_off_date, gmt_create\n" +
                    "\t, gmt_modified\n" +
                    "FROM ktv_bill_info\n" +
                    "WHERE status = ?\n" +
                    "\tAND gmt_modified > SYSDATE - INTERVAL ? MINUTE\n" +
                    "\tAND gmt_modified <= SYSDATE - INTERVAL ? MINUTE\n" +
                    "\tAND rownum <= ?\n" +
                    "ORDER BY gmt_modified ASC", text);
        }

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        assertEquals(1, visitor.getTables().size());
        assertEquals(21, visitor.getColumns().size());
        assertEquals(4, visitor.getConditions().size());
        assertEquals(0, visitor.getRelationships().size());
        assertEquals(1, visitor.getOrderByColumns().size());
    }


}
