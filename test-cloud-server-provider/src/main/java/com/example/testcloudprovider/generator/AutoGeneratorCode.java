package com.example.testcloudprovider.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * Created by czh on 2020/7/8 13:42
 *
 * @author chengzhenhua
 */
public class AutoGeneratorCode {

    public static void main(String[] args) {
        // 全局包名
        String packageName = "com.example.testcloudprovider";
        // service生产策略。例如：user -> UserService, 设置成true: user -> IUserService
        boolean serviceNameStartWithI = true;
        generateByTables(serviceNameStartWithI, packageName, "payment_order");
    }

    private static void generateByTables(boolean serviceNameStartWithI, String packageName, String... tableNames) {

        GlobalConfig config = new GlobalConfig();

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl("jdbc:mysql://127.0.0.1:3306/test_cloud?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai")
                .setUsername("root")
                .setPassword("1234")
                .setDriverName("com.mysql.cj.jdbc.Driver");
//        dataSourceConfig.setDbType(DbType.SQL_SERVER)
//                .setUrl("jdbc:sqlserver://47.99.60.88:1455;database=wangxiao")
//                .setUsername("sa")
//                .setPassword("P@ssw0rdSA")
//                .setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(true)
                //.setTablePrefix("tp")
                .setNaming(NamingStrategy.underline_to_camel)
                // 修改替换成你需要的表名，多个表名传数组
                .setInclude(tableNames);

        config.setActiveRecord(false)
//                .setBaseResultMap(true)
//                .setBaseColumnList(true)
                .setAuthor("yyz")
                .setOutputDir("/data")
                .setIdType(IdType.INPUT)
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setService("service")
                                .setServiceImpl("service.impl")
                                .setMapper("mapper")
                                .setXml("mapper.xml")
                                .setController("controller")
                                .setEntity("entity")).execute();
    }
}
