package org.mbmapper.produce;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.utils.RegexUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 日志输出类
 */
public class MbLog {

    /** 配置文件 */
    private static MbMapperConfig config;

    /** 日志内容 */
    private static StringBuilder logStr;

    /**
     * 开始写出日志, 传入配置文件
     * @param config 配置文件
     */
    public static void start(MbMapperConfig config) {
        MbLog.config = config;
        logStr = new StringBuilder();
    }

    /**
     * 打印消息, 会被写入日志, 不过不管 printLog 是否打开, 都会打印
     * @param msg 消息内容
     */
    public static void print(String msg) {
        if (logStr != null) logStr.append(msg).append("\n");
        System.out.println(msg);
    }

    /**
     * 打印消息, 会被写入日志, 不过不管 printLog 是否打开, 都会打印
     * @param msg 消息内容
     * @param bgc 背景颜色
     * @param color 字体颜色
     * @param underline 是否带下划线
     */
    public static void print(String msg, Bgc bgc,Color color,boolean underline) {
        if (logStr != null) logStr.append(msg).append("\n");
        String bgcVal = bgc.getValue();
        String colorVal = color.getValue();
        if (bgcVal != null && colorVal != null) {
            System.out.printf("%s\033[%s;%sm%s\033[0m%n", underline ? "\033[4m" : "", bgcVal, colorVal, msg);
        } else if (bgcVal != null) {
            System.out.printf("%s\033[%sm%s\033[0m%n", underline ? "\033[4m" : "", bgcVal, msg);
        } else if (colorVal != null) {
            System.out.printf("%s\033[%sm%s\033[0m%n", underline ? "\033[4m" : "", colorVal, msg);
        } else {
            System.out.printf("%s%s\033[0m%n", underline ? "\033[4m" : "", msg);
        }
    }

    /**
     * 打印日志, 如果 printLog 没打开, 则不会输出日志
     * @param msg 消息内容
     */
    public static void log(String msg) {
        if (config != null && !config.isPrintLog()) return;
        print(msg);
    }

    /**
     * 打印日志, 如果 printLog 没打开, 则不会输出日志
     * @param msg 消息内容
     * @param bgc 背景颜色
     * @param color 字体颜色
     * @param underline 是否带下划线
     */
    public static void log(String msg, Bgc bgc,Color color,boolean underline) {
        if (config != null && !config.isPrintLog()) return;
        print(msg, bgc, color, underline);
    }

    /**
     * 打印分界线
     */
    public static void line() {
        log("####################################################################################################");
    }

    /**
     * 输出警告信息
     */
    public static void logWarning(String msg) {
        log(msg, Bgc.normal, Color.yellow, true);
    }

    /**
     * 输出成功信息
     */
    public static void logSuccess(String msg) {
        log(msg, Bgc.normal, Color.green, false);
    }

    /**
     * 输出提示信息
     */
    public static void logInfo(String msg) {
        log(msg, Bgc.normal, Color.blue, false);
    }

    /**
     * 输出错误消息
     */
    public static void logError(String msg) {
        log(msg, Bgc.normal, Color.red, false);
    }


    /**
     * 执行写出操作, 将日志写出到磁盘中
     */
    public static void write() {
        String logPath;
        //获取配置项 logPath 的路径, 提取其中的保存文件名, 如果不存在, 就使用默认的名称
        if (!RegexUtil.matches(".*[^/\\\\]+\\..+$", config.getLogPath())) { //没写详细名称
            logPath = config.getLogPath() + "/mbmapper.log";
        } else { //写了详细名称
            logPath = config.getLogPath();
        }

        File file = new File(logPath);
        try {
            //写出操作
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(logStr.toString().getBytes());
            fileOutputStream.close();
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 背景颜色
     */
    public enum Bgc {
        /** 正常颜色 */
        normal(null),
        /** 黑色 */
        black("40"),
        /** 红色 */
        red("41"),
        /** 绿色 */
        green("42"),
        /** 黄色 */
        yellow("43"),
        /** 蓝色 */
        blue("44"),
        /** 紫色 */
        purple("45"),
        /** 深绿 */
        darkGreen("46"),
        /** 白色 */
        white("47");

        private final String val;
        Bgc(String value) {
            val = value;
        }
        /**
         * 获取值
         */
        public String getValue() {
            return val;
        }
    }

    /**
     * 字体颜色
     */
    public enum Color {
        /** 正常颜色 */
        normal(null),
        /** 黑色 */
        black("30"),
        /** 红色 */
        red("31"),
        /** 绿色 */
        green("32"),
        /** 黄色 */
        yellow("33"),
        /** 蓝色 */
        blue("34"),
        /** 紫色 */
        purple("35"),
        /** 深绿 */
        darkGreen("36"),
        /** 白色 */
        white("37");

        private final String val;
        Color(String value) {
            val = value;
        }
        /**
         * 获取值
         */
        public String getValue() {
            return val;
        }
    }

}
