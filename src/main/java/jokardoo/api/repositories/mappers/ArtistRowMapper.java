package jokardoo.api.repositories.mappers;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Track;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArtistRowMapper {


    // Предназначен для возвращения 1 исполнителя. Не ленивая загрузка (подгружает все треки исполнителя)

    public static Artist rowMap(ResultSet resultSet) {
        try {
            Set<String> genres = new HashSet<>();
            List<Track> tracks = new ArrayList<>();

            // заполняем жанры и треки для артиста
            while (resultSet.next()) {
                String genre = resultSet.getString("tracks_genres_genre");
                genres.add(genre);

                Track curTrack = new Track();
                curTrack.setId(resultSet.getLong("track_id"));
                curTrack.setName(resultSet.getString("track_name"));
                curTrack.setTrackGenre(genre);
                curTrack.setFullTime(resultSet.getInt("track_full_time"));

                tracks.add(curTrack);
            }

            resultSet.beforeFirst();

            if (resultSet.next()) {
                Artist artist = new Artist();

                artist.setId(resultSet.getLong("artist_id"));
                artist.setName(resultSet.getString("artist_name"));
                artist.setDescription(resultSet.getString("artist_description"));

                artist.setTracks(tracks);

                return artist;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @SneakyThrows
    public static Set<Artist> rowMaps(ResultSet resultSet) {

        Set<Artist> artists = new HashSet<>();

        while (resultSet.next()) {
            Artist artist = new Artist();

            artist.setId(resultSet.getLong("artist_id"));
            artist.setName(resultSet.getString("artist_name"));
            artist.setDescription(resultSet.getString("artist_description"));

            artists.add(artist);
        }

        return artists;
    }
}
