package com.brunofragadev.infrastructure.config;

import com.brunofragadev.infrastructure.handler.GlobalAsyncErrorHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    private final GlobalAsyncErrorHandler errorHandler;

    public AsyncConfig(GlobalAsyncErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return errorHandler;
    }
}