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
package com.alibaba.druid.bvt.sql.mysql.select;

import com.alibaba.druid.sql.MysqlTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;

public class MySqlSelectTest_67 extends MysqlTest {
    public void test_0() throws Exception {
        String sql = "SELECT\n" +
                "t_aid,t_title,t_path,t_datetime,t_total\n" +
                "FROM t_article WHERE t_state =\n" +
                "? ORDER BY t_total DESC limit ?,?";

        System.out.println(sql);

        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.MYSQL, SQLParserFeature.OptimizedForParameterized);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        assertEquals(1, statementList.size());

        SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(JdbcConstants.MYSQL);
        stmt.accept(visitor);
        {
            String output = SQLUtils.toMySqlString(stmt);
            assertEquals("SELECT t_aid, t_title, t_path, t_datetime, t_total\n" +
                            "FROM t_article\n" +
                            "WHERE t_state = ?\n" +
                            "ORDER BY t_total DESC\n" +
                            "LIMIT ?, ?", //
                    output);
        }
        {
            String output = SQLUtils.toMySqlString(stmt, SQLUtils.DEFAULT_LCASE_FORMAT_OPTION);
            assertEquals("select t_aid, t_title, t_path, t_datetime, t_total\n" +
                            "from t_article\n" +
                            "where t_state = ?\n" +
                            "order by t_total desc\n" +
                            "limit ?, ?", //
                    output);
        }

        {
            String output = SQLUtils.toMySqlString(stmt, new SQLUtils.FormatOption(true, true, true));
            assertEquals("SELECT t_aid, t_title, t_path, t_datetime, t_total\n" +
                            "FROM t_article\n" +
                            "WHERE t_state = ?\n" +
                            "ORDER BY t_total DESC\n" +
                            "LIMIT ?, ?", //
                    output);
        }
    }
}
