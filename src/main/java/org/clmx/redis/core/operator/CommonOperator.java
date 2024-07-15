package org.clmx.redis.core.operator;

import org.clmx.redis.core.CacheEntry;

import java.util.Arrays;
import java.util.Objects;

/**
 * CommonOperator class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午6:15
 */
public class CommonOperator extends AbstractOperator {

    public int del(String[] keys) {
        return Objects.isNull(keys) ? 0 : (int) Arrays.stream(keys).map(map::remove).filter(Objects::nonNull).count();
    }

    public int exists(String[] keys) {
        return Objects.isNull(keys) ? 0 : (int) Arrays.stream(keys).map(map::containsKey).filter(x -> x).count();

    }

    public int ttl(String key) {
        CacheEntry<?> entry = checkInvalid(key);
        if (Objects.isNull(entry)) {    // key 不存在时返回-2
            return -2;
        }
        if (entry.getTtl() == CacheEntry.DEFAULT_TTL){  // key 没有过期时间时返回-1
            return -1;
        }

        long current = System.currentTimeMillis();
        long ret = entry.getTs() + entry.getTtl() - current;
        if (ret > 0){   //返回当前还剩的过期时间
            return (int) Math.ceil(ret / 1000.0);
        }
        return -1;
    }

    public int expire(String key, long ttl) {
        CacheEntry<?> entry = checkInvalid(key);
        if (Objects.isNull(entry)) {
            return 0;
        }
        entry.setTtl(ttl * 1000L);
        entry.setTs(System.currentTimeMillis());
        return 1;
    }
}