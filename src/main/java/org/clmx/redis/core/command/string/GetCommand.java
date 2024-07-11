package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

/**
 * GetCommand class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午8:20
 */
public class GetCommand implements Command {
    @Override
    public String name() {
        // GET ===> *2,$3,get,$1,a
        return "GET";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String key = getKey(args);
        return Reply.string(cache.get(key));
    }
}