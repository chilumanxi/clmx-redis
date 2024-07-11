package org.clmx.redis.server;

import org.springframework.scheduling.annotation.Async;

/**
 * ICacheServerPlugin interface
 * 插件接口的用途，即定义Netty服务器的生命周期
 *
 * @author chilumanxi
 * @date 2024/7/4 下午2:56
 */
public interface CacheServerPlugin {

    void init();      // 初始化netty

    @Async("MyExecutor")
    void startup();   // 启动netty
    void shutdown();  // 关停netty
}