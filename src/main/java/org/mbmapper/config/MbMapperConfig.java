package org.mbmapper.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mbmapper.utils.PropertyLoader;

@Data
@AllArgsConstructor
public class MbMapperConfig {

    /** 是否打印详细日志, 默认false */
    private boolean printLog = false;
    /** 日志存放地址, 默认 ./mbmapper.log */
    private String logPath = "./mbmapper.log";

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

    /** java代码输出输出路径, 绝对路径 */
    private String javaDir;
    /** 实体类包名, 默认'vo' */
    private String voPackage = "vo";
    /** dao层接口包名, 默认'dao' */
    private String daoPackage = "dao";
    /** xml文件保存路径, 默认在dao层接口文件夹下 */
    private String xmlDir;

    /** xml文件名称后缀, 默认'Mapper' */
    private String suffixStr = "Mapper";

    /** 是否启用捆绑关联表, 默认 false, 当开启后, 如果表 a 外键关联着表 b, 而你在 tables 中没有配置生成表 b, 那么系统会帮助你生成表 b */
    private boolean useRope = false;

    /** 要写出的表, 默认写出所有的表 */
    private String tables = "*";

    /**
     * 创建一个配置类
     * 使用默认的配置路径加载配置路径, 配置文件在 classpath:mbmapper.properties
     */
    public MbMapperConfig() throws MbMapperConfigException {
        new PropertyLoader().load(this, "mbmapper.properties");
        verify();
    }

    /**
     * 创建一个配置类
     * 带指定配置文件的初始化, 不想使用默认配置路径, 可以使用该构造函数
     * @param propertiesFile 配置文件地址路径, 使用相对路径
     */
    public MbMapperConfig(String propertiesFile) throws MbMapperConfigException {
        new PropertyLoader().load(this, propertiesFile);
        verify();
    }


    /**
     * 验证配置属性是否符合规范
     */
    private void verify() throws MbMapperConfigException {
        if (url == null) throw new MbMapperConfigException("Configuration item 'url' cannot be empty");
        if (user == null) throw new MbMapperConfigException("Configuration item 'user' cannot be empty");
        if (password == null) throw new MbMapperConfigException("Configuration item 'user' cannot be empty");
        if (driver == null) throw new MbMapperConfigException("Configuration item 'driver' cannot be empty");
    }

}
