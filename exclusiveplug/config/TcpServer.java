package com.qg.exclusiveplug.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * @author WilderGao
 * time 2018-09-20 16:12
 * motto : everything is no in vain
 * description TCP配置文件
 */
@Component
@Slf4j
@Data
public class TcpServer {
    @Autowired
    @Qualifier("serverBootstrap")
    private ServerBootstrap serverBootstrap;

    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress tcpPort;

    private ChannelFuture channelFuture;

    /**
     * 开启Netty
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        log.info("TCP 连接开启，端口号为: " + tcpPort + " ... ");
        channelFuture = serverBootstrap.bind(tcpPort).sync();
    }

    /**
     * 关闭 Netty
     *
     * @throws InterruptedException
     */
    @PreDestroy
    public void stop() throws InterruptedException {
        log.info("TCP 连接断开 ... ");
        channelFuture.channel().closeFuture().sync();
    }


}
