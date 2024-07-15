package org.clmx.redis.core.command.common;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

/**
 * DelCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/12 上午11:35
 */
public class DelCommand implements Command {
    @Override
    public String name() {
        // DEL ===> *4,$3,del,$1,a,$1,b,$1,c
        return "DEL";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String[] keys = getParams(args);
        return Reply.integer(cache.del(keys));
    }
}