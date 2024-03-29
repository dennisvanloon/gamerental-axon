package com.gamerental.command.api;

import com.gamerental.command.api.model.CreateGameResponse;
import com.gamerental.command.model.RegisterGameCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.gamerental.command.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
public class GameCommandEndpointTest {

    private final CommandGateway commandGateway;
    private final GameCommandEndpoint gameCommandEndpoint;

    public GameCommandEndpointTest() {
        commandGateway = Mockito.mock(CommandGateway.class);
        gameCommandEndpoint = new GameCommandEndpoint(commandGateway);
    }

    @Test
    public void testCreateGame() {
        ArgumentCaptor<RegisterGameCommand> captor = ArgumentCaptor.forClass(RegisterGameCommand.class);
        when(commandGateway.sendAndWait(captor.capture())).thenReturn("some-id");

        ResponseEntity<CreateGameResponse> response = gameCommandEndpoint.registerGame(defaultCreateGameBuilder().build());

        RegisterGameCommand registerGameCommand = captor.getValue();
        assertEquals(TITLE, registerGameCommand.getTitle());
        assertEquals(DESCRIPTION, registerGameCommand.getDescription());
        assertEquals(RELEASE_DATE, registerGameCommand.getReleaseDate());
        assertTrue(registerGameCommand.isSingleplayer());
        assertTrue(registerGameCommand.isMultiplayer());

        assertEquals("some-id", response.getBody().getId());
        assertEquals(CREATED, response.getStatusCode());
    }

}
