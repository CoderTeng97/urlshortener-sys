package com.tg.pro.urlshortener.config;


import com.tg.pro.urlshortener.pojo.vo.UrlBaseVo;
import com.tg.pro.urlshortener.service.UrlManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootConfiguration
public class BootConfig {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UrlManagerService urlManagerService;

    @Value("${url.redis.key.pre}")
    private String urlPre;
    @Value("${url.time.task.period}")
    private String period;
    @Value("${url.time.task.initialDelay}")
    private String initialDelay;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public void UrlModifieldTask() {
        logger.info("开始启动配置");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.info("定时任务开始：" + LocalDateTime.now() + "--------------");
                List<UrlBaseVo> urlBaseVos = new ArrayList<>();
                Set set = redisTemplate.keys(urlPre + "*");
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    UrlBaseVo urlBaseVo = (UrlBaseVo) redisTemplate.opsForValue().get(iterator.next());
                    try {
                        urlManagerService.modifieldUrlViewInfo(urlBaseVo);
                        //延迟一秒执行 避免频繁更新
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        logger.error("定时更新任务 失败记录 ：" + urlBaseVo.toString());
                    }
                }
                logger.info("定时任务结束：" + LocalDateTime.now() + "--------------");
            }
        };
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, Long.valueOf(initialDelay), Long.valueOf(period), TimeUnit.MINUTES);
    }

}
