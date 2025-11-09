package com.amcom.desafio_tecnico_amcom.config.kafka;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, OrderEvent> consumerFactory,
            KafkaTemplate<String, OrderEvent> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (r, e) -> new org.apache.kafka.common.TopicPartition(r.topic() + ".DLQ", r.partition()));

        FixedBackOff backOff = new FixedBackOff(1000L, 5L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
