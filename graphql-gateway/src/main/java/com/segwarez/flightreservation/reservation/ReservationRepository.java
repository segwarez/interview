package com.segwarez.flightreservation.reservation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @EntityGraph(attributePaths = {
            "passengers",
            "itinerary",
            "itinerary.segments",
            "itinerary.segments.origin",
            "itinerary.segments.destination",
            "itinerary.segments.legs",
            "itinerary.segments.legs.departureAirport",
            "itinerary.segments.legs.arrivalAirport",
            "itinerary.segments.legs.aircraft"
    })
    Optional<Reservation> findByBookingReference(String bookingReference);
}