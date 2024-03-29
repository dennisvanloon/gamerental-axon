package com.gamerental.command.api.model;

import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateGame {
    private String title;
    private Instant releaseDate;
    private String description;
    private boolean singleplayer;
    private boolean multiplayer;
}
