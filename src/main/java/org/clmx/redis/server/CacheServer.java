package org.clmx.redis.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * CacheServer class
 *
 * @author chilumanxi
 * @date 2024/7/4 下午2:57
 */
@Component
public class CacheServer implements CacheServerPlugin {
    // 服务器端口号
    int port = 6379;
    // boss 线程组，用于接受客户端连接
    EventLoopGroup bossGroup;
    // worker 线程组，用于处理客户端IO操作
    EventLoopGroup workerGroup;
    // 服务器通道
    Channel channel;

    @Override
    public void init() {
        // 初始化 boss 线程组，指定线程工厂
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("redis-boss"));
        // 初始化 worker 线程组，指定线程工厂
        workerGroup = new NioEventLoopGroup(16, new DefaultThreadFactory("redis-work"));
    }

    @Override
    public void startup() {
        try {
            // 创建 ServerBootstrap 对象，用于配置服务器
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 128)                     // 连接队列大小
                    .childOption(ChannelOption.TCP_NODELAY, true)       // 关闭Nagle,即时传输
                    .childOption(ChannelOption.SO_KEEPALIVE, true)      // 支持长连接
                    .childOption(ChannelOption.SO_REUSEADDR, true)      // 共享端口
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)    // 操作缓冲区的大小
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)    // 发送缓冲区的大小
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            // 配置 ServerBootstrap
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //Netty 默认处理的是字节数据（ByteBuf），而不是字符串，需要添加String的解码器，不然将会接收到ByteBuf数据
                            ch.pipeline().addLast(new RESPDecoder(), new CacheHandler());
                        }
                    });

            channel = b.bind(port).sync().channel();
            System.out.println("开启netty redis服务器，端口为 " + port);
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(Objects.nonNull(bossGroup)) {
                bossGroup.shutdownGracefully();
            }
            if(Objects.nonNull(workerGroup)) {
                workerGroup.shutdownGracefully();
            }
        }
    }

    //关闭server
    @Override
    public void shutdown() {
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        if (this.bossGroup != null) {
            this.bossGroup.shutdownGracefully();
            this.bossGroup = null;
        }
        if (this.workerGroup != null) {
            this.workerGroup.shutdownGracefully();
            this.workerGroup = null;
        }
    }
}