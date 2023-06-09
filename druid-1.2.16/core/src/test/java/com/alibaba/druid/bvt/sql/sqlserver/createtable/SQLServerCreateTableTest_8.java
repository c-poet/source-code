/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
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
package com.alibaba.druid.bvt.sql.sqlserver.createtable;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Column;
import com.alibaba.druid.util.JdbcConstants;

public class SQLServerCreateTableTest_8 extends TestCase {
    public void test_0() throws Exception {
        String sql = "create table ACT_RU_VARIABLE ("
                + " ID_ nvarchar(64) not null,"
                + " DOUBLE_ double precision,"
                + " LONG_ numeric(19,0),"
                + " TEXT_ nvarchar(4000),"
                + " primary key (ID_)"
                + ")";

        SQLServerStatementParser parser = new SQLServerStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLCreateTableStatement stmt = (SQLCreateTableStatement) statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        String output = SQLUtils.toSQLString(stmt, JdbcConstants.SQL_SERVER);
        Assert.assertEquals("CREATE TABLE ACT_RU_VARIABLE ("
                + "\n\tID_ nvarchar(64) NOT NULL,"
                + "\n\tDOUBLE_ DOUBLE PRECISION,"
                + "\n\tLONG_ numeric(19, 0),"
                + "\n\tTEXT_ nvarchar(4000),"
                + "\n\tPRIMARY KEY (ID_)"
                + "\n)", output);

        SQLServerSchemaStatVisitor visitor = new SQLServerSchemaStatVisitor();
        stmt.accept(visitor);

//        System.out.println("Tables : " + visitor.getTables());
//        System.out.println("fields : " + visitor.getColumns());
//        System.out.println("coditions : " + visitor.getConditions());
//        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(4, visitor.getColumns().size());
        Assert.assertEquals(0, visitor.getConditions().size());

        Assert.assertTrue(visitor.getTables().containsKey(new TableStat.Name("ACT_RU_VARIABLE")));

        Assert.assertTrue(visitor.getColumns().contains(new Column("ACT_RU_VARIABLE", "ID_")));
        Assert.assertTrue(visitor.getColumns().contains(new Column("ACT_RU_VARIABLE", "DOUBLE_")));
    }
}
