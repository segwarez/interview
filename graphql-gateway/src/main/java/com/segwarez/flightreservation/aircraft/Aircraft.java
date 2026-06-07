package com.segwarez.flightreservation.aircraft;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tailNumber;
    private String manufacturer;
    private String model;
    @Enumerated(EnumType.STRING)
    private AircraftBodyType bodyType;
    private int numberOfSeats;

    public Aircraft(String tailNumber, String manufacturer, String model, AircraftBodyType bodyType, int numberOfSeats) {
        this.tailNumber = tailNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.bodyType = bodyType;
        this.numberOfSeats = numberOfSeats;
    }
}