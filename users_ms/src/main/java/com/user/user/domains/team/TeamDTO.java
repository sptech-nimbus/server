package com.user.user.domains.team;

import com.user.user.domains.coach.Coach;

public record TeamDTO(
        String category,
        String picture,
        String local,
        String name,
        Coach coach) {
}
