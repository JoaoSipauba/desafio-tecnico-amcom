package com.amcom.desafio_tecnico_amcom.config.mongo;

import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MongoInitializer - Testes unitários")
class MongoInitializerTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoDatabase mongoDatabase;

    @InjectMocks
    private MongoInitializer mongoInitializer;

    @Test
    @DisplayName("Cria coleção orders quando não existe e verifica banco sem falhar")
    void criaColecaoQuandoNaoExiste() throws Exception {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.getName()).thenReturn("db-teste");
        when(mongoTemplate.collectionExists("orders")).thenReturn(false);

        mongoInitializer.run();

        verify(mongoTemplate, atLeastOnce()).getDb();
        verify(mongoDatabase, atLeastOnce()).getName();
        verify(mongoDatabase, atLeastOnce()).listCollectionNames();
        verify(mongoTemplate).collectionExists("orders");
        verify(mongoTemplate).createCollection("orders");
    }

    @Test
    @DisplayName("Não cria coleção orders quando já existe")
    void naoCriaColecaoQuandoJaExiste() throws Exception {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.getName()).thenReturn("db-teste");
        when(mongoTemplate.collectionExists("orders")).thenReturn(true);

        mongoInitializer.run();

        verify(mongoTemplate).collectionExists("orders");
        verify(mongoTemplate, never()).createCollection("orders");
    }

    @Test
    @DisplayName("Continua execução e cria coleção mesmo quando verificação do banco lança exceção")
    void continuaQuandoVerificacaoBancoFalha() throws Exception {
        when(mongoTemplate.getDb()).thenThrow(new RuntimeException("falha-db"));
        when(mongoTemplate.collectionExists("orders")).thenReturn(false);

        mongoInitializer.run();

        verify(mongoTemplate).collectionExists("orders");
        verify(mongoTemplate).createCollection("orders");
    }

    @Test
    @DisplayName("Ignora erro ao listar coleções e não cria quando coleção já existe")
    void ignoraErroListagemColecoesENaoCriaSeExiste() throws Exception {
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.getName()).thenReturn("db-teste");
        when(mongoDatabase.listCollectionNames()).thenThrow(new RuntimeException("falha-listagem"));
        when(mongoTemplate.collectionExists("orders")).thenReturn(true);

        mongoInitializer.run();

        verify(mongoTemplate).collectionExists("orders");
        verify(mongoTemplate, never()).createCollection("orders");
    }
}