package org.clmx.redis.core;

/**
 * CacheCommend class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午6:32
 */
public class CacheCommend {

    CommonOperator testOperator = new CommonOperator();

    public String get(String key) {
        return testOperator.get(key);
    }

    public void set(String key, String value) {
        testOperator.set(key, value);
    }
}