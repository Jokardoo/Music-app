package jokardoo.api.repositories;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

//@Mapper

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByName(String name);
//
//    Optional<Artist> findById(Long id);
//
//    void delete(Long id);

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
                                        FROM artists a
                                        LEFT JOIN artists_tracks atr on a.id = atr.artist_id
                                        LEFT JOIN tracks t on atr.track_id = t.id
                                        LEFT JOIN tracks_genres tg on t.id = tg.track_id
                                        WHERE tg.genre = :genre
            """, nativeQuery = true)
    Set<Artist> findByGenre(@Param("genre") String genre);

//    Artist create(@Param("name") String name, @Param("description") String description);
//
//    void update(@Param("artist") Artist artist, @Param("id") Long id);

    @Query(value = """
            SELECT exists(
                        SELECT 1
                        FROM artists
                        WHERE name = :artistName
                        )
            """, nativeQuery = true)
    boolean isArtistContains(@Param("artistName") String artistName);

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
                            FROM artists a
                            LEFT JOIN artists_tracks atr on a.id = atr.artist_id
                            LEFT JOIN tracks t on atr.track_id = t.id
                            LEFT JOIN tracks_genres tg on t.id = tg.track_id
                            WHERE a.name = :artistName
            """, nativeQuery = true)
    List<Track> findTracksByArtistName(String artistName);

    List<Track> findAllByName(String name);


}
