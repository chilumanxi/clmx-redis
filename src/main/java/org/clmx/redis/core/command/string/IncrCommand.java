package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

/**
 * IncrCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 下午9:25
 */
public class IncrCommand implements Command {
    @Override
    public String name() {
        // incr ===>  *2,$4,incr,$1,a
        return "INCR";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String key = getKey(args);
        return Reply.integer(cache.incr(key));
    }
}