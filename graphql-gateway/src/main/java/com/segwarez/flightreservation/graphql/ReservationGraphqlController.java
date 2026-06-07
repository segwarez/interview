package com.segwarez.flightreservation.graphql;

import com.segwarez.flightreservation.itinerary.Itinerary;
import com.segwarez.flightreservation.reservation.Reservation;
import com.segwarez.flightreservation.reservation.ReservationRepository;
import com.segwarez.flightreservation.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReservationGraphqlController {
    private final ReservationRepository reservationRepository;

    @QueryMapping
    public Reservation reservation(@Argument Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public Reservation reservationByBookingReference(@Argument String bookingReference) {
        return reservationRepository.findByBookingReference(bookingReference).orElse(null);
    }

    @QueryMapping
    public List<Reservation> reservations() {
        return reservationRepository.findAll();
    }

    @MutationMapping
    public Reservation createReservation(@Argument CreateReservationInput input) {
        Reservation reservation = new Reservation(input.bookingReference(), new Itinerary());
        return reservationRepository.save(reservation);
    }

    @MutationMapping
    @Transactional
    public Reservation cancelReservation(@Argument Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + id));
        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservation;
    }
}