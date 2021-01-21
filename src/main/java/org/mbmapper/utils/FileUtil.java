package org.mbmapper.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
    public static void writeFile(String content,File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content.getBytes());
            fileOutputStream.flush();
        }
    }

    /**
     * 文件的拷贝
     * @param readFile 需要拷贝的文件
     * @param newFile 保存的文件
     */
    public static void copyFile(File readFile, File newFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(newFile);
             Writer writer = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = new BufferedWriter(writer);
             BufferedReader bufferedReader = new BufferedReader(new FileReader(readFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
    }

}
