package org.clmx.redis.core;

import org.clmx.redis.core.operator.CommonOperator;
import org.clmx.redis.core.operator.StringOperator;

/**
 * CacheCommend class
 *
 * @author chilumanxi
 * @date 2024/7/9 下午6:32
 */
public class CacheCommend {

    CommonOperator commonOperator = new CommonOperator();
    StringOperator stringOperator = new StringOperator();

    /**
     * -------------------------------------
     * Common
     * -------------------------------------
     */

    public int del(String... keys) {
        return commonOperator.del(keys);
    }


    public int exists(String... keys) {
        return commonOperator.exists(keys);
    }

    public int ttl(String key) {
        return commonOperator.ttl(key);
    }

    public int expire(String key, long ttl) {
        return commonOperator.expire(key, ttl);
    }

    /**
     * -------------------------------------
     * String
     * -------------------------------------
     */


    public String get(String key) { // get key value
        return stringOperator.get(key);
    }

    public void set(String key, String value) { //set key value
        stringOperator.set(key, value);
    }

    public int setNx(String key, String value) { //set key value
        return stringOperator.setNx(key, value);
    }

    public void setEx(String key, String value, long ttl) { //set key value
        stringOperator.setEx(key, value, ttl);
    }

    public String[] mGet(String... keys) {
        return stringOperator.mGet(keys);
    }

    public void mSet(String[] keys, String[] args) {
       stringOperator.mSet(keys, args);
    }

    public int incr(String key) {
        return stringOperator.incr(key);
    }

    public int decr(String key) {
        return stringOperator.decr(key);
    }





}