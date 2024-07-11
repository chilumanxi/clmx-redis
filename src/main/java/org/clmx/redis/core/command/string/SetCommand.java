package org.clmx.redis.core.command.string;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

import static org.clmx.redis.common.Constants.OK;

/**
 * SetCommand class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午8:19
 */
public class SetCommand implements Command {
    @Override
    public String name() {
        // SET ===> *3,$3,set,$1,a,$1,1
        return "SET";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        String key = getKey(args);
        String val = getValue(args);
        cache.set(key, val);
        return Reply.string(OK);
    }
}