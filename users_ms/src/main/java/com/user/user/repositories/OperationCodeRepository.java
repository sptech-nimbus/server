package com.user.user.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.operationCodes.OperationCode;

public interface OperationCodeRepository extends JpaRepository<OperationCode, UUID> {
    Optional<OperationCode> findByCode(String code);
}
