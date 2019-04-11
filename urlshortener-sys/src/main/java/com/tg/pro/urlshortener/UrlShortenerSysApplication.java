package com.tg.pro.urlshortener;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tg.pro.urlshortener")
@MapperScan(basePackages = "com.tg.pro.urlshortener.mapper*")
public class UrlShortenerSysApplication {
    public static void main(String[] args){
        SpringApplication.run(UrlShortenerSysApplication.class,args);
    }
}
