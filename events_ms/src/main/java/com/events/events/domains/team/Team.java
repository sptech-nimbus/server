package com.events.events.domains.team;

import java.util.List;

import com.events.events.domains.athlete.Athlete;
import com.events.events.domains.coach.Coach;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private String id;
    private String name;
    private String category;
    private String picture;
    private String local;
    private Coach coach;
    private List<Athlete> athletes;
}