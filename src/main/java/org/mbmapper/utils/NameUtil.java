package org.mbmapper.utils;

import org.mbmapper.produce.describe.Type;

import java.sql.JDBCType;
import java.util.regex.Matcher;

public class NameUtil {

    /**
     * 将数据库字段名转成驼峰命名
     *
     * @param name 名称
     */
    public static String toHumpName(String name) {
        Matcher matcher = RegexUtil.matcher("(?<=\\w)_(\\w)", name);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString().replace("_", "");
    }

    /**
     * 将名称首字母大写
     *
     * @param name 名称
     */
    public static String firstToUpperCase(String name) {
        return name.replaceFirst("\\w", name.substring(0, 1).toUpperCase());
    }

    /**
     * 将名称首字母小写
     *
     * @param name 名称
     */
    public static String firstToLowerCase(String name) {
        return name.replaceFirst("\\w", name.substring(0, 1).toLowerCase());
    }

    /**
     * 将jdbc类型编码转换成对应的java类型
     *
     * @return key代表类型, value代表引入的包
     */
    public static Type jdbcType(int type) {
        if (JDBCType.CHAR.getVendorTypeNumber().equals(type) || JDBCType.VARCHAR.getVendorTypeNumber().equals(type)
                || JDBCType.LONGVARCHAR.getVendorTypeNumber().equals(type)) {
            return new Type("String", null);
        } else if (JDBCType.NUMERIC.getVendorTypeNumber().equals(type) || JDBCType.DECIMAL.getVendorTypeNumber().equals(type)) {
            return new Type("BigDecimal", "java.math.BigDecimal");
        } else if (JDBCType.BIT.getVendorTypeNumber().equals(type) || JDBCType.BOOLEAN.getVendorTypeNumber().equals(type)) {
            return new Type("Boolean", null);
        } else if (JDBCType.TINYINT.getVendorTypeNumber().equals(type)) {
            return new Type("Byte", null);
        } else if (JDBCType.SMALLINT.getVendorTypeNumber().equals(type)) {
            return new Type("Short", null);
        } else if (JDBCType.INTEGER.getVendorTypeNumber().equals(type)) {
            return new Type("Integer", null);
        } else if (JDBCType.BIGINT.getVendorTypeNumber().equals(type)) {
            return new Type("Long", null);
        } else if (JDBCType.REAL.getVendorTypeNumber().equals(type)) {
            return new Type("Float", null);
        } else if (JDBCType.FLOAT.getVendorTypeNumber().equals(type) || JDBCType.DOUBLE.getVendorTypeNumber().equals(type)) {
            return new Type("Double", null);
        } else if (JDBCType.BINARY.getVendorTypeNumber().equals(type) || JDBCType.VARBINARY.getVendorTypeNumber().equals(type) || JDBCType.LONGVARBINARY.getVendorTypeNumber().equals(type)) {
            return new Type("Byte[]", null);
        } else if (JDBCType.DATE.getVendorTypeNumber().equals(type)) {
            return new Type("Date", "java.sql.Date");
        } else if (JDBCType.TIME.getVendorTypeNumber().equals(type)) {
            return new Type("Time", "java.sql.Time");
        } else if (JDBCType.TIMESTAMP.getVendorTypeNumber().equals(type)) {
            return new Type("Timestamp", "java.sql.Timestamp");
        } else if (JDBCType.CLOB.getVendorTypeNumber().equals(type)) {
            return new Type("Clob", "java.sql.Clob");
        } else if (JDBCType.BLOB.getVendorTypeNumber().equals(type)) {
            return new Type("Blob", "java.sql.Blob");
        } else if (JDBCType.ARRAY.getVendorTypeNumber().equals(type)) {
            return new Type("Array", "java.sql.Array");
        } else if (JDBCType.STRUCT.getVendorTypeNumber().equals(type)) {
            return new Type("Struct", "java.sql.Struct");
        } else if (JDBCType.REF.getVendorTypeNumber().equals(type)) {
            return new Type("Ref", "java.sql.Ref");
        } else if (JDBCType.DATALINK.getVendorTypeNumber().equals(type)) {
            return new Type("URL", "java.net.URL");
        }
        return null;
    }

}
