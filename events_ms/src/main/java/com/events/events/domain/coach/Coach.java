package com.events.events.domain.coach;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.events.events.domain.team.Team;

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


    @Override
    public String toString() {
        return "Coach {" + '\'' +
                "id=" + id + '\'' +
                "firstName=" + firstName + '\'' +
                "lastName=" + lastName + '\'' +
                "birthDate=" + birthDate + '\'' +
                "phone=" + phone + '\'' +
                "picture=" + picture + '\'' +
                "teams=" + teams + '\'' +
                "}";
    }

    
}
