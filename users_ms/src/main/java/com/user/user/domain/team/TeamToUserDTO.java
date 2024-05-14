package com.user.user.domain.team;

public record TeamToUserDTO(String name, String picture, String category) {
    public TeamToUserDTO(Team team) {
        this(team.getName(), team.getPicture(), team.getCategory());
    }
}
