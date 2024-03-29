package com.gamerental.query.es;

import com.gamerental.common.api.FullGameCatalogQuery;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.junit.jupiter.api.Test;

import static com.gamerental.query.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GameCatalogESProjectorTest {

    private final GameCatalogESProjector gameCatalogESProjector;

    private final GamesRepository gamesRepository;

    private final QueryUpdateEmitter queryUpdateEmitter;

    public GameCatalogESProjectorTest() {
        queryUpdateEmitter = mock(QueryUpdateEmitter.class);
        gamesRepository = mock(GamesRepository.class);
        gameCatalogESProjector = new GameCatalogESProjector(gamesRepository, queryUpdateEmitter);
    }

    @Test
    public void receiveGameRegisteredEvent() {
        gameCatalogESProjector.on(defaultGameRegisteredEvent().build());
        verify(gamesRepository).save(defaultGameView());
        verify(queryUpdateEmitter).emit(eq(FullGameCatalogQuery.class), any(), eq(TITLE));
    }
}
