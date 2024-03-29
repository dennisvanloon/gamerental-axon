package com.gamerental.command.model;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.gamerental.command.TestUtils.*;
import static com.gamerental.common.api.ExceptionStatusCode.*;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.axonframework.test.matchers.Matchers.matches;

class GameTest {

    private FixtureConfiguration<com.gamerental.command.model.Game> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(com.gamerental.command.model.Game.class);
    }

    @Test
    void testRegisterGameCommandAppliesGameRegisteredEvent() {
        fixture.givenNoPriorActivity()
               .when(defaultGameRegistereGameCommand().build())
               .expectEvents(defaultGameRegisteredEvent().build());
    }

    @Test
    void testRentGameCommandAppliesGameRentedEvent() {
        fixture.given(defaultGameRegisteredEvent().build())
               .when(defaultRentGameCommand().build())
               .expectEvents(defaultGameRentedEvent().build());
    }

    @Test
    void testRentGameCommandThrowsExceptionForInsufficientStock() {
        fixture.given(defaultGameRegisteredEvent().build(),
                      defaultGameRentedEvent().build())
               .when(defaultRentGameCommand().build())
               .expectException(matches(e -> e instanceof RentalCommandException
                               && ((RentalCommandException) e).getDetails().isPresent()
                               && ((RentalCommandException) e).getDetails().get().equals(INSUFFICIENT)
               ));
    }

    @Test
    void testRentGameCommandThrowsExceptionForToEarlyRenting() {
        Instant futureDate = Instant.now().plus(1, DAYS);
        fixture.given(defaultGameRegisteredEvent().releaseDate(futureDate).build())
               .when(defaultRentGameCommand().build())
               .expectException(matches(
                       e -> e instanceof RentalCommandException
                               && ((RentalCommandException) e).getDetails().isPresent()
                               && ((RentalCommandException) e).getDetails().get().equals(UNRELEASED)
               ));
    }

    @Test
    void testReturnGameCommandAppliesGameReturnedEvent() {
        fixture.given(defaultGameRegisteredEvent().build(),
                      defaultGameRentedEvent().build())
               .when(defaultReturnGameCommand().build())
               .expectEvents(defaultGameReturnedEvent().build());
    }

    @Test
    void testReturnGameCommandThrowsExceptionForReturnerNotMatchingOriginalRenter() {
        fixture.given(defaultGameRegisteredEvent().build())
               .when(defaultReturnGameCommand().returner(OTHER_RENTER).build())
               .expectException(matches(
                       e -> e instanceof RentalCommandException
                               && ((RentalCommandException) e).getDetails().isPresent()
                               && ((RentalCommandException) e).getDetails().get().equals(DIFFERENT_RETURNER)
               ));
    }
}
