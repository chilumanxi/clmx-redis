package org.clmx.redis.core.reply;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reply class
 * 对不同数据类型的适配
 *
 * @author chilumanxi
 * @date 2024/7/9 下午8:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reply<T> {
    T value;
    ReplyType type;

    public static Reply<String> string(String value) {
        return new Reply<>(value, ReplyType.SIMPLE_STRING);
    }

    public static Reply<String> bulkString(String value) {
        return new Reply<>(value, ReplyType.BULK_STRING);
    }

    public static Reply<Integer> integer(Integer value) {
        return new Reply<>(value, ReplyType.INT);
    }

    public static Reply<String> error(String value) {
        return new Reply<>(value, ReplyType.ERROR);
    }

    public static Reply<String[]> array(String[] value) {
        return new Reply<>(value, ReplyType.ARRAY);
    }
}