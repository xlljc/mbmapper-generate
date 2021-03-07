package org.mbmapper.utils;


import org.mbmapper.config.MbMapperConfigException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * 属性加载器
 */
public class PropertyLoader {

    /**
     * 通过传入的配置文件初始化对象属性
     *
     * @param obj            需要初始化属性的对象
     * @param propertiesFile properties文件路径
     */
    public void load(Object obj, String propertiesFile) throws MbMapperConfigException {
        try {
            Properties properties = new Properties();
            //获取配置文件url
            URL url = getClass().getClassLoader().getResource(propertiesFile);
            //如果没有该文件就抛出异常
            if (url == null) throw new FileNotFoundException(String.format("Properties file '%s' not found!", propertiesFile));
            InputStream inputStream = new FileInputStream(url.getFile());

            //加载配置文件
            properties.load(inputStream);

            //循环遍历每一个字段, 从properties中读取值
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Class<?> type = field.getType();
                    //如果字段有默认值, 而且配置文件中没有该配置, 就使用原有的值
                    Object val = field.get(obj);
                    String temp;
                    if (val != null) {
                        temp = properties.getProperty(field.getName(), val.toString());
                    } else {
                        temp = properties.getProperty(field.getName());
                    }
                    if (temp != null) {
                        temp = refProperties(temp,properties);
                        field.set(obj, toTargetType(type, temp));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new MbMapperConfigException(e.getMessage());
        }
    }

    /**
     * 设置引用
     * @param str 原字符串
     * @param properties 配置文件
     */
    private String refProperties(String str,Properties properties) throws MbMapperConfigException {
        Matcher matcher = RegexUtil.matcher("(?<!\\\\)\\$\\{\\w+}", str);
        String s = str;
        while (matcher.find()) {
            String temp = matcher.group();
            String key = temp.substring(2, temp.length() - 1);
            String refString = properties.getProperty(key);
            if (refString == null) throw new MbMapperConfigException(String.format("Unknown reference '%s' in '%s'", key, str));
            s = s.replace(temp, refString);
        }
        return s;
    }

    /**
     * 将数值转换成指定的类型
     * @param type 类型
     * @param val 需要转换的值
     */
    private Object toTargetType(Class<?> type, Object val) {
        if (type == String.class || type == Object.class) {
            return val;
        } else if (type == int.class || type == Integer.class) {
            return Integer.parseInt(val.toString());
        } else if (type == float.class || type == Float.class) {
            return Float.parseFloat(val.toString());
        } else if (type == boolean.class || type == Boolean.class) {
            return Boolean.parseBoolean(val.toString());
        }
        return val;
    }

}
