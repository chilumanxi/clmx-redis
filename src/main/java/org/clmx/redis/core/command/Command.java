package org.clmx.redis.core.command;

import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.reply.Reply;

/**
 * Commend class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午6:44
 */
public interface Command {
    String name();

    Reply<?> exec(CacheCommend cache, String[] args);

    // add default args operator
    default String getKey(String[] args) {
        return args[4];
    }

    default String getValue(String[] args) {
        return args[6];
    }

    default String[] getParams(String[] args) {
        int len = (args.length - 3) / 2;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[4 + i * 2];
        }
        return keys;
    }

    default String[] getParamsNoKey(String[] args) {
        int len = (args.length - 5) / 2;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[6 + i * 2];
        }
        return keys;
    }

    default String[] getKeys(String[] args) {
        int len = (args.length - 3) / 4;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[4 + i * 4];
        }
        return keys;
    }

    default String[] getValues(String[] args) {
        int len = (args.length - 3) / 4;
        String[] vals = new String[len];
        for (int i = 0; i < len; i++) {
            vals[i] = args[6 + i * 4];
        }
        return vals;
    }


    default String[] getHValues(String[] args) {
        int len = (args.length - 5) / 4;
        String[] vals = new String[len];
        for (int i = 0; i < len; i++) {
            vals[i] = args[8 + i * 4];
        }
        return vals;
    }

    default String[] getHKeys(String[] args) {
        int len = (args.length - 5) / 4;
        String[] keys = new String[len];
        for (int i = 0; i < len; i++) {
            keys[i] = args[6 + i * 4];
        }
        return keys;
    }
}