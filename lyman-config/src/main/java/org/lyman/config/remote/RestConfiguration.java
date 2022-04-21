package org.lyman.config.remote;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Conditional(RestCondition.class)
@Configuration
@PropertySource(value = "classpath:application.yml")
public class RestConfiguration {

    @Bean
    @Autowired
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        RestTemplate template = new RestTemplate();
        template.setRequestFactory(factory);
        return template;
    }

    @Bean
    @Autowired
    @ConfigurationProperties(prefix = "rest.request-factory")
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClientBuilder builder) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(builder.build());
        return factory;
    }

    @Bean
    @Autowired
    public HttpClientBuilder httpClientBuilder(HttpClientConnectionManager manager) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setConnectionManager(manager);
        return builder;
    }

    @Bean
    @ConfigurationProperties(prefix = "rest.connection-manager")
    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        return manager;
    }

}
