package com.personal.quasar.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return factory -> {
            factory.addAdditionalTomcatConnectors(createHttpToHttpsRedirectConnector());
        };
    }

    private Connector createHttpToHttpsRedirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8088); // HTTP port
        connector.setSecure(false);
        connector.setRedirectPort(8443); // HTTPS port (default for Spring Boot SSL)
        return connector;
    }
}