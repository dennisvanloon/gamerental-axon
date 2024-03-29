package com.gamerental.common.api;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.util.Objects;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class GameReturnedEvent {

    private String gameIdentifier;
    private String returner;

}