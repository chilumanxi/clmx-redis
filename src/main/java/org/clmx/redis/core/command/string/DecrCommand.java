package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

/**
 * DecrCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 下午9:27
 */
public class DecrCommand implements Command {
    @Override
    public String name() {
        // decr ===>  *2,$4,incr,$1,a
        return "DECR";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.decr(key));
    }
}