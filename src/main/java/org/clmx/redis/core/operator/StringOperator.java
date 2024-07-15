package org.clmx.redis.core.operator;

import org.clmx.redis.core.CacheEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * StringOperator class
 *
 * @author zhanghaoran25
 * @date 2024/7/15 下午1:52
 */
public class StringOperator extends AbstractOperator{

    public String get(String key) {
        CacheEntry<String> cacheEntry = (CacheEntry<String>) checkInvalid(key);
        if (Objects.isNull(cacheEntry)) {
            return null;
        }
        return cacheEntry.getValue();
    }

    public void set(String key, String value) {
        map.put(key, new CacheEntry<>(value));
    }

    public int setNx(String key, String value) {
        CacheEntry<String> cacheEntry = (CacheEntry<String>) checkInvalid(key);
        if (Objects.isNull(cacheEntry)) {
            map.put(key, new CacheEntry<>(value));
            return 1;
        }
        return 0;
    }

    public void setEx(String key, String value, long ttl) {
        map.put(key, new CacheEntry<>(value));
        CommonOperator commonOperator = new CommonOperator();
        commonOperator.expire(key, ttl);
    }

    public String[] mGet(String... keys) {
        if(Objects.isNull(keys) || keys.length == 0){
            return new String[0];
        }
        List<String> result = new ArrayList<>();
        for (String key : keys) {
            CacheEntry<String> cacheEntry = (CacheEntry<String>) checkInvalid(key);
            if(Objects.isNull(cacheEntry)){
                result.add(null);
            }else{
                result.add(cacheEntry.getValue());
            }
        }
        return result.toArray(new String[0]);
    }

    public void mSet(String[] keys, String[] args) {
        if(Objects.isNull(keys) || keys.length == 0){
            return ;
        }
        if(Objects.isNull(args) || keys.length != args.length){
            return ;
        }
        for(int i = 0; i < keys.length; i ++){
            set(keys[i], args[i]);
        }
    }


    public int incr(String key) {
        CacheEntry<String> cacheEntry = (CacheEntry<String>) checkInvalid(key);
        if(Objects.isNull(cacheEntry)){
            set(key, "1");
            return 1;
        }
        try {
            int num = Integer.parseInt(cacheEntry.getValue());
            num ++;
            set(key, String.valueOf(num));
            return num;
        }catch (NumberFormatException nfe){
            throw new RuntimeException("ERR value is not an integer or out of range");
        }
    }

    public int decr(String key) {
        CacheEntry<String> cacheEntry = (CacheEntry<String>) checkInvalid(key);
        if(Objects.isNull(cacheEntry)){
            set(key, "-1");
            return -1;
        }
        try {
            int num = Integer.parseInt(cacheEntry.getValue());
            num --;
            set(key, String.valueOf(num));
            return num;
        }catch (NumberFormatException nfe){
            throw new RuntimeException("ERR value is not an integer or out of range");
        }
    }
}