package com.summer.netty;

import com.summer.config.WebSocketConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/24 10:50
 **/
public class NettyServer {

    public void start(WebSocketConfig webSocketConfig) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            // 绑定线程池
            sb.group(group, bossGroup)
                    // 指定使用的channel
                    .channel(NioServerSocketChannel.class)
                    // 绑定监听端口
                    .localAddress(webSocketConfig.getPort())
                    // 绑定客户端连接时候触发操作
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //System.out.println("收到新连接");
                            //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            //以块的方式来写的处理器
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
                            ch.pipeline().addLast(new ClientWebSocketHandler());
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler(webSocketConfig.getAdminwsPath(), null, true, 65536 * 10));
                            if (webSocketConfig.isSsl()) {
                                // 以下为要支持wss所需处理
                                KeyStore ks = KeyStore.getInstance("JKS");
                                InputStream ksInputStream = new FileInputStream(webSocketConfig.getKeyStore());
                                ks.load(ksInputStream, webSocketConfig.getKeyPassword().toCharArray());
                                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                                kmf.init(ks, webSocketConfig.getKeyPassword().toCharArray());
                                SSLContext sslContext = SSLContext.getInstance("TLS");
                                sslContext.init(kmf.getKeyManagers(), null, null);
                                SSLEngine sslEngine = sslContext.createSSLEngine();
                                sslEngine.setUseClientMode(false);
                                sslEngine.setNeedClientAuth(false);
                                // 需把SslHandler添加在第一位
                                ch.pipeline().addFirst("ssl", new SslHandler(sslEngine));
                            }
                        }
                    });
            // 服务器异步创建绑定
            ChannelFuture cf = sb.bind().sync();
            System.out.println(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
            // 关闭服务器通道
            cf.channel().closeFuture().sync();
        } finally {
            // 释放线程池资源
            group.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }
}
