package com.user.user.domain.operationCodes;

import java.time.LocalDateTime;
import java.util.UUID;

import com.user.user.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "operation_code")
@Table(name = "operation_code")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "operation_code_id")
    private UUID id;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @JoinColumn(name = "main_user_id", referencedColumnName = "user_id", nullable = false)
    @ManyToOne
    private User mainUser;

    @JoinColumn(name = "related_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private User relatedUser;

    public OperationCode(String operation, String code, LocalDateTime expirationDate, User mainUser, User relatedUser) {
        this.operation = operation;
        this.code = code;
        this.expirationDate = expirationDate;
        this.mainUser = mainUser;
        this.relatedUser = relatedUser;
    }

    @Override
    public String toString() {
        return "OperationCode [id=" + id
                + ", operation=" + operation
                + ", code=" + code + ", expirationDate="
                + expirationDate + ", mainUser="
                + mainUser + ", relatedUser="
                + relatedUser + "]";
    }
}