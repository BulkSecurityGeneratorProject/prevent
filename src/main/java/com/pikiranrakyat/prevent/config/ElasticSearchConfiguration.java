package com.pikiranrakyat.prevent.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticSearchConfiguration {

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client,Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) throws UnknownHostException {
//        String server = clusterNodes.split(":")[0];
//        Integer port = Integer.parseInt(clusterNodes.split(":")[1]);
//        Settings settings = Settings.settingsBuilder()
//            .put("cluster_name", clusterName).build();
//        client = TransportClient.builder().settings(settings).build()
//            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server), port));

//        client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getLocalHost(), 9300));
//        Client client = TransportClient.builder().settings(settings).build() .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
        return new ElasticsearchTemplate(client, new CustomEntityMapper(jackson2ObjectMapperBuilder.createXmlMapper(false).build()));
    }

    public class CustomEntityMapper implements EntityMapper {

        private ObjectMapper objectMapper;

        public CustomEntityMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }
    }
}
