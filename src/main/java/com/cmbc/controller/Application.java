package com.cmbc.controller;

import com.alibaba.fastjson.parser.ParserConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"com.wwtx.chinesemedicine.service", "com.wwtx.chinesemedicine.api"})
@EnableAspectJAutoProxy
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableElasticsearchRepositories(basePackages = "com.wwtx.chinesemedicine.service.dao.es")
public class Application {
    public static void main(String[] args) {

        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        SpringApplication.run(ChinesemedicineApiApplication.class, args);

    }
}
