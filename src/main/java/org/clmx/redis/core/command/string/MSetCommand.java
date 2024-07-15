package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

import static org.clmx.redis.common.Constants.OK;

/**
 * MSetCommand class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 下午8:19
 */
public class MSetCommand implements Command {
    @Override
    public String name() {
        // MSET ===> *7,$4,mset,$1,a,$1,1,$1,b,$1,2,$1,c,$1,3
        return "MSET";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String[] keys = getKeys(args);
        String[] values = getValues(args);
        cache.mSet(keys, values);
        return Reply.string(OK);
    }
}