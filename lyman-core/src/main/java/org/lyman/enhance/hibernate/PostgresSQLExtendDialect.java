package org.lyman.enhance.hibernate;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class PostgresSQLExtendDialect extends PostgreSQL95Dialect {

    public PostgresSQLExtendDialect() {
        super();
        registerFunction("convert_gbk", new SQLFunctionTemplate(StandardBasicTypes.STRING,
                "convert_to(?1, 'gbk')"));
    }

}
