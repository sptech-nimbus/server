package com.events.events.domain.athlete;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Athlete {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String picture;
    private String category;

    @Override
    public String toString() {
        return "Athlete {" + '\'' +
                "id=" + id + '\'' +
                " firstName=" + firstName + '\'' +
                " lastName=" + lastName + '\'' +
                " birthDate=" + birthDate + '\'' +
                " phone=" + phone + '\'' +
                " picture=" + picture + '\'' +
                " category=" + category + '\'' +
                "}";
    }
}

