package com.gamerental.query.es;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.time.LocalDate;

import static org.springframework.data.elasticsearch.annotations.FieldType.Date;

@Document(indexName = "games")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class Gameview {

    @Id
    private String gameIdentifier;
    private String title;
    @Field(type = Date, format = DateFormat.date)
    private LocalDate releaseDate;
    private String description;
    private boolean singleplayer;
    private boolean multiplayer;

}
