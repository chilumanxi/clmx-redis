package org.clmx.redis.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * RESPDecoder class
 *  缓存解码器，用于将字节流解码为可识别的消息对象。
 *  此解码器负责从ByteBuf中读取数据，并将其转换为字符串形式添加到输出列表中。
 *
 * @author chilumanxi
 * @date 2024/7/4 下午4:37
 */
public class RESPDecoder extends ByteToMessageDecoder {

    AtomicLong counter = new AtomicLong();

    /**
     * 解码方法，Netty框架调用此方法进行实际的解码操作。
     *
     * @param ctx Netty的通道处理上下文，用于通道的管理和操作。
     * @param in                    输入的ByteBuf，包含待解码的数据。
     * @param out                   输出列表，解码后的消息对象将被添加到此列表中。
     * @throws Exception 如果解码过程中发生错误。
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("IMCacheDecoder decodeCount:" + counter.incrementAndGet());

        // 获取当前可读字节的长度和读取索引
        if (in.readableBytes() <= 0) {
            return;
        }
        int count = in.readableBytes();
        int index = in.readerIndex();
        System.out.println("IMCacheDecoder count:" + count + ", index:" + index);

        // 根据可读字节长度创建字节数组，并从ByteBuf中读取字节到字节数组
        byte[] bytes = new byte[count];
        in.readBytes(bytes);

        // 将字节数组转换为字符串
        String ret = new String(bytes);
        System.out.println("IMCacheDecoder ret:" + ret);

        if(!isRESP(ret)){
            ret = toRESP(ret);
        }

        // 将解码后的字符串添加到输出列表中
        out.add(ret);
    }

    private static String toRESP(String command) {
        String[] parts = command.split(" ");
        StringBuilder resp = new StringBuilder();

        // Start with the array indicator '*'
        resp.append("*").append(parts.length).append("\r\n");

        for (String part : parts) {
            // Append each part as a Bulk String
            resp.append("$").append(part.length()).append("\r\n");
            resp.append(part).append("\r\n");
        }
        System.out.println();
        return resp.toString();
    }

    //判断是否是RESP语言
    private static boolean isRESP(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        char firstChar = input.charAt(0);

        return switch (firstChar) {
            case '+', '-' -> // Simple Strings 和 Errors
                    true;
            case ':' -> // Integers
                    findIntegerEnd(input, 0) != -1;
            case '$' -> // Bulk Strings
                    isBulkString(input);
            case '*' -> // Arrays
                    isArray(input);
            default -> false;
        };
    }

    private static boolean isBulkString(String input) {
        // Bulk Strings 以'$'作为开始并跟着字符串的长度
        if (!input.startsWith("$")) {
            return false;
        }
        int newlineIndex = input.indexOf("\r\n");
        if (newlineIndex == -1) {
            return false;
        }
        String lengthStr = input.substring(1, newlineIndex);
        try {
            int length = Integer.parseInt(lengthStr);
            // 确保长度 >= 第一个\r\n结束的位置 (newlineIndex + 2) + 后面字符串的长度 + \r\n的长度
            return input.length() >= newlineIndex + 2 + length + 2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isArray(String input) {
        // Arrays start with '*' followed by the number of elements
        if (!input.startsWith("*")) {
            return false;
        }
        int newlineIndex = input.indexOf("\r\n");
        if (newlineIndex == -1) {
            return false;
        }
        String lengthStr = input.substring(1, newlineIndex);
        try {
            int length = Integer.parseInt(lengthStr);
            // Parse each element in the array
            int currentIndex = newlineIndex + 2;
            for (int i = 0; i < length; i++) {
                if (currentIndex >= input.length()) {
                    return false;
                }
                char elementType = input.charAt(currentIndex);
                switch (elementType) {
                    case '+': // Simple Strings
                    case '-': // Errors
                    case ':': // Integers
                        int integerEnd = findIntegerEnd(input, currentIndex);
                        if (integerEnd == -1) {
                            return false;
                        }
                        currentIndex = integerEnd;
                        break;
                    case '$': // Bulk Strings
                        int bulkEnd = findBulkStringEnd(input, currentIndex);
                        if (bulkEnd == -1) {
                            return false;
                        }
                        currentIndex = bulkEnd;
                        break;
                    case '*': // Arrays
                        int arrayEnd = findArrayEnd(input, currentIndex);
                        if (arrayEnd == -1) {
                            return false;
                        }
                        currentIndex = arrayEnd;
                        break;
                    default:
                        return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int findIntegerEnd(String input, int startIndex){
        int newlineIndex = input.indexOf("\r\n", startIndex);
        if(newlineIndex == -1) {
            return -1;
        }
        try{
            String integerStr = input.substring(startIndex + 1, newlineIndex);
            Integer.parseInt(integerStr);
            return newlineIndex + 2;
        }catch (Exception e){
            return -1;
        }
    }

    private static int findBulkStringEnd(String input, int startIndex) {
        int newlineIndex = input.indexOf("\r\n", startIndex);
        if (newlineIndex == -1) {
            return -1;
        }
        String lengthStr = input.substring(startIndex + 1, newlineIndex);
        try {
            int length = Integer.parseInt(lengthStr);
            int bulkEndIndex = newlineIndex + 2 + length + 2;
            if (bulkEndIndex > input.length() || !input.substring(newlineIndex + 2 + length, bulkEndIndex).equals("\r\n")) {
                return -1;
            }
            return bulkEndIndex;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int findArrayEnd(String input, int startIndex) {
        int newlineIndex = input.indexOf("\r\n", startIndex);
        if (newlineIndex == -1) {
            return -1;
        }
        String lengthStr = input.substring(startIndex + 1, newlineIndex);
        try {
            int length = Integer.parseInt(lengthStr);
            int currentIndex = newlineIndex + 2;
            for (int i = 0; i < length; i++) {
                if (currentIndex >= input.length()) {
                    return -1;
                }
                char elementType = input.charAt(currentIndex);
                switch (elementType) {
                    case '+': // Simple Strings
                    case '-': // Errors
                    case ':': // Integers
                        currentIndex = input.indexOf("\r\n", currentIndex) + 2;
                        if (currentIndex == 1) { // indexOf returns -1 if not found
                            return -1;
                        }
                        break;
                    case '$': // Bulk Strings
                        int bulkEnd = findBulkStringEnd(input, currentIndex);
                        if (bulkEnd == -1) {
                            return -1;
                        }
                        currentIndex = bulkEnd;
                        break;
                    case '*': // Arrays
                        int arrayEnd = findArrayEnd(input, currentIndex);
                        if (arrayEnd == -1) {
                            return -1;
                        }
                        currentIndex = arrayEnd;
                        break;
                    default:
                        return -1;
                }
            }
            return currentIndex;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) throws Exception{
        System.out.println(toRESP("set a 5"));
//        System.out.println(isRESP("+OK\r\n")); // Simple String
//        System.out.println(isRESP("-Error message\r\n")); // Error
//        System.out.println(isRESP(":1000\r\n")); // Integer
//        System.out.println(isRESP("$6\r\nfoobar\r\n")); // Bulk String
//        System.out.println(isRESP("*2\r\n$3\r\nfoo\r\n:1000\r\n")); // Array
//        System.out.println(isRESP("*3\r\n:1\r\n:2\r\n:3\r\n")); // Array with integers
//        System.out.println(isRESP("*2\r\n$3\r\nfoo\r\n*2\r\n$3\r\nbar\r\n$3\r\nbaz\r\n")); // Nested array
//        System.out.println(isRESP("invalid")); // Invalid


    }
}


