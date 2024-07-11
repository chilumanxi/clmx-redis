package org.clmx.redis.core;

/**
 * CommonOperator class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午6:15
 */
public class CommonOperator extends AbstractOperator {

    public String get(String key) {
        if (checkInvalid(key)) {
            return null;
        }
        CacheEntry<String> cacheEntry = (CacheEntry<String>) map.get(key);
        if (cacheEntry == null) {
            return null;
        }
        return cacheEntry.getValue();
    }

    public void set(String key, String value) {
        map.put(key, new CacheEntry<>(value));
    }
}