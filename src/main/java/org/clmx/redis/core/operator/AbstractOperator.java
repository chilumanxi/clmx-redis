package org.clmx.redis.core.operator;

import org.clmx.redis.core.CacheEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractOperator class
 * 抽象操作类，定义公共行为
 *
 * @author chilumanxi
 * @date 2024/7/9 下午5:56
 */
public abstract class AbstractOperator {

    public static Map<String, CacheEntry<?>> map = new HashMap<>();

    protected Map<String, CacheEntry<?>> getMap() {
        return map;
    }

    protected CacheEntry<?> getCacheEntry(String key) {
        return map.get(key);
    }

    public CacheEntry<?> checkInvalid(String key) {
        CacheEntry<?> entry = getCacheEntry(key);
        if (entry == null || entry.getValue() == null) {
            return null;
        }
        long current = System.currentTimeMillis();
        // 如果key已过期,访问时删除
        if (entry.getTtl() > 0 && (current - entry.getTs()) > entry.getTtl()) {
            System.out.printf("KEY[%s] expire cause CURRENT[%d]-TS[%d] > TTL[%d] ms%n",
                    key, current, entry.getTs(), entry.getTtl());
            map.remove(key);
            return null;
        }
        return entry;
    }
}