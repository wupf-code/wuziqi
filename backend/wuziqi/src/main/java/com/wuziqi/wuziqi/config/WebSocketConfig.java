package com.wuziqi.wuziqi.config;

/**
 * @author: 武鹏飞
 * @user:ASUS
 * @date:2022/8/25 - 20:23
 * @projectName:wuziqi
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}


