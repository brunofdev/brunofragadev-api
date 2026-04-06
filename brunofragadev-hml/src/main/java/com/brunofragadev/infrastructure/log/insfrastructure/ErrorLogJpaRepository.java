package com.brunofragadev.infrastructure.log.insfrastructure;
import com.brunofragadev.infrastructure.log.domain.entity.ErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogJpaRepository extends JpaRepository<ErrorLog, Long> {
    Page<ErrorLog> findAllByOrderByDataHoraDesc(Pageable pageable);
}