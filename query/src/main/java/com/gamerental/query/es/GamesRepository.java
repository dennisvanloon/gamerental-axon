package com.gamerental.query.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GamesRepository extends ElasticsearchRepository<Gameview, String> {
}
