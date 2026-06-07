# Flight Reservation GraphQL Gateway

http://localhost:8080/graphiql

```graphql
query {
  reservationByBookingReference(bookingReference: "ABC123") {
    bookingReference
    status
    passengers {
      firstName
      lastName
    }
    itinerary {
      segments {
        origin {
          code
        }
        destination {
          code
        }
        legs {
          flightNumber
          departureAirport {
            code
          }
          arrivalAirport {
            code
          }
          aircraft {
            model
            tailNumber
          }
        }
      }
    }
  }
}
```