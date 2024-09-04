package jokardoo.api.web.mappers;

import jokardoo.api.domain.music.TrackFile;
import jokardoo.api.web.dto.track.TrackFileDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrackFileMapper extends Mappable<TrackFile, TrackFileDto> {
}
