package jokardoo.api.repositories;

import jokardoo.api.domain.music.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Mapper
@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    Optional<Track> findByNameAndArtist(String name, String artist);

    List<Track> findAllByNameAndArtist(String name, String artist);

    @Query(value = """
            INSERT INTO artists_tracks (artist_id, track_id) VALUES (:artistId, :trackId)
            """, nativeQuery = true)
    void assignToArtistById(@Param("trackId") Long trackId,
                            @Param("artistId") Long artistId);

    @Query(value = """
            INSERT INTO users_tracks (user_id, track_id) VALUES (:userId, :trackId)
            """, nativeQuery = true)
    void assignTrackToUserStorage(@Param("userId") Long userId,
                                  @Param("trackId") Long trackId);

    @Query(value = """
            SELECT a.id as artist_id,
                            a.name as artist_name,
                            a.description as artist_description,
                            atr.track_id as artists_tracks_track_id,
                            tg.genre as tracks_genres_genre,
                            t.id as track_id,
                            t.name as track_name,
                            t.full_time as track_full_time,
                            t.download_link as track_download_link
                            FROM tracks_genres tg
                            LEFT JOIN tracks t on tg.track_id = t.id
                            LEFT JOIN artists_tracks atr on t.id = atr.artist_id
                            LEFT JOIN artists a on atr.artist_id = a.id
                            WHERE tg.genre = :genre
            """, nativeQuery = true)
    List<Track> findByGenre(@Param("genre") String genre);

    List<Track> findAllByName(String name);

//    List<Track> findAllByNameAndArtisAndArtistName(String name, String artistName);

//    List<Track> findByTrackNameAndArtistName(@Param("trackName") String trackName, @Param("artistName") String artistName);

//    void delete(Long id);

//    Track create(@Param("track") Track track, @Param("genre") Genre genre);

//    List<Track> findByName(String name);

//    void update(Track track);

//    Track findById(Long id);

//    Optional<Track> findOneByTrackNameAndArtistName(String trackName, String artistName);
}
