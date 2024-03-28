package com.user.user.domain.team;

import com.user.user.domain.coach.Coach;

public record TeamDTO(
                String category,
                String picture,
                String local,
                String name,
                Coach coach) {
}