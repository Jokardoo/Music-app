package jokardoo.api.web.mappers;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.web.dto.artist.ArtistDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper extends Mappable<Artist, ArtistDto> {

//    Artist toEntity(ArtistDto dto);
//
//    ArtistDto toDto(Artist artist);
//
//    List<ArtistDto> toDto(List<Artist> artistList);
}
