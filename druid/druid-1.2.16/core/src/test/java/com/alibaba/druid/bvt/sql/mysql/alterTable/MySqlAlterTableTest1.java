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
package com.alibaba.druid.bvt.sql.mysql.alterTable;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.Token;
import junit.framework.TestCase;
import org.junit.Assert;

public class MySqlAlterTableTest1 extends TestCase {
    public void test_alter_0() throws Exception {
        String sql = "ALTER TABLE t1 RENAME t2;";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("RENAME TABLE t1 TO t2;", output);
    }

    public void test_alter_1() throws Exception {
        String sql = "ALTER TABLE t2 ADD d TIMESTAMP;";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("ALTER TABLE t2\n\tADD COLUMN d TIMESTAMP;", output);
    }

    public void test_alter_2() throws Exception {
        String sql = "ALTER TABLE t2 ADD INDEX (d), ADD UNIQUE (a);";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("ALTER TABLE t2\n\tADD INDEX (d),\n\tADD UNIQUE (a);", output);
    }

    public void test_alter_3() throws Exception {
        String sql = "ALTER TABLE t1 RENAME TO t2;";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("RENAME TABLE t1 TO t2;", output);
    }

    public void test_alter_4() throws Exception {
        String sql = "ALTER TABLE t1 RENAME AS t2;";
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        SQLStatement stmt = parser.parseStatementList().get(0);
        parser.match(Token.EOF);
        String output = SQLUtils.toMySqlString(stmt);
        Assert.assertEquals("RENAME TABLE t1 TO t2;", output);
    }

}
