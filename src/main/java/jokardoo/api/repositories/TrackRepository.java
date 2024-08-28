package jokardoo.api.repositories;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Genre;
import jokardoo.api.domain.music.Track;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

//@Mapper
public interface TrackRepository {

    List<Track> findByName(String name);

    void update(Track track);

    Track findById(Long id);

    Optional<Track> findOneByTrackNameAndArtistName(String trackName, String artistName);

    List<Track> findByTrackNameAndArtistName(@Param("trackName") String trackName, @Param("artistName") String artistName);

    void delete(Long id);

    Track create(@Param("track") Track track, @Param("genre") Genre genre);

    void assignToArtistById(@Param("trackId") Long trackId, @Param("artistId") Long artistId);

    void assignTrackToUserStorage(@Param("userId") Long userId, @Param("trackId") Long trackId);

    List<Track> findByGenre(String genre);



}
