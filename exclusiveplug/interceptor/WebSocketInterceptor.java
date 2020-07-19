package com.qg.exclusiveplug.interceptor;

import com.qg.exclusiveplug.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author WilderGao
 * time 2018-09-30 23:23
 * motto : everything is no in vain
 * description
 */
@Service
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Resource
    private RedisTemplate<String, User> redisTemplate;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) {
        log.info("webSocket握手请求...");
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
            String token = servletRequest.getServletRequest().getParameter("x-auth-token");
            log.info("websocket连接令牌" + token);

            if (null != token && !token.equals("")) {
                User user = (User) redisTemplate.opsForHash().get("spring:session:sessions:" + token, "sessionAttr:user");

                if (null != user) {
                    log.info("user用户：" + user);
                    map.put(serverHttpRequest.getRemoteAddress().toString(), token);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
        log.info("webSocket握手结束...");
    }
}
