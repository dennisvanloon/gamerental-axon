package com.gamerental.query.es;

import com.gamerental.common.api.FullGameCatalogQuery;
import com.gamerental.common.api.GameRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@ProcessingGroup("game-catalog")
public class GameCatalogESProjector {

    private final QueryUpdateEmitter updateEmitter;
    private final GamesRepository gamesRepository;

    public GameCatalogESProjector(GamesRepository gamesRepository, QueryUpdateEmitter updateEmitter) {
        this.gamesRepository = gamesRepository;
        this.updateEmitter = updateEmitter;
    }

    @EventHandler
    public void on(GameRegisteredEvent event) {
        String title = event.getTitle();

        gamesRepository.save(new Gameview(event.getGameIdentifier(),
                title,
                LocalDate.ofInstant(event.getReleaseDate(), ZoneOffset.UTC),
                event.getDescription(),
                event.isSingleplayer(),
                event.isMultiplayer()));

        updateEmitter.emit(FullGameCatalogQuery.class, query -> true, title);
    }

//    @EventHandler
//    public void on(GameRentedEvent event) {
//        Optional<GameView> result = repository.findById(event.getGameIdentifier());
//        if (result.isPresent()) {
//            result.get().decrementStock();
//        } else {
//            throw new IllegalArgumentException("Game with id [" + event.getGameIdentifier() + "] could not be found.");
//        }
//    }
//
//    @EventHandler
//    public void on(GameReturnedEvent event) {
//        Optional<GameView> result = repository.findById(event.getGameIdentifier());
//        if (result.isPresent()) {
//            result.get().incrementStock();
//        } else {
//            throw new IllegalArgumentException("Game with id [" + event.getGameIdentifier() + "] could not be found.");
//        }
//    }
//
//    @QueryHandler
//    public Game handle(FindGameQuery query) {
//        String gameIdentifier = query.getGameIdentifier();
//        return repository.findById(gameIdentifier)
//                         .map(gameView -> new Game(
//                                 gameView.getTitle(),
//                                 gameView.getReleaseDate(),
//                                 gameView.getDescription(),
//                                 gameView.isSingleplayer(),
//                                 gameView.isMultiplayer()
//                         ))
//                         .orElseThrow(() -> new IllegalArgumentException(
//                                 "Game with id [" + gameIdentifier + "] could not be found."
//                         ));
//    }
//
    @QueryHandler
    public List<String> handle(FullGameCatalogQuery query) {
        return StreamSupport.stream(gamesRepository.findAll().spliterator(), false)
                         .map(Gameview::getTitle)
                         .collect(toList());
    }

//    @ExceptionHandler(resultType = IllegalArgumentException.class)
//    public void handle(IllegalArgumentException exception) {
//        ExceptionStatusCode statusCode;
//        if (exception.getMessage().contains("could not be found")) {
//            statusCode = ExceptionStatusCode.GAME_NOT_FOUND;
//        } else {
//            statusCode = ExceptionStatusCode.UNKNOWN_EXCEPTION;
//        }
//        throw new RentalQueryException(exception.getMessage(), exception, statusCode);
//    }
//
//    @ResetHandler
//    public void reset() {
//        logger.info("Resetting the game catelog projection");
//        repository.deleteAll();
//    }
}
