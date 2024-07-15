package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

import static org.clmx.redis.common.Constants.OK;

/**
 * SetNXCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 下午9:00
 */
public class SetNXCommand implements Command {
    @Override
    public String name() {
        // SETNX ===> *3,$3,setnx,$1,a,$1,1
        return "SETNX";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String key = getKey(args);
        String val = getValue(args);
        return Reply.integer(cache.setNx(key, val));
    }
}