package jokardoo.api.services;

import jokardoo.api.domain.music.Track;
import jokardoo.api.domain.music.TrackFile;

import java.util.List;

public interface TrackService {
    List<Track> getAllByName(String name);

    Track getById(long id);

    Track save(Track track);

    List<Track> getByTrackNameAndArtistName(String trackName, String artistName);

    Track getOneByTrackNameAndArtistName(String trackName, String artistName);

    void delete(Long id);

    void uploadFile(Long id, TrackFile file);

    void assignToArtistById(Long trackId, Long artistId);

    void assignTrackToUserStorage(Long userId, Long trackId);

    List<Track> getByGenre(String genre);

//    void createRelationshipBetweenTrackAndGenre(Long trackId, Genre genre);
}
