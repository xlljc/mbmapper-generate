package org.mbmapper.utils;

import java.io.*;

public class FileUtil {

    /**
     * 读取一个文件, 返回文件内容的字符串
     * @param file 指定文件
     * @return 文件内容
     */
    public static String loadFile(File file) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
        return builder.toString();
    }

    /**
     * 将一个文件写出到磁盘
     * @param content 文件内容
     * @param file 写出的指定文件
     */
    public static void writeFile(String content,File file) {

    }

}
