package com.pobluesky.global.config;


import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EcsConfig {

    @Value("${spring.application.name}")
    private String appName;  // spring.application.name 값 주입

    @Value("${server.port}")
    private int serverPort;  // server.port 값 주입

    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
        EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        config.setIpAddress(ip);
        config.setPreferIpAddress(true);
        config.setNonSecurePort(serverPort);

        // 서비스 이름 설정
        config.setAppname(appName);
        config.setVirtualHostName(appName);
        config.setSecureVirtualHostName(appName);

        return config;
    }
}