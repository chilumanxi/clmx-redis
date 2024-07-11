package org.clmx.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * SpringAsyncConfig class
 *
 * @author chilumanxi
 * @date 2024/7/10 下午4:57
 */
@Configuration
public class SpringAsyncConfig {
    @Bean("MyExecutor")
    public Executor asyncServiceExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //设核心线程
        threadPoolTaskExecutor.setCorePoolSize(10);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(100);
        //缓冲队列大小
        threadPoolTaskExecutor.setQueueCapacity(10);
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //等待时间
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        //拒绝策略走默认，不设置
        //线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("MyAsync-");
        // 初始化线程
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}