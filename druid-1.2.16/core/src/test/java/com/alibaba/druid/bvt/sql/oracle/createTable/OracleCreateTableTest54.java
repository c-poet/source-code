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
package com.alibaba.druid.bvt.sql.oracle.createTable;

import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;

import java.util.List;

public class OracleCreateTableTest54 extends OracleTest {
    public void test_types() throws Exception {
        String sql = //
                "       CREATE TABLE \"SC_001\".\"TB_001\" \n" +
                        "   (  \"PUSH\" NUMBER, \n" +
                        "  \"LOAD\" NUMBER, \n" +
                        "  \"TABLE_ID\" NUMBER, \n" +
                        "  \"TABLE_NAME\" VARCHAR2(50), \n" +
                        "  \"PRI_KEY\" VARCHAR2(50), \n" +
                        "  \"TEMP_TABLE_NAME\" VARCHAR2(50)\n" +
                        "   ) \n" +
                        "   ORGANIZATION EXTERNAL \n" +
                        "    ( TYPE ORACLE_LOADER\n" +
                        "      DEFAULT DIRECTORY \"DIR_MQ\"\n" +
                        "      ACCESS PARAMETERS\n" +
                        "      ( records delimited by newline fields terminated by '|'    )\n" +
                        "      LOCATION\n" +
                        "       ( 'retl-table.cfg'\n" +
                        "       )\n" +
                        "    )\n" +
                        "   REJECT LIMIT UNLIMITED     ";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);
        print(statementList);

        Assert.assertEquals(1, statementList.size());

        Assert.assertEquals("CREATE TABLE \"SC_001\".\"TB_001\" (\n" +
                        "\t\"PUSH\" NUMBER,\n" +
                        "\t\"LOAD\" NUMBER,\n" +
                        "\t\"TABLE_ID\" NUMBER,\n" +
                        "\t\"TABLE_NAME\" VARCHAR2(50),\n" +
                        "\t\"PRI_KEY\" VARCHAR2(50),\n" +
                        "\t\"TEMP_TABLE_NAME\" VARCHAR2(50)\n" +
                        ")\n" +
                        "ORGANIZATION EXTERNAL (\n" +
                        "\t\tTYPE ORACLE_LOADER\n" +
                        "\t\tDEFAULT DIRECTORY \"DIR_MQ\"\n" +
                        "\t\tACCESS PARAMETERS (\n" +
                        "\t\t\tRECORDS DELIMITED BY NEWLINE\n" +
                        "\t\t\tFIELDS TERMINATED BY '|'\n" +
                        "\t\t)\n" +
                        "\t\tLOCATION ('retl-table.cfg')\n" +
                        "\t)\n" +
                        "\tREJECT LIMIT UNLIMITED",//
                SQLUtils.toSQLString(stmt, JdbcConstants.ORACLE));

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("coditions : " + visitor.getConditions());
        System.out.println("relationships : " + visitor.getRelationships());
        System.out.println("orderBy : " + visitor.getOrderByColumns());

        Assert.assertEquals(1, visitor.getTables().size());

        Assert.assertEquals(6, visitor.getColumns().size());

        Assert.assertTrue(visitor.containsColumn("SC_001.TB_001", "LOAD"));
    }
}
