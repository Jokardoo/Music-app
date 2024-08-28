package jokardoo.api.web.mappers;

import jokardoo.api.domain.music.Track;
import jokardoo.api.web.dto.track.TrackDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackMapper {

    TrackMapper instance = Mappers.getMapper(TrackMapper.class);

    TrackDto toDto(Track track);

    Track toEntity(TrackDto trackDto);

    List<TrackDto> toDto(List<Track> tracks);
}
