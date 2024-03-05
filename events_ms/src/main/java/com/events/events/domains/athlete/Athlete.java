package com.events.events.domains.athlete;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Athlete {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String picture;
    private String category;
}