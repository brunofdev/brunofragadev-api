package com.brunofragadev.infrastructure.log.application.usecase;

import com.brunofragadev.infrastructure.log.domain.entity.ErrorLog;
import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class CreateNewErrorLogUseCase {
    private final ErrorLogRepository repository;

    public CreateNewErrorLogUseCase(ErrorLogRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void execute (Exception e, String endpoint,
                         String httpRequest,
                         String loggedUser){
        ErrorLog errorLog = ErrorLog.register(e, endpoint, httpRequest, loggedUser);
        repository.save(errorLog);
    }
}
