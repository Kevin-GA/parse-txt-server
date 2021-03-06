package com.newtranx.cloud.edit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;


@Configuration
@EnableReactiveElasticsearchRepositories
public class ReactiveElasticsearchConfig extends AbstractReactiveElasticsearchConfiguration {
    @Override
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("101.200.183.167:9200")
                .build();
        return ReactiveRestClients.create(clientConfiguration);
    }
}

