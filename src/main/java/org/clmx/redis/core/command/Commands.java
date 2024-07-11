package org.clmx.redis.core.command;

import org.clmx.redis.core.command.common.CmdCommand;
import org.clmx.redis.core.command.string.GetCommand;
import org.clmx.redis.core.command.string.SetCommand;

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
        register(new SetCommand());
        register(new GetCommand());
        register(new CmdCommand());
    }


    public static void register(Command command) {
        ALL.put(command.name(), command);
    }

    public static Command get(String name) {
        return ALL.get(name);
    }



}