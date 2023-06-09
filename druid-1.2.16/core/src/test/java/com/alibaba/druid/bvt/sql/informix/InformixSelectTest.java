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
package com.alibaba.druid.bvt.sql.informix;

import com.alibaba.druid.sql.PGTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.List;

public class InformixSelectTest extends TestCase {
    public void test_0() throws Exception {
        String sql = "select skip 500 first 500 * from pub_menu ";

        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.INFORMIX);
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals("SELECT SKIP 500 FIRST 500 *\n" +
                "FROM pub_menu", SQLUtils.toSQLString(stmt, JdbcConstants.INFORMIX));

        Assert.assertEquals("select skip 500 first 500 *\n" +
                "from pub_menu", SQLUtils.toSQLString(stmt, JdbcConstants.INFORMIX, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION));

        Assert.assertEquals(1, statementList.size());

        PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
        stmt.accept(visitor);

//        System.out.println("Tables : " + visitor.getTables());
//        System.out.println("fields : " + visitor.getColumns());
//        System.out.println("coditions : " + visitor.getConditions());

        Assert.assertEquals(1, visitor.getColumns().size());
        Assert.assertEquals(1, visitor.getTables().size());
    }
}
