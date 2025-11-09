package com.amcom.desafio_tecnico_amcom.config.kafka;

import com.amcom.desafio_tecnico_amcom.model.Event.OrderEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaConsumerConfig - Testes unitários")
class KafkaConsumerConfigTest {

    @Mock
    private ConsumerFactory<String, OrderEvent> consumerFactory;

    @Mock
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @InjectMocks
    private KafkaConsumerConfig config;

    @Test
    @DisplayName("Cria container com um único tópico configurado")
    void criaContainerComUmTopico() {
        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
                config.kafkaListenerContainerFactory(consumerFactory, kafkaTemplate);

        var container = factory.createContainer("orders");
        assertNotNull(container);
        assertArrayEquals(new String[]{"orders"}, container.getContainerProperties().getTopics());
    }

    @Test
    @DisplayName("Cria container com múltiplos tópicos configurados")
    void criaContainerComMultiplosTopicos() {
        ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
                config.kafkaListenerContainerFactory(consumerFactory, kafkaTemplate);

        var container = factory.createContainer("orders", "orders.DLQ");
        assertNotNull(container);
        assertArrayEquals(new String[]{"orders", "orders.DLQ"}, container.getContainerProperties().getTopics());
    }

    @Test
    @DisplayName("Factory usa o ConsumerFactory fornecido")
    void factoryUsaConsumerFactoryFornecido() {
        var factory = config.kafkaListenerContainerFactory(consumerFactory, kafkaTemplate);
        assertSame(consumerFactory, factory.getConsumerFactory());
    }

    @Test
    @DisplayName("Container recém criado não está em execução")
    void containerRecemCriadoNaoEstaEmExecucao() {
        var factory = config.kafkaListenerContainerFactory(consumerFactory, kafkaTemplate);
        var container = factory.createContainer("orders");
        assertFalse(container.isRunning());
    }
}
