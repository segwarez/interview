package com.segwarez.flightreservation.itinerary;

import com.segwarez.flightreservation.airport.Airport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Segment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Itinerary itinerary;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Airport origin;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Airport destination;
    @OneToMany(mappedBy = "segment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Leg> legs = new HashSet<>();

    public Segment(Airport origin, Airport destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public void addLeg(Leg leg) {
        legs.add(leg);
        leg.setSegment(this);
    }
}