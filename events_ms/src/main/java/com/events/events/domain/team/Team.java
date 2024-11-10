package com.events.events.domain.team;

import java.util.List;
import java.util.UUID;

import com.events.events.domain.athlete.Athlete;
import com.events.events.domain.coach.Coach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private UUID id;
    private String name;
    private String category;
    private String picture;
    private String local;
    private Integer level;
    private Coach coach;
    private List<Athlete> athletes;
    
    @Override
    public String toString() {
        return "Team {" + '\'' +
                "id=" + id + '\'' +
                "name=" + name + '\'' +
                "category=" + category + '\'' +
                "picture=" + picture + '\'' +
                "local=" + local + '\'' +
                "level=" + level + '\'' +
                "coach=" + coach + '\'' +
                "athletes=" + athletes + '\'' +
                "}";
    }
}