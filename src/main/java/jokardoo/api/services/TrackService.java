package jokardoo.api.services;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Genre;
import jokardoo.api.domain.music.Track;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Optional;

public interface TrackService {
    List<Track> getByName(String name);

    Track getById(long id);

    Track update(Track track);

    List<Track> getByTrackNameAndArtistName(String trackName, String artistName);

    Track getOneByTrackNameAndArtistName(String trackName, String artistName);

    void delete(Long id);

    Track create(Track track, Genre genre);


    void assignToArtistById(Long trackId, Long artistId);

    void assignTrackToUserStorage(Long userId, Long trackId);

    List<Track> getByGenre(String genre);

//    void createRelationshipBetweenTrackAndGenre(Long trackId, Genre genre);
}
