package com.brunofragadev.infrastructure.log.application.usecase;

import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import com.brunofragadev.infrastructure.log.api.dto.response.LogPage;
import com.brunofragadev.infrastructure.log.api.dto.response.LogPageResponse;
import com.brunofragadev.infrastructure.log.application.mapper.ErrorLogMapper;
import org.springframework.stereotype.Service;

@Service
public class GetPaginatedLogsUseCase {

    private final ErrorLogRepository repository;

    public GetPaginatedLogsUseCase(ErrorLogRepository repository) {
        this.repository = repository;
    }

    public LogPageResponse execute(int page, int size) {
        LogPage logPage = repository.findPaginated(page, size);
        return ErrorLogMapper.toPageResponse(logPage);
    }
}