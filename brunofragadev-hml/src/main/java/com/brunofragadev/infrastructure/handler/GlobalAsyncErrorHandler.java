package com.brunofragadev.infrastructure.handler;


import com.brunofragadev.infrastructure.log.application.usecase.CreateNewErrorLogUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class GlobalAsyncErrorHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalAsyncErrorHandler.class);

    private final CreateNewErrorLogUseCase createNewErrorLogUseCase;

    public GlobalAsyncErrorHandler(CreateNewErrorLogUseCase createNewErrorLogUseCase) {
        this.createNewErrorLogUseCase = createNewErrorLogUseCase;
    }

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Falha no método assíncrono: {}", method.getName(), ex);
        createNewErrorLogUseCase.execute(
                (Exception) ex,
                method.getDeclaringClass().getSimpleName() + "." + method.getName(),
                "async",
                "System"
        );
    }
}