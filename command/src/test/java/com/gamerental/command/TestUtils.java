package com.gamerental.command;

import com.gamerental.command.api.model.CreateGame;
import com.gamerental.command.model.RegisterGameCommand;
import com.gamerental.command.model.RentGameCommand;
import com.gamerental.command.model.ReturnGameCommand;
import com.gamerental.common.api.*;

import java.time.Instant;
import java.util.UUID;

public class TestUtils {

    public static final String GAME_IDENTIFIER = UUID.randomUUID().toString();
    public static final String OTHER_GAME_IDENTIFIER = UUID.randomUUID().toString();
    public static final String TITLE = "Super Mario Kart";
    public static final String OTHER_TITLE = "Sonic The Hedgehog";
    public static final Instant RELEASE_DATE = Instant.parse("1993-01-21T00:00:01.000009Z");
    public static final Instant OTHER_RELEASE_DATE = Instant.parse("1991-06-23T00:00:01.000009Z");
    public static final String DESCRIPTION = "Kart racing game with the entire Nintendo cast";
    public static final String OTHER_DESCRIPTION = "A platformer in which a super-fast hedgehog is saving animals from a crazy scientist\n";
    public static final String RENTER = "Dennis";
    public static final String OTHER_RENTER = "Piet";

    public static RegisterGameCommand.RegisterGameCommandBuilder defaultGameRegistereGameCommand() {
        return RegisterGameCommand.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .title(TITLE)
                .description(DESCRIPTION)
                .releaseDate(RELEASE_DATE)
                .multiplayer(true)
                .singleplayer(true);
    }

    public static RentGameCommand.RentGameCommandBuilder defaultRentGameCommand() {
        return RentGameCommand.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .renter(RENTER);
    }

    public static ReturnGameCommand.ReturnGameCommandBuilder defaultReturnGameCommand() {
        return ReturnGameCommand.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .returner(RENTER);
    }

    public static GameRegisteredEvent.GameRegisteredEventBuilder defaultGameRegisteredEvent() {
        return GameRegisteredEvent.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .title(TITLE)
                .description(DESCRIPTION)
                .releaseDate(RELEASE_DATE)
                .multiplayer(true)
                .singleplayer(true);
    }

    public static GameRentedEvent.GameRentedEventBuilder defaultGameRentedEvent() {
        return GameRentedEvent.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .renter(RENTER);
    }

    public static GameReturnedEvent.GameReturnedEventBuilder defaultGameReturnedEvent() {
        return GameReturnedEvent.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .returner(RENTER);
    }

    public static CreateGame.CreateGameBuilder defaultCreateGameBuilder() {
        return CreateGame.builder()
                .title(TITLE)
                .description(DESCRIPTION)
                .releaseDate(RELEASE_DATE)
                .multiplayer(true)
                .singleplayer(true);
    }



}
