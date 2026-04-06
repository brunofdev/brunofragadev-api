package com.brunofragadev.infrastructure.log.insfrastructure;

import com.brunofragadev.infrastructure.log.api.dto.response.LogPage;
import com.brunofragadev.infrastructure.log.domain.entity.ErrorLog;
import com.brunofragadev.infrastructure.log.domain.repository.ErrorLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;


@Repository
public class ErrorLogRepositoryAdapter implements ErrorLogRepository {

    private final ErrorLogJpaRepository jpaRepository;

    public ErrorLogRepositoryAdapter(ErrorLogJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(ErrorLog log) {
        jpaRepository.save(log);
    }

    @Override
    public LogPage findPaginated(int page, int size) {
        Page<ErrorLog> springPage = jpaRepository.findAllByOrderByDataHoraDesc(PageRequest.of(page, size));

        return new LogPage(
                springPage.getContent(),
                springPage.getNumber(),
                springPage.getTotalPages(),
                springPage.getTotalElements()
        );
    }
}
