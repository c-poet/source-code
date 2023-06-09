/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.generator;

import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.MessageFormat;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 可以通过MBG1.3.4+版本提供的table元素的mapperName属性设置统一的名称，使用{0}作为实体类名的占位符。
 * <p>
 * 用法：
 * <pre>
 * &lt;context id="Mysql" targetRuntime="tk.mybatis.mapper.generator.TkMyBatis3Impl" defaultModelType="flat"&gt;
 * &lt;/context&gt;
 * </pre>
 * </p>
 *
 * @author liuzh
 * @since 2016-09-04 09:57
 */
public class TkMyBatis3Impl extends IntrospectedTableMyBatis3Impl {

    @Override
    protected String calculateMyBatis3XmlMapperFileName() {
        StringBuilder sb = new StringBuilder();
        if (stringHasValue(tableConfiguration.getMapperName())) {
            String mapperName = tableConfiguration.getMapperName();
            int ind = mapperName.lastIndexOf('.');
            if (ind != -1) {
                mapperName = mapperName.substring(ind + 1);
            }
            //支持mapperName = "{0}Dao" 等用法
            sb.append(MessageFormat.format(mapperName, fullyQualifiedTable.getDomainObjectName()));
            sb.append(".xml"); //$NON-NLS-1$
        } else {
            sb.append(fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper.xml"); //$NON-NLS-1$
        }
        return sb.toString();
    }

    @Override
    protected void calculateJavaClientAttributes() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(this.calculateJavaClientInterfacePackage());
        sb.append('.');
        if (StringUtility.stringHasValue(this.tableConfiguration.getMapperName())) {
            //支持mapperName = "{0}Dao" 等用法
            sb.append(MessageFormat.format(tableConfiguration.getMapperName(), fullyQualifiedTable.getDomainObjectName()));
        } else {
            if (StringUtility.stringHasValue(this.fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append(this.fullyQualifiedTable.getDomainObjectSubPackage());
                sb.append('.');
            }

            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("Mapper");
        }

        this.setMyBatis3JavaMapperType(sb.toString());
        sb.setLength(0);
        sb.append(this.calculateJavaClientInterfacePackage());
        sb.append('.');
        if (StringUtility.stringHasValue(this.tableConfiguration.getSqlProviderName())) {
            //支持mapperName = "{0}SqlProvider" 等用法
            sb.append(MessageFormat.format(tableConfiguration.getSqlProviderName(), fullyQualifiedTable.getDomainObjectName()));
        } else {
            if (StringUtility.stringHasValue(this.fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append(this.fullyQualifiedTable.getDomainObjectSubPackage());
                sb.append('.');
            }

            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("SqlProvider");
        }

        this.setMyBatis3SqlProviderType(sb.toString());
        sb.setLength(0);
        sb.append(this.calculateDynamicSqlSupportPackage());
        sb.append('.');
        if (StringUtility.stringHasValue(this.tableConfiguration.getDynamicSqlSupportClassName())) {
            sb.append(this.tableConfiguration.getDynamicSqlSupportClassName());
        } else {
            if (StringUtility.stringHasValue(this.fullyQualifiedTable.getDomainObjectSubPackage())) {
                sb.append(this.fullyQualifiedTable.getDomainObjectSubPackage());
                sb.append('.');
            }

            sb.append(this.fullyQualifiedTable.getDomainObjectName());
            sb.append("DynamicSqlSupport");
        }

        this.setMyBatisDynamicSqlSupportType(sb.toString());
        if (StringUtility.stringHasValue(this.tableConfiguration.getDynamicSqlTableObjectName())) {
            this.setMyBatisDynamicSQLTableObjectName(this.tableConfiguration.getDynamicSqlTableObjectName());
        } else {
            this.setMyBatisDynamicSQLTableObjectName(this.fullyQualifiedTable.getDomainObjectName());
        }
    }
}
