package com.dbwork.springboot.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

//代码生成器
public class CodeGenerator {

    public static void main(String[] args) {
        String tablename = "sys_calendar";
        generate(tablename);
    }

    private static void generate(String tablename) {
        FastAutoGenerator.create(
                        "jdbc:mysql://cd-cdb-rxg8j3pm.sql.tencentcdb.com:63931/lwdDB?serverTimezone=GMT%2b8&useSSL=false&useUnicode=true&characterEncoding=utf-8",
                        "root",
                        "2020141460Yzwx")
                .globalConfig(builder -> {
                    builder.author("Mrlwd") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\Users\\Admin\\Desktop\\lwdSys\\acholeSys\\backend\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.dbwork.springboot") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(
                                    Collections.singletonMap(OutputFile.mapperXml,
                                            "C:\\Users\\Admin\\Desktop\\lwdSys\\acholeSys\\backend\\src\\main\\java\\com\\dbwork\\springboot\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok(); //开始Lombok
                    builder.controllerBuilder().enableHyphenStyle(); //开启驼峰连字符
                    builder.controllerBuilder().enableRestStyle();//开启@restController控制器
                    builder.addInclude(tablename) // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_", "good_"); // 设置过滤表前缀
                })
                // .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
