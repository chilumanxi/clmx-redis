package org.clmx.redis.core.command.common;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.reply.Reply;

import static org.clmx.redis.common.Constants.OK;

/**
 * CmdCommand class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午10:14
 */
public class CmdCommand implements Command {
    @Override
    public String name() {
        // *2,$7,COMMAND,$4,DOCS
        return "COMMAND";
    }

    @Override
    public Reply<?> exec(CacheCommend cache, String[] args) {
        return Reply.string(OK);
    }
}