package com.gamerental.command.model;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.beans.ConstructorProperties;
import java.time.Instant;
import java.util.Objects;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegisterGameCommand {

    @TargetAggregateIdentifier
    private String gameIdentifier;
    private String title;
    private Instant releaseDate;
    private String description;
    private boolean singleplayer;
    private boolean multiplayer;

}
