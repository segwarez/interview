package com.segwarez.flightreservation.config;

import com.segwarez.flightreservation.aircraft.Aircraft;
import com.segwarez.flightreservation.aircraft.AircraftBodyType;
import com.segwarez.flightreservation.airport.Airport;
import com.segwarez.flightreservation.itinerary.Itinerary;
import com.segwarez.flightreservation.itinerary.Leg;
import com.segwarez.flightreservation.itinerary.Segment;
import com.segwarez.flightreservation.passenger.Passenger;
import com.segwarez.flightreservation.passenger.PassengerType;
import com.segwarez.flightreservation.reservation.Reservation;
import com.segwarez.flightreservation.reservation.ReservationRepository;
import com.segwarez.flightreservation.reservation.ReservationStatus;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.ChainedInstrumentation;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.graphql.autoconfigure.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GraphQlConfiguration {
    private final ReservationRepository reservationRepository;

    @Bean
    GraphQlSourceBuilderCustomizer graphQlSourceBuilderCustomizer() {
        return builder -> builder.configureGraphQl(graphQlBuilder ->
                graphQlBuilder.instrumentation(
                        new ChainedInstrumentation(List.of(
                                new MaxQueryDepthInstrumentation(10),
                                new MaxQueryComplexityInstrumentation(100)
                        ))
                )
        );
    }

    @PostConstruct
    public void init() {
        Airport waw = new Airport("WAW", "Warsaw Chopin Airport", "Warsaw", "Poland");
        Airport fra = new Airport("FRA", "Frankfurt Airport", "Frankfurt", "Germany");
        Airport jfk = new Airport("JFK", "John F. Kennedy International Airport", "New York", "United States");

        Aircraft embraer = new Aircraft(
                "SP-LDD",
                "Embraer",
                "E190",
                AircraftBodyType.NARROW_BODY,
                106
        );

        Aircraft dreamliner = new Aircraft(
                "SP-LRB",
                "Boeing",
                "787-9 Dreamliner",
                AircraftBodyType.WIDE_BODY,
                294
        );

        Leg firstLeg = new Leg("LO381", waw, fra, embraer);
        Leg secondLeg = new Leg("LH400", fra, jfk, dreamliner);

        Segment segment = new Segment(waw, jfk);
        segment.addLeg(firstLeg);
        segment.addLeg(secondLeg);

        Itinerary itinerary = new Itinerary();
        itinerary.addSegment(segment);

        Reservation reservation = new Reservation("ABC123", itinerary);
        reservation.addPassenger(new Passenger("Jan", "Kowalski", PassengerType.ADULT));
        reservation.addPassenger(new Passenger("Anna", "Kowalska", PassengerType.ADULT));
        reservation.setStatus(ReservationStatus.CONFIRMED);

        reservationRepository.save(reservation);
    }
}