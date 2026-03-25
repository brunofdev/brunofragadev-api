package com.brunofragadev.infrastructure.handler;

import com.brunofragadev.module.user.domain.exception.*;
// import com.brunofragadev.suas.outras.excecoes.aqui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Constrói o JSON padronizado esperado pelo frontend: { status: false, erro: { message: "..." } }
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", false);

        Map<String, String> erro = new HashMap<>();
        erro.put("message", message);
        body.put("erro", erro);

        return ResponseEntity.status(status).body(body);
    }

    // Exceções de Conflito (Ex: Dados duplicados) - Status 409
    @ExceptionHandler({
            UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflictExceptions(RuntimeException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }


    // Exceções de Busca (Ex: Registro não existe no banco) - Status 404
    @ExceptionHandler({
            UserNotFoundException.class,
            UserEmailNotRegisteredException.class

    })
    public ResponseEntity<Map<String, Object>> handleNotFoundExceptions(RuntimeException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Exceções de Regra de Negócio/Validação - Status 400
    @ExceptionHandler({
            InvalidCredentialsException.class,
            VerificationCodeInvalidException.class

    })
    public ResponseEntity<Map<String, Object>> handleBadRequestExceptions(RuntimeException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    // Erros de validação do Spring (@Valid em DTOs) - Status 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("Dados de entrada inválidos.");

        return buildError(HttpStatus.BAD_REQUEST, mensagem);
    }

    // Erro de formatação do JSON na requisição - Status 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJSON(HttpMessageNotReadableException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Formato do JSON inválido ou campos ausentes.");
    }

    // Erro de integridade no banco de dados (Ex: Violação de Unique Constraint) - Status 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrity(DataIntegrityViolationException ex) {
        return buildError(HttpStatus.CONFLICT, "Conflito ao processar os dados. Verifique os valores enviados.");
    }

    // Captura genérica de erros não mapeados (Impede o vazamento de stack trace) - Status 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        logger.error("Erro inesperado no servidor:", ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno no servidor. O administrador do Sistema já esta sabendo");
    }
}