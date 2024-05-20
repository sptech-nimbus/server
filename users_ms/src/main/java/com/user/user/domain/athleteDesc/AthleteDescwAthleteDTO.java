package com.user.user.domain.athleteDesc;

import java.time.LocalDate;

public record AthleteDescwAthleteDTO(String picture,
        String firstName,
        String lastName,
        Integer number,
        String position,
        LocalDate birthDate,
        Double height,
        Double weight,
        String address,
        Integer pts,
        Integer ast,
        Integer rebounds,
        String email,
        String phone,
        String category) {
    public AthleteDescwAthleteDTO(AthleteDesc ad, Integer points, Integer assists,
            Integer rebounds) {
        this(ad.getAthlete().getPicture(),
                ad.getAthlete().getFirstName(),
                ad.getAthlete().getLastName(),
                ad.getNumber(),
                ad.getPosition(),
                ad.getAthlete().getBirthDate(),
                ad.getHeight(),
                ad.getWeight(),
                ad.getAddress(),
                points,
                assists,
                rebounds,
                ad.getAthlete().getUser().getEmail(),
                ad.getAthlete().getPhone(),
                ad.getAthlete().getCategory());
    }
}
