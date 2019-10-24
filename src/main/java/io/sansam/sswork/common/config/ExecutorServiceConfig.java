package io.sansam.sswork.common.config;

import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置对象
 */
@Slf4j
@Configuration
public class ExecutorServiceConfig {

    @Bean(name = "payOrderDelayQueueExecutor")
    public Executor payOrderDelayQueueExecutor() {
        int core = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(core, core*2,
                10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(),
                ThreadFactoryBuilder.create().setNameFormat("PayOrder-DelayQueue-Thread").get());
    }

}
