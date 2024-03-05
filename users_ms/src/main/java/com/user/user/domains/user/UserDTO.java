package com.user.user.domains.user;

import com.user.user.domains.athlete.AthleteDTO;
import com.user.user.domains.coach.CoachDTO;

public record UserDTO(String email, String password, CoachDTO coach, AthleteDTO athlete) {

}
