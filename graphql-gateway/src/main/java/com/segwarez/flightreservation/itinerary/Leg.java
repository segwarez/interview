package com.segwarez.flightreservation.itinerary;

import com.segwarez.flightreservation.aircraft.Aircraft;
import com.segwarez.flightreservation.airport.Airport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Leg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    private Segment segment;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Airport departureAirport;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Airport arrivalAirport;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Aircraft aircraft;

    public Leg(String flightNumber, Airport departureAirport, Airport arrivalAirport, Aircraft aircraft) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.aircraft = aircraft;
    }
}