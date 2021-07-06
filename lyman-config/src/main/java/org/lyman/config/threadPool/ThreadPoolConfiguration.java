package org.lyman.config.threadPool;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@PropertySource("classpath:application.yml")
public class ThreadPoolConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "thread-pool.excutor")
    public ThreadPoolTaskExecutor excutorThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        return executor;
    }

    @Bean
    @ConfigurationProperties(prefix = "thread-pool.cache")
    public ThreadPoolTaskExecutor cacheThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        return executor;
    }

    @Bean
    @ConfigurationProperties(prefix = "thread-pool.scheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        return scheduler;
    }

}
