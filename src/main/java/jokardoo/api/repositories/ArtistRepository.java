package jokardoo.api.repositories;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Track;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Mapper

public interface ArtistRepository {

    Optional<Artist> findByName(String name);

    Optional<Artist> findById(Long id);

    void delete(Long id);

    Set<Artist> findByGenre(String genre);

    Artist create(@Param("name") String name, @Param("description") String description);

    void update(@Param("artist") Artist artist, @Param("id") Long id);

    boolean isArtistContains(String artistName);

    List<Track> findTracksByArtistName(String artistName);


}
