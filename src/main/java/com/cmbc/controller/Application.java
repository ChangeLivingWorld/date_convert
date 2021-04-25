package com.cmbc.controller;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"com.cmbc"})
@EnableAspectJAutoProxy
@EnableAsync
@EnableCaching
@EnableScheduling
public class Application {
    public static void main(String[] args) {

        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        SpringApplication.run(Application.class, args);

    }
}
