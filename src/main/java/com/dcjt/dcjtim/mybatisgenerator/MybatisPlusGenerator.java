package com.dcjt.dcjtim.mybatisgenerator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <p>
 * mybatis-plus 代码生成器
 * </p>
 */
public class MybatisPlusGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {

        String basePath = System.getProperty("user.dir") + File.separator;
        String javaPath = basePath + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String resourcePath = basePath + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        String xmlPath = resourcePath + File.separator + "mapper" + File.separator;
        String packgesPath = "com.dcjt.dcjtim";

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(javaPath);
        gc.setEnableCache(false);
        gc.setAuthor("滔哥");
        gc.setFileOverride(true);
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://192.168.3.22:3306/msgdb?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8&tinyInt1isBit=false");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("dcjt");
        dsc.setPassword("dcjt@123");
        mpg.setDataSource(dsc);

        // 包路径配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setParent(packgesPath);
        //默认路径分别为：web、entity、mapper、service、service.impl
        pc.setController("controller").setEntity("entity").setXml("mapper").setMapper("repository").setService("service").setServiceImpl(
                "service.impl");

        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // xml模板文件路径。模板文件的路径在mybatis-plus-generate.jar包下template目录下。
        // 如果模板引擎是 freemarker
        /*  String templatePath = "/templates/mapper.xml.ftl";*/
        // 如果模板引擎是 velocity
        /*String templatePath = "/templates/mapper.xml.vm";*/

        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义xml文件名称和生成路径
                return xmlPath + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel).setColumnNaming(NamingStrategy.underline_to_camel);//驼峰下划线转换
        //strategy.setSuperEntityClass("com.taoge.vertxspring.utils.entity.BaseEntity");// 设置实体继承的超类
        strategy.setEntityLombokModel(false);
        //strategy.setSuperControllerClass("com.taoge.vertxspring.utils.entity.BaseController");
        //strategy.setInclude(scanner("表名"));
        strategy.setInclude("chat_sole","chat_room"); // 需要生成的表，可以多表 string...
        //strategy.setSuperEntityColumns("id");
        //strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);//驼峰转连字符
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
