package com.gamerental.command.api;

import com.gamerental.command.api.model.CreateGame;
import com.gamerental.command.api.model.CreateGameResponse;
import com.gamerental.command.model.RegisterGameCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/game")
public class GameCommandEndpoint {

    private final CommandGateway commandGateway;
    public GameCommandEndpoint(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ResponseEntity<CreateGameResponse> registerGame(@RequestBody CreateGame createGame) {
        String aggregateId = commandGateway.sendAndWait(RegisterGameCommand.builder()
                .gameIdentifier(UUID.randomUUID().toString())
                .title(createGame.getTitle())
                .description(createGame.getDescription())
                .releaseDate(createGame.getReleaseDate())
                .singleplayer(createGame.isSingleplayer())
                .multiplayer(createGame.isMultiplayer())
                .build());
        CreateGameResponse createGameResponse = CreateGameResponse.builder()
                .id(aggregateId)
                .build();
        return new ResponseEntity<>(createGameResponse, CREATED);
    }

}
