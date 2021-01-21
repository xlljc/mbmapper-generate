package org.mbmapper.produce.file;

import lombok.Data;
import org.mbmapper.produce.MbLog;
import org.mbmapper.utils.FileUtil;

import java.io.File;
import java.io.IOException;

@Data
public class JavaFileLoader {

    private final File javaFile;

    private String javaContent;

    /**
     * 通过java文件路径创建对象
     */
    public JavaFileLoader(String path) {
        javaFile = new File(path);
    }

    /**
     * 加载 java 文件
     */
    public void load() throws IOException {
        MbLog.log(String.format("JavaFileLoader.load(): => Load file '%s'.", javaFile.getName()));
        //如果存在该文件, 就读取, 反之则视为空文件
        if (javaFile.exists()) {
            //文件读取操作
            javaContent = FileUtil.loadFile(javaFile);
            MbLog.logSuccess(String.format("JavaFileLoader.load(): => Load file '%s' success.", javaFile.getName()));
        } else {
            MbLog.log(String.format("JavaFileLoader.load(): => File '%s' does not exist, create empty file.", javaFile.getName()));
            javaContent = "";
        }
    }
}
