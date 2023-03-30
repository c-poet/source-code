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
package com.alibaba.druid.bvt.filter.wall.mysql;

import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.druid.wall.WallProvider;
import com.alibaba.druid.wall.spi.MySqlWallProvider;

/**
 * SQLServerWallTest
 *
 * @author RaymondXiu
 * @version 1.0, 2012-3-18
 * @see
 */
public class MySqlWallTest89 extends TestCase {
    public void test_true() throws Exception {
        WallProvider provider = new MySqlWallProvider();

        provider.getConfig().setSelectHavingAlwayTrueCheck(true);

        Assert.assertTrue(provider.checkValid(//
                "select login('', '', 'guest', '47ea3937793101011caaa5dd88d3bcae926526624796b8a26a9615e8d3bea6b4', 'iPad3,4', 'unknown'),     '', (select max(num) from accounts) +  @@auto_increment_increment"));

        Assert.assertEquals(1, provider.getTableStats().size());
        Assert.assertTrue(provider.getTableStats().containsKey("accounts"));
    }

}
