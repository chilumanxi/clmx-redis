package org.clmx.redis.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.clmx.redis.core.CacheCommend;
import org.clmx.redis.core.command.Command;
import org.clmx.redis.core.command.Commands;
import org.clmx.redis.core.reply.Reply;

import java.nio.charset.StandardCharsets;

import static org.clmx.redis.common.Constants.*;


/**
 * CacheHandler class
 *
 * @author chilumanxi
 * @date 2024/7/4 下午6:32
 */
public class CacheHandler extends SimpleChannelInboundHandler<String> {

    // 全局缓存实例，用于存储和检索数据。
    public static final CacheCommend CACHE = new CacheCommend();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("Received message: " + msg);
        String[] args = msg.split(CRLF);
        System.out.println("IMCacheHandler ==> " + String.join(",", args));

        // 根据redis操作指令,获取具体的执行方法
        String cmd = args[2].toUpperCase();
        Command command = Commands.get(cmd);
        if (command != null) {
            try {
                Reply<?> reply = command.exec(CACHE, args);
                System.out.println("CMD[" + cmd + "] => " + reply.getType() + " => " + reply.getValue());
                replyContext(ctx, reply);
            } catch (Exception e) {
                Reply<?> reply = Reply.error("ERR exception with msg: '" + e.getMessage() + "'");
                replyContext(ctx, reply);
            }
        } else {
            Reply<?> reply = Reply.error("ERR unsupported command '" + cmd + "'");
            replyContext(ctx, reply);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void replyContext(ChannelHandlerContext ctx, Reply<?> reply) {
        switch (reply.getType()) {
            case INT:
                integer(ctx, (Integer) reply.getValue());
                break;
            case ERROR:
                error(ctx, (String) reply.getValue());
                break;
            case SIMPLE_STRING:
                simpleString(ctx, (String) reply.getValue());
                break;
            case BULK_STRING:
                bulkString(ctx, (String) reply.getValue());
                break;
            case ARRAY:
                array(ctx, (String[]) reply.getValue());
                break;
            default:
                simpleString(ctx, OK);
        }
    }


    // 发送错误响应
    private void error(ChannelHandlerContext ctx, String msg) {
        writeByteBuf(ctx, errorEncode(msg));
    }

    // 发送数组响应
    private void array(ChannelHandlerContext ctx, String[] array) {
        writeByteBuf(ctx, arrayEncode(array));
    }

    // 发送整数响应
    private void integer(ChannelHandlerContext ctx, int i) {
        writeByteBuf(ctx, integerEncode(i));
    }

    // 发送复杂的string响应
    private void bulkString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, bulkStringEncode(content));
    }

    // 发送简单的string响应
    private void simpleString(ChannelHandlerContext ctx, String content) {
        writeByteBuf(ctx, simpleStringEncode(content));
    }

    // 将数组编码为Redis协议格式的字符串
    private static String arrayEncode(Object[] array) {
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            sb.append("*-1").append(CRLF);
        } else if (array.length == 0) {
            sb.append("*0").append(CRLF);       // 空数组
        } else {
            sb.append("*").append(array.length).append(CRLF);
            for (Object obj : array) {
                if (obj == null) {
                    sb.append("$-1" + CRLF);
                } else {
                    if (obj instanceof Integer) {
                        sb.append(integerEncode((Integer) obj));
                    } else if (obj instanceof String) {
                        sb.append(bulkStringEncode(obj.toString()));
                    } else if (obj instanceof Object[] objs) {
                        sb.append(arrayEncode(objs));
                    }
                }
            }
        }
        return sb.toString();
    }

    // 将整数编码为Redis协议格式的字符串
    private static String integerEncode(int i) {
        return ":" + i + CRLF;
    }

    // 将错误信息编码为Redis协议格式的字符串
    private static String errorEncode(String error) {
        return "-" + error + CRLF;
    }

    // 将复杂的string编码为Redis协议格式的字符串
    private static String bulkStringEncode(String content) {
        String ret;
        if (content == null) {
            ret = "$-1";
        } else if (content.isEmpty()) { // 字符串空
            ret = "$0";
        } else {
            ret = "$" + content.getBytes().length + CRLF + content;
        }
        return ret + CRLF;
    }

    // 将简单的string编码为Redis协议格式的字符串
    private static String simpleStringEncode(String content) {
        String ret;
        if (content == null) {
            ret = "$-1";
        } else if (content.isEmpty()) { // 字符串空
            ret = "$0";
        } else {
            ret = "+" + content;
        }
        return ret + CRLF;
    }

    // 将编码后的字符串写入ByteBuf并发送
    private void writeByteBuf(ChannelHandlerContext ctx, String content) {
        System.out.println("wrap byte buffer and reply: " + content);
        ByteBuf buffer = Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buffer);
    }
}