package org.mbmapper.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mbmapper.utils.PropertyLoader;

import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MbMapperConfig {

    /** 是否打印详细日志, 默认false */
    private boolean printLog = false;

    /** 数据库连接url */
    private String url;
    /** 数据库连接driver */
    private String driver;
    /** 数据库连接user */
    private String user;
    /** 数据库连接password */
    private String password;

    /** 是否启用备份, 默认true */
    private boolean useBackup = true;
    /** 实体层是否使用Lombok, 默认false */
    private boolean useLombok = false;

    /** 代码输出输出路径, 绝对路径 */
    private String srcDir;
    /** 实体类包名, 默认'vo' */
    private String voPackage = "vo";
    /** dao层接口包名, 默认'dao' */
    private String daoPackage = "dao";
    /** xml文件保存路径, 默认在dao层接口文件夹下 */
    private String xmlDir;

    /** 要写出的表, 默认没有 */
    private String tables = "[]";

    /**
     * 创建一个配置类
     * 使用默认的配置路径加载配置路径, 配置文件在 classpath:mbmapper.properties
     */
    public MbMapperConfig() throws IOException {
        new PropertyLoader().load(this,"mbmapper.properties");
    }

    /**
     * 创建一个配置类
     * 带指定配置文件的初始化, 不想使用默认配置路径, 可以使用该构造函数
     * @param propertiesFile 配置文件地址路径, 使用相对路径
     */
    public MbMapperConfig(String propertiesFile) throws IOException {
        new PropertyLoader().load(this,propertiesFile);
    }

}
