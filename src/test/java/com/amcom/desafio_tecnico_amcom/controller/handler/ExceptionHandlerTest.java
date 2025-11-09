package com.amcom.desafio_tecnico_amcom.controller.handler;

import com.amcom.desafio_tecnico_amcom.exception.NotFoundException;
import com.amcom.desafio_tecnico_amcom.model.dto.out.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExceptionHandler - Testes unitários")
class ExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Trata MissingRequestValueException e retorna HTTP 400 com erro de validação")
    void deveTratarMissingRequestValueExceptionERetornarBadRequest() {
        String caminhoEsperado = "/api/orders";
        String mensagemErro = "Parametro obrigatorio ausente";
        MissingRequestValueException exception = mock(MissingRequestValueException.class);
        when(exception.getMessage()).thenReturn(mensagemErro);
        when(request.getServletPath()).thenReturn(caminhoEsperado);
        LocalDateTime inicio = LocalDateTime.now();

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadRequest(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals(mensagemErro, response.getBody().getMessage());
        assertEquals(caminhoEsperado, response.getBody().getPath());
        assertNotNull(response.getBody().getTimeStamp());
        assertFalse(response.getBody().getTimeStamp().isBefore(inicio));

        verify(request).getServletPath();
        verify(exception, atLeastOnce()).getMessage();
    }

    @Test
    @DisplayName("Trata NotFoundException e retorna HTTP 404 com mensagem customizada")
    void deveTratarNotFoundExceptionERetornarNotFound() {
        String caminhoEsperado = "/api/orders/123";
        String mensagemErro = "Pedido nao encontrado";
        NotFoundException exception = new NotFoundException(mensagemErro);
        when(request.getServletPath()).thenReturn(caminhoEsperado);
        LocalDateTime inicio = LocalDateTime.now();

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleRecursoNaoEncontradoException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals(mensagemErro, response.getBody().getMessage());
        assertEquals(caminhoEsperado, response.getBody().getPath());
        assertNotNull(response.getBody().getTimeStamp());
        assertFalse(response.getBody().getTimeStamp().isBefore(inicio));

        verify(request).getServletPath();
    }

    @Test
    @DisplayName("Trata HttpRequestMethodNotSupportedException e retorna HTTP 405")
    void deveTratarHttpRequestMethodNotSupportedExceptionERetornarMethodNotAllowed() {
        String caminhoEsperado = "/api/orders";
        String mensagemErro = "Metodo POST nao suportado";
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");
        when(request.getServletPath()).thenReturn(caminhoEsperado);
        LocalDateTime inicio = LocalDateTime.now();

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpRequestMethodNotSupportedException(exception, request);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(response.getBody());
        assertEquals(405, response.getBody().getStatus());
        assertEquals("Method Not Allowed", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("POST"));
        assertEquals(caminhoEsperado, response.getBody().getPath());
        assertNotNull(response.getBody().getTimeStamp());
        assertFalse(response.getBody().getTimeStamp().isBefore(inicio));

        verify(request).getServletPath();
    }

    @Test
    @DisplayName("Trata MethodArgumentNotValidException e retorna HTTP 400 com mensagem padrão")
    void deveTratarMethodArgumentNotValidExceptionERetornarBadRequest() throws Exception {
        String caminhoEsperado = "/api/orders";
        BindingResult bindingResult = mock(BindingResult.class);

        class DummyController {
            @SuppressWarnings("unused")
            void metodoTeste(String campo) {}
        }
        java.lang.reflect.Method method = DummyController.class.getDeclaredMethod("metodoTeste", String.class);
        org.springframework.core.MethodParameter methodParameter = new org.springframework.core.MethodParameter(method, 0);

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        when(request.getServletPath()).thenReturn(caminhoEsperado);
        LocalDateTime inicio = LocalDateTime.now();

        ResponseEntity<ErrorResponse> response = exceptionHandler.tratarErroArgumentoInvalido(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Dados invalidos.", response.getBody().getMessage());
        assertEquals(caminhoEsperado, response.getBody().getPath());
        assertNotNull(response.getBody().getTimeStamp());
        assertFalse(response.getBody().getTimeStamp().isBefore(inicio));

        verify(request).getServletPath();
    }

    @Test
    @DisplayName("Trata Exception genérica e retorna HTTP 500 com mensagem de erro interno")
    void deveTratarExceptionGenericaERetornarInternalServerError() {
        String caminhoEsperado = "/api/orders";
        Exception exception = new RuntimeException("Erro inesperado");
        when(request.getServletPath()).thenReturn(caminhoEsperado);
        LocalDateTime inicio = LocalDateTime.now();

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("Erro de infraestrutura do servico.", response.getBody().getMessage());
        assertEquals(caminhoEsperado, response.getBody().getPath());
        assertNotNull(response.getBody().getTimeStamp());
        assertFalse(response.getBody().getTimeStamp().isBefore(inicio));

        verify(request).getServletPath();
    }
}