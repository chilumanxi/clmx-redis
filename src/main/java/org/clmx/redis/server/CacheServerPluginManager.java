package org.clmx.redis.server;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CacheServerPluginManager class
 * 插件入口点类，负责在Spring应用启动和关闭时管理插件的生命周期
 *
 * @author chilumanxi
 * @date 2024/7/4 下午3:24
 */
@Component
public class CacheServerPluginManager implements ApplicationListener<ApplicationEvent> {

    // 自动注入所有实现了 ICacheServerPlugin 接口的插件
    @Autowired
    List<CacheServerPlugin> plugins;

    /**
     * 处理应用程序事件的方法。
     *
     * @param event 应用程序事件
     */
    @Override
    public void onApplicationEvent(@NonNull ApplicationEvent event) {
        // 如果事件是 ApplicationReadyEvent（应用启动完成事件）
        if (event instanceof ApplicationReadyEvent) {
            // 遍历所有插件，依次初始化并启动
            for (CacheServerPlugin plugin : plugins) {
                plugin.init();    // 初始化插件
                plugin.startup(); // 启动插件
            }
            // 如果事件是 ContextClosedEvent（应用上下文关闭事件）
        } else if (event instanceof ContextClosedEvent) {
            // 遍历所有插件，依次关闭
            for (CacheServerPlugin plugin : plugins) {
                plugin.shutdown(); // 关闭插件
            }
        }
    }
}
