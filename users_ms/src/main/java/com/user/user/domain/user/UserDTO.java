package com.user.user.domain.user;

import com.user.user.domain.athlete.AthleteDTO;
import com.user.user.domain.coach.CoachDTO;

public record UserDTO(String email, String password, CoachDTO coach, AthleteDTO athlete) {

}

