package com.events.events.domains.coach;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.events.events.domains.team.Team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coach {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String picture;
    private List<Team> teams;
}
