package com.gamerental.query;

import com.gamerental.common.api.GameRegisteredEvent;
import com.gamerental.query.es.Gameview;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
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
    public static final String RENTER = "Steven van Beelen";

    public static GameRegisteredEvent.GameRegisteredEventBuilder defaultGameRegisteredEvent() {
        return GameRegisteredEvent.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .title(TITLE)
                .description(DESCRIPTION)
                .releaseDate(RELEASE_DATE)
                .multiplayer(true)
                .singleplayer(true);
    }

    public static Gameview defaultGameView() {
        return Gameview.builder()
                .gameIdentifier(GAME_IDENTIFIER)
                .title(TITLE)
                .description(DESCRIPTION)
                .releaseDate(LocalDate.ofInstant(RELEASE_DATE, ZoneOffset.UTC))
                .multiplayer(true)
                .singleplayer(true)
                .build();
    }
}
