package com.brunofragadev.infrastructure.log.domain.repository;

import com.brunofragadev.infrastructure.log.api.dto.response.LogPage;
import com.brunofragadev.infrastructure.log.domain.entity.ErrorLog;

public interface ErrorLogRepository {
    void save(ErrorLog log);
    LogPage findPaginated(int page, int size);
}
