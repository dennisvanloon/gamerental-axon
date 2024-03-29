package com.gamerental.query;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gamerental.common.api.GameRegisteredEvent;
import com.gamerental.query.es.GameCatalogESProjector;
import com.gamerental.query.es.GamesRepository;
import com.gamerental.query.es.Gameview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static com.gamerental.query.ElasticsearchTestConfiguration.ES_PASSWORD;
import static com.gamerental.query.TestUtils.defaultGameRegisteredEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnabledIfSystemProperty(named = "spring.profiles.active", matches = "integrationTest")
public class GameRentalQueryApplicationTest {

    private static final DockerImageName ES_CONTAINER_IMAGE = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.13.0");
    private static final ElasticsearchContainer elasticsearchContainer;

    @Autowired
    GameCatalogESProjector gameCatalogESProjector;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ElasticsearchClient elasticsearchClient;

    @Autowired
    GamesRepository gamesRepository;

    static {
        elasticsearchContainer = new ElasticsearchContainer(ES_CONTAINER_IMAGE)
                .withExposedPorts(9200)
                .withPassword(ES_PASSWORD);
        elasticsearchContainer.getEnvMap().remove("xpack.security.enabled");
        elasticsearchContainer.start();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("elasticsearch.endpoint", elasticsearchContainer::getHttpHostAddress);
        registry.add("elasticsearch.certificate", () -> new String(elasticsearchContainer.copyFileFromContainer("/usr/share/elasticsearch/config/certs/http_ca.crt", InputStream::readAllBytes)));
    }

    @Test
    public void createGame() throws IOException {
        GameRegisteredEvent gameRegisteredEvent = defaultGameRegisteredEvent().build();
        gameCatalogESProjector.on(gameRegisteredEvent);

        GetResponse<ObjectNode> gameviewGetResponse = elasticsearchClient.get(g -> g.index("games").id(gameRegisteredEvent.getGameIdentifier()), ObjectNode.class);
        assertTrue(gameviewGetResponse.found());

        Gameview gameview = objectMapper.readValue(objectMapper.writeValueAsString(gameviewGetResponse.source()), Gameview.class);
        assertEquals(gameRegisteredEvent.getGameIdentifier(), gameview.getGameIdentifier());

        Optional<Gameview> gameviewFromRepo = gamesRepository.findById(gameRegisteredEvent.getGameIdentifier());
        assertTrue(gameviewFromRepo.isPresent());
    }

}
