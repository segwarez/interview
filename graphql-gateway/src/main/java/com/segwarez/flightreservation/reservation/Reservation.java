package com.segwarez.flightreservation.reservation;

import com.segwarez.flightreservation.itinerary.Itinerary;
import com.segwarez.flightreservation.passenger.Passenger;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookingReference;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.CREATED;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Passenger> passengers = new LinkedHashSet<>();
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private Itinerary itinerary;

    public Reservation(String bookingReference, Itinerary itinerary) {
        this.bookingReference = bookingReference;
        this.itinerary = itinerary;
        itinerary.setReservation(this);
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        passenger.setReservation(this);
    }
}