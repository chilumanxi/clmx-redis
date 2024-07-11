package org.clmx.redis.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CacheEntry class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午5:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheEntry<T> {
    private T value;    //存储的值
    private long ts;    //存储开始时间
    private long ttl;   //存活时间

    public final static long DEFAULT_TTL = -1000L;

    public CacheEntry(T v) {
        value = v;
        ts = System.currentTimeMillis();    // created timestamp
        ttl = DEFAULT_TTL;                  // default alive ttl
    }
}