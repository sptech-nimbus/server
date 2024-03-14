package com.user.user.domains.athleteDesc;

import java.util.UUID;

import com.user.user.domains.athlete.Athlete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "athlete_desc")
@Table(name = "athlete_desc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AthleteDesc {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "athlete_desc_id")
    private UUID id;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "position")
    private String position;

    @OneToOne
    @JoinColumn(name = "athlete_id", referencedColumnName = "athlete_id")
    private Athlete athlete;

    public AthleteDesc(AthleteDescDTO dto) {
        this.setPosition(dto.position());
        this.setWeight(dto.weight());
        this.setHeight(dto.height());
        this.setAthlete(dto.athlete());
    }

    @Override
    public String toString() {
        return "AthleteDesc {" + '\'' +
                "id=" + id + '\'' +
                "weight=" + weight + '\'' +
                "height=" + height + '\'' +
                "position=" + position + '\'' +
                "athlete=" + athlete + '\'' +
                "}";
    }

    
}