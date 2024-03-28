package com.user.user.domain.user;

public record ChangePasswordDTO(String code, String oldPassword, String newPassword) {
}