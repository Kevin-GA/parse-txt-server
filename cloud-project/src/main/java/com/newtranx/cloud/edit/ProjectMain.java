package com.newtranx.cloud.edit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

//@EnableElasticsearchRepositories(basePackages = "com.test.els.demo.core.user.repository")
@EnableDiscoveryClient
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
public class ProjectMain {
    public static void main(String[] args) {
        SpringApplication.run(ProjectMain.class, args);
    }
}
