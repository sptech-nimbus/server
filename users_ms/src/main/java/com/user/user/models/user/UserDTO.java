package com.user.user.models.user;

import com.user.user.models.coach.CoachDTO;

public record UserDTO(String email, String password, CoachDTO coach) {

}
