package jokardoo.api.web.mappers;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.web.dto.artist.ArtistDto;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    Artist toEntity(ArtistDto dto);

    ArtistDto toDto(Artist artist);

    List<ArtistDto> toDto(List<Artist> artistList);
}
