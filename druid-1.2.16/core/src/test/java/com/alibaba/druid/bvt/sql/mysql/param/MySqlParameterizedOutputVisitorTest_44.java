package com.alibaba.druid.bvt.sql.mysql.param;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson2.JSONWriter;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenshao on 16/8/23.
 */
public class MySqlParameterizedOutputVisitorTest_44 extends TestCase {
    public void test_for_parameterize() throws Exception {
        final DbType dbType = JdbcConstants.MYSQL;
        String sql = "select 1 from a where c1 in (date_format(date_add(curdate(), INTERVAL -7 DAY), '%Y%m%d'))";

        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
        List<SQLStatement> stmtList = parser.parseStatementList();
        SQLStatement statement = stmtList.get(0);

        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = SQLUtils.createOutputVisitor(out, JdbcConstants.MYSQL);
        List<Object> parameters = new ArrayList<Object>();
        visitor.setParameterized(true);
        visitor.setParameterizedMergeInList(true);
        visitor.setParameters(parameters);
        /*visitor.setPrettyFormat(false);*/
        statement.accept(visitor);
       /* JSONArray array = new JSONArray();
        for(String table : visitor.getTables()){
            array.add(table.replaceAll("`",""));
        }*/

        String psql = out.toString();

        System.out.println(psql);

        assertEquals("SELECT ?\n" +
                "FROM a\n" +
                "WHERE c1 IN (date_format(date_add(curdate(), INTERVAL ? DAY), '%Y%m%d'))", psql);
        String params_json = JSON.toJSONString(parameters, JSONWriter.Feature.WriteClassName);
        System.out.println(params_json);
        JSONArray jsonArray = JSON.parseArray(params_json);
        String json = JSON.toJSONString(jsonArray, JSONWriter.Feature.WriteClassName);
        assertEquals("[1,-7]", json);

        String rsql = SQLUtils.toSQLString(SQLUtils.parseStatements(psql, dbType), dbType, jsonArray);
        assertEquals("SELECT 1\n" +
                "FROM a\n" +
                "WHERE c1 IN (date_format(date_add(curdate(), INTERVAL -7 DAY), '%Y%m%d'))", rsql);
    }
}
