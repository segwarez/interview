package com.segwarez.flightreservation.itinerary;

import com.segwarez.flightreservation.reservation.Reservation;
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
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "itinerary", fetch = FetchType.LAZY)
    private Reservation reservation;
    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Segment> segments = new HashSet<>();

    public void addSegment(Segment segment) {
        segments.add(segment);
        segment.setItinerary(this);
    }
}