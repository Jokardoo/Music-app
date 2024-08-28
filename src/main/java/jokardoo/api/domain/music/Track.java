package jokardoo.api.domain.music;

import jokardoo.api.web.dto.track.TrackDto;
import jokardoo.api.web.mappers.TrackMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track implements Serializable {
    private Long id;

    private String trackGenre;
    private String name;
    private String artist;

    private String downloadLink;

    private int fullTime;


}
