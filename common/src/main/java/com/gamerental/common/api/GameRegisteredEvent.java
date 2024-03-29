package com.gamerental.common.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class GameRegisteredEvent {

    private final String gameIdentifier;
    private final String title;
    private final Instant releaseDate;
    private final String description;
    private final boolean singleplayer;
    private final boolean multiplayer;

}
