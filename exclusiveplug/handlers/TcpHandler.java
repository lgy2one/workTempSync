package com.qg.exclusiveplug.handlers;

import com.qg.exclusiveplug.map.NettyChannelHolder;
import com.qg.exclusiveplug.service.TcpService;
import com.qg.exclusiveplug.util.FormatMatchingUtil;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WilderGao
 * time 2018-09-20 16:43
 * motto : everything is no in vain
 * description
 */
@Component
@Qualifier("serverHandler")
@Slf4j
@ChannelHandler.Sharable
public class TcpHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 是否已存储端口号对应的连接
     */
    private boolean hasRead = false;

    /**
     * 心跳检测次数
     */
    private int loss_connect_time = 0;
    private static final String PONG_MSG = "2";

    @Autowired
    private TcpService tcpService;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) {
        log.info("客户端请求，Id为： " + channelHandlerContext.channel().id());
        log.info("收到客户端发来的消息: " + message);

        sendPongMsg(channelHandlerContext);

        // 若是符合条件的信息则进入service层处理
        if(FormatMatchingUtil.isServiceInfo(message)){
            tcpService.messageHandler(message);
        } else {
            if(FormatMatchingUtil.isDeviceIndexs(message)){
                String[] list = message.split(":");
                for (String index : list) {
                    Integer deviceIndex = Integer.valueOf(index);
                    if(NettyChannelHolder.containsKey(deviceIndex)) {
                        log.info("NettyChannelHolder已包含该端口-->{}", deviceIndex);
                        List<ChannelHandlerContext> channelHandlerContextList = NettyChannelHolder.get(deviceIndex);

                        if(!channelHandlerContextList.contains(channelHandlerContext)) {
                            channelHandlerContextList.add(channelHandlerContext);
                            log.info("NettyChannelHolder新增端口-->{}", deviceIndex);
                        }
                    } else {
                        log.info("NettyChannelHolder端口已存在-->{}", deviceIndex);

                        List<ChannelHandlerContext> channelHandlerContextList = new ArrayList<>();
                        channelHandlerContextList.add(channelHandlerContext);
                        NettyChannelHolder.put(deviceIndex, channelHandlerContextList);
                    }
                }
            } else {
                log.info("数据格式错误");
                channelHandlerContext.close();
            }
        }

        channelHandlerContext.channel().flush();
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        ChannelFuture channelFuture = context.writeAndFlush(PONG_MSG);
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                log.info("发送PONG信息成功：" + context.channel().remoteAddress());
            } else {
                log.error("发送PONG信息失败：" + context.channel().remoteAddress());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Throwable t = cause.getCause();
        if (t instanceof ReadTimeoutException) {
            log.info("read time out");
        } else if (t instanceof WriteTimeoutException) {
            log.info("write time out");
        }
        log.info("连接出现了异常 ... ");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannelHolder.removeChannel(ctx);
        log.info("断开连接 ... ");
        super.channelInactive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("读写超时，检测心跳 >> {}", ctx.channel().remoteAddress());
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            log.info("检查通道连接状态 >> {}", ctx.channel().remoteAddress());
            loss_connect_time++;
            System.out.println(loss_connect_time);
            if(loss_connect_time > 2){
                loss_connect_time = 0;
                log.info("关闭不活动的连接 >> {}", ctx.channel().remoteAddress());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("已经连接上了 : " + ctx.channel().remoteAddress() + ":" + ctx.channel().id());
        ctx.fireChannelActive();
    }

    public void send(Integer deviceIndex, String message) {
        List<ChannelHandlerContext> channelHandlerContextList = NettyChannelHolder.get(deviceIndex);
        if (null != channelHandlerContextList && !channelHandlerContextList.isEmpty()) {
            for(ChannelHandlerContext ctx : channelHandlerContextList) {
                System.out.println("asdasdasd" + ctx);
                ctx.writeAndFlush(message);
            }
        }
    }

}
