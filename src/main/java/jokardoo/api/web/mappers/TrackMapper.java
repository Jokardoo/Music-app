package jokardoo.api.web.mappers;

import jokardoo.api.domain.music.Track;
import jokardoo.api.web.dto.track.TrackDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackMapper extends Mappable<Track, TrackDto> {

//    TrackMapper instance = Mappers.getMapper(TrackMapper.class);
//
//    TrackDto toDto(Track track);
//
//    Track toEntity(TrackDto trackDto);
//
//    List<TrackDto> toDto(List<Track> tracks);
}
