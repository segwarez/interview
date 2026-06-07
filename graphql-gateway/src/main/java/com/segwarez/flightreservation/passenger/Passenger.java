package com.segwarez.flightreservation.passenger;

import com.segwarez.flightreservation.reservation.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private PassengerType type;
    @ManyToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    public Passenger(String firstName, String lastName, PassengerType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }
}