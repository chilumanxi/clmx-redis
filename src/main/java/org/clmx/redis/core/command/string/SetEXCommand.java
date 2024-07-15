package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

import static org.clmx.redis.common.Constants.OK;

/**
 * SetEXCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 下午9:08
 */
public class SetEXCommand implements Command {
    @Override
    public String name() {
        // SETEX ===> *4,$3,setnx,$1,a,$1,1,$10
        return "SETEX";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String key = getKey(args);
        String value = getValue(args);
        long ttl = Long.parseLong(getEXTtl(args));
        cache.setEx(key, value, ttl);
        return Reply.string(OK);
    }
}