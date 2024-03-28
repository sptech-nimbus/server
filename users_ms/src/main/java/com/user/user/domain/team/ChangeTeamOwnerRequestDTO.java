package com.user.user.domain.team;

import java.time.LocalDateTime;

import com.user.user.domain.user.User;

public record ChangeTeamOwnerRequestDTO(User relatedUser, LocalDateTime expirationDate, User mainUser) {
}