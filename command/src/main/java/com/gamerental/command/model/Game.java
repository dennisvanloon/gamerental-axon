package com.gamerental.command.model;

import com.gamerental.common.api.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
class Game {

    @AggregateIdentifier
    private String gameIdentifier;
    private int stock;
    private Instant releaseDate;
    private Set<String> renters;

    @CommandHandler
    public Game(RegisterGameCommand command) {
        apply(GameRegisteredEvent.builder()
                .gameIdentifier(command.getGameIdentifier())
                .title(command.getTitle())
                .description(command.getDescription())
                .releaseDate(command.getReleaseDate())
                .singleplayer(command.isSingleplayer())
                .multiplayer(command.isMultiplayer())
                .build()
        );
    }

    @CommandHandler
    public void handle(RentGameCommand command) {
        if (stock <= 0) {
            throw new IllegalStateException(
                    "Insufficient items in stock for game with identifier [" + gameIdentifier + "]"
            );
        }
        if (Instant.now().isBefore(releaseDate)) {
            throw new IllegalStateException(
                    "Game with identifier [" + gameIdentifier + "] cannot be rented out as it has not been released yet"
            );
        }
        apply(GameRentedEvent.builder().gameIdentifier(gameIdentifier).renter(command.getRenter()).build());
    }

    @CommandHandler
    public void handle(ReturnGameCommand command) {
        if (!renters.contains(command.getReturner())) {
            throw new IllegalStateException("A game should be returned by someone who has actually rented it");
        }
        apply(GameReturnedEvent.builder().gameIdentifier(gameIdentifier).returner(command.getReturner()).build());
    }

    @EventSourcingHandler
    public void on(GameRegisteredEvent event) {
        this.gameIdentifier = event.getGameIdentifier();
        this.stock = 1;
        this.releaseDate = event.getReleaseDate();
        this.renters = new HashSet<>();
    }

    @EventSourcingHandler
    public void on(GameRentedEvent event) {
        this.stock--;
        this.renters.add(event.getRenter());
    }

    @EventSourcingHandler
    public void on(GameReturnedEvent event) {
        this.stock++;
        this.renters.remove(event.getReturner());
    }

    @ExceptionHandler(resultType = IllegalStateException.class)
    public void handle(IllegalStateException exception) {
        ExceptionStatusCode statusCode;
        if (exception.getMessage().contains("Insufficient")) {
            statusCode = ExceptionStatusCode.INSUFFICIENT;
        } else if (exception.getMessage().contains("not been released")) {
            statusCode = ExceptionStatusCode.UNRELEASED;
        } else if (exception.getMessage().contains("actually rented it")) {
            statusCode = ExceptionStatusCode.DIFFERENT_RETURNER;
        } else {
            statusCode = ExceptionStatusCode.UNKNOWN_EXCEPTION;
        }
        throw new RentalCommandException(exception.getMessage(), exception, statusCode);
    }

    public Game() {
        // Required by Axon
    }
}
