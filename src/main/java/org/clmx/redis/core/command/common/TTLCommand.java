package org.clmx.redis.core.command.common;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

/**
 * TTLCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 上午10:23
 */
public class TTLCommand implements Command {
    @Override
    public String name() {
        // ttl ===>  *2,$3,ttl,$1,a
        return "TTL";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.ttl(key));
    }
}