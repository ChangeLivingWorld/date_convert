package com.cmbc.controller;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(scanBasePackages = {"com.cmbc"},exclude = {DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {

        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        SpringApplication.run(Application.class, args);

    }
}
