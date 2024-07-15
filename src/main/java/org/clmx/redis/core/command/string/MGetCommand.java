package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

/**
 * MGetCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 下午7:00
 */
public class MGetCommand implements Command {
    @Override
    public String name() {
        // MGET ===> *4,$4,mget,$1,a,$1,b
        return "MGET";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String[] keys = getParams(args);
        return Reply.array(cache.mGet(keys));
    }
}