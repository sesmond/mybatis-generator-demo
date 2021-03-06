package generator.service;


import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenerateMain {

    public static void main(String[] args) {

        String[] configArr = new String[]{
//                "generatorConfig3.xml",
                "mysqlConfig.xml",
//                "generatorConfig3.xml"

        };
        // 执行中的异常信息会保存在warnings中
        List<String> warnings = new ArrayList<String>();
        try {
            // true:生成的文件覆盖之前的
            boolean overwrite = true;
            for (String cfgFile : configArr){
                System.out.println("开始生成文件：" + cfgFile);
                // 读取配置,构造 Configuration 对象.
                // 如果不想使用配置文件的话,也可以直接来 new Configuration(),然后给相应属性赋值.
                File configFile = new File("src/main/resources/" + cfgFile);
                ConfigurationParser cp = new ConfigurationParser(warnings);
                Configuration config = cp.parseConfiguration(configFile);
                DefaultShellCallback callback = new DefaultShellCallback(overwrite);
                MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
                myBatisGenerator.generate(null);
                for (String warning : warnings) {
                    System.out.println(warning);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (XMLParserException e) {
            e.printStackTrace();
        }
    }

}