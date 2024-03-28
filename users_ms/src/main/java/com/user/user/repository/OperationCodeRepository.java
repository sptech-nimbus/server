package com.user.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.operationCodes.OperationCode;

public interface OperationCodeRepository extends JpaRepository<OperationCode, UUID> {
    Optional<OperationCode> findByCode(String code);
}
