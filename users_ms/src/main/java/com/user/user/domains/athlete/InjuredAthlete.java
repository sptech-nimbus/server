package com.user.user.domains.athlete;

import com.user.user.domains.injury.Injury;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InjuredAthlete {
    private Athlete athlete;
    private Injury injury;
}