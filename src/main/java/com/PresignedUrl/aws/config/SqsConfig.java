package com.PresignedUrl.aws.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;


@Configuration
public class SqsConfig {

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(SqsAsyncClient sqsAsyncClient)
    {
        return SqsMessageListenerContainerFactory
                .builder()
                .configure(options -> options
                        .maxMessagesPerPoll(10)       // Pull 10 messages per AWS HTTP request
                        .maxConcurrentMessages(13)    // Spin up 13 parallel threads
                )
                .sqsAsyncClient(sqsAsyncClient)
                .build();

    }
}
