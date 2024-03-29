package com.gamerental.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamerental.command.api.model.CreateGame;
import org.axonframework.test.server.AxonServerContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.gamerental.command.TestUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnabledIfSystemProperty(named = "spring.profiles.active", matches = "integrationTest")
public class GameRentalCommandApplicationTest {

    private static final DockerImageName AXON_CONTAINER_IMAGE = DockerImageName.parse("axoniq/axonserver:latest-dev");
    private static final AxonServerContainer axonServerContainer;

    static {
        axonServerContainer = new AxonServerContainer(AXON_CONTAINER_IMAGE).withExposedPorts(8024, 8124);
        axonServerContainer.start();
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("axon.axonserver.servers", () -> "localhost:" + axonServerContainer.getMappedPort(8124));
    }

    @Test
    public void createGame() throws Exception {
        CreateGame createGame = defaultCreateGameBuilder().build();

        mvc.perform(post("/api/game")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createGame)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }

}
