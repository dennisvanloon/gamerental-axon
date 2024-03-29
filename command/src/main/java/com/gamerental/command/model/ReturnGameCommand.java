package com.gamerental.command.model;

import lombok.*;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ReturnGameCommand {

    @TargetAggregateIdentifier
    private String gameIdentifier;
    private String returner;

}
