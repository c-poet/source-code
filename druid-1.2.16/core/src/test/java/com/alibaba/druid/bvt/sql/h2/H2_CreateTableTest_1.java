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
package com.alibaba.druid.bvt.sql.h2;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.OracleTest;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Assert;

import java.util.List;

public class H2_CreateTableTest_1 extends OracleTest {
    public void test_0() throws Exception {
        String sql = //
                "CREATE TABLE tutorials_tbl ( \n" +
                        "   id bigint generated by default as identity, \n" +
                        "   title VARCHAR(50) NOT NULL, \n" +
                        "   author VARCHAR(20) NOT NULL, \n" +
                        "   submission_date DATE \n" +
                        ");"; //

        SQLStatement stmt = SQLUtils.parseSingleStatement(sql, DbType.h2);
        assertEquals("CREATE TABLE tutorials_tbl (\n" +
                "\tid bigint GENERATED ALWAYS AS DEFAULT AS IDENTITY,\n" +
                "\ttitle VARCHAR(50) NOT NULL,\n" +
                "\tauthor VARCHAR(20) NOT NULL,\n" +
                "\tsubmission_date DATE\n" +
                ");", stmt.toString());
    }
}
