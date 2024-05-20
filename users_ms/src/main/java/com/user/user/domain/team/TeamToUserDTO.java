package com.user.user.domain.team;

import java.util.UUID;

public record TeamToUserDTO(String name, String picture, String category, UUID id) {
    public TeamToUserDTO(Team team) {
        this(team.getName(), team.getPicture(), team.getCategory(), team.getId());
    }
}
