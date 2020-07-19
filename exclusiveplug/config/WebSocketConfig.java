package com.qg.exclusiveplug.config;

import com.qg.exclusiveplug.interceptor.WebSocketInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author WilderGao
 * time 2018-09-30 23:19
 * motto : everything is no in vain
 * description
 */
@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    private int loss_connect_time = 0;
    @Autowired
    private WebSocketHandler handler;
    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/message").addInterceptors(webSocketInterceptor).setAllowedOrigins("*");
        registry.addHandler(handler, "/sockjs/message").addInterceptors(webSocketInterceptor).setAllowedOrigins("*").withSockJS();
    }

    /*@Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        StandardSessionFacade ssf = (StandardSessionFacade) request.getHttpSession();
        if(null != ssf) {
            User user = (User) ssf.getAttribute("user");
            sec.getUserProperties().put("user", user);
            log.info("获取到的User-->", user.toString());
        }
        super.modifyHandshake(sec, request, response);
    }*/

    /*@Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }*/
}
