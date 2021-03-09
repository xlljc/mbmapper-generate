package org.mbmapper.utils;

import org.mbmapper.MbMapperException;
import org.mbmapper.produce.MbLog;
import org.mbmapper.produce.describe.KeyValue;

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
    public static KeyValue<String, String> jdbcType(int type) {
        if (JDBCType.CHAR.getVendorTypeNumber().equals(type) || JDBCType.VARCHAR.getVendorTypeNumber().equals(type)
                || JDBCType.LONGVARCHAR.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("String", null);
        } else if (JDBCType.NUMERIC.getVendorTypeNumber().equals(type) || JDBCType.DECIMAL.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("BigDecimal", "java.math.BigDecimal");
        } else if (JDBCType.BIT.getVendorTypeNumber().equals(type) || JDBCType.BOOLEAN.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Boolean", null);
        } else if (JDBCType.TINYINT.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Byte", null);
        } else if (JDBCType.SMALLINT.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Short", null);
        } else if (JDBCType.INTEGER.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Integer", null);
        } else if (JDBCType.BIGINT.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Long", null);
        } else if (JDBCType.REAL.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Float", null);
        } else if (JDBCType.FLOAT.getVendorTypeNumber().equals(type) || JDBCType.DOUBLE.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Double", null);
        } else if (JDBCType.BINARY.getVendorTypeNumber().equals(type) || JDBCType.VARBINARY.getVendorTypeNumber().equals(type) || JDBCType.LONGVARBINARY.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Byte[]", null);
        } else if (JDBCType.DATE.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Date", "java.sql.Date");
        } else if (JDBCType.TIME.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Time", "java.sql.Time");
        } else if (JDBCType.TIMESTAMP.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Timestamp", "java.sql.Timestamp");
        } else if (JDBCType.CLOB.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Clob", "java.sql.Clob");
        } else if (JDBCType.BLOB.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Blob", "java.sql.Blob");
        } else if (JDBCType.ARRAY.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Array", "java.sql.Array");
        } else if (JDBCType.STRUCT.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Struct", "java.sql.Struct");
        } else if (JDBCType.REF.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("Ref", "java.sql.Ref");
        } else if (JDBCType.DATALINK.getVendorTypeNumber().equals(type)) {
            return new KeyValue<>("URL", "java.net.URL");
        }
        return null;
    }

}
