package org.clmx.redis.core.command;

import org.clmx.redis.core.command.common.CmdCommand;
import org.clmx.redis.core.command.common.DelCommand;
import org.clmx.redis.core.command.common.ExpireCommand;
import org.clmx.redis.core.command.common.TTLCommand;
import org.clmx.redis.core.command.string.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Commands class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午8:14
 */
public class Commands {
    private static final Map<String, Command> ALL = new LinkedHashMap<>();

    static {
        register(new CmdCommand());
        register(new DelCommand());
        register(new TTLCommand());
        register(new ExpireCommand());


        register(new SetCommand());
        register(new SetEXCommand());
        register(new SetNXCommand());
        register(new GetCommand());
        register(new MGetCommand());
        register(new MSetCommand());
        register(new IncrCommand());
        register(new DecrCommand());
    }


    public static void register(Command command) {
        ALL.put(command.name(), command);
    }

    public static Command get(String name) {
        return ALL.get(name);
    }



}