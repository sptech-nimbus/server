package com.user.user.domains.team;

import java.time.LocalDateTime;

import com.user.user.domains.user.User;

public record ChangeTeamOwnerDTO(User relatedUser, LocalDateTime expirationDate, User mainUser) {
}