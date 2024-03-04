package com.user.user.models.team;

import com.user.user.models.coach.Coach;

public record TeamDTO(
                String category,
                String picture,
                String local,
                Coach coach) {

}
