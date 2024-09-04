package jokardoo.api.repositories.mappers;

import jokardoo.api.domain.music.Track;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrackRowMapper {

    @SneakyThrows
    public static List<Track> rowMaps(ResultSet resultSet) {

        List<Track> tracks = new ArrayList<>();

        while (resultSet.next()) {

            Track curTrack = new Track();

            curTrack.setArtist(resultSet.getString("artist_name"));

            curTrack.setId(resultSet.getLong("track_id"));
            curTrack.setName(resultSet.getString("track_name"));
            curTrack.setTrackGenre(resultSet.getString("tracks_genres_genre"));
            curTrack.setFullTime(resultSet.getInt("track_full_time"));
            curTrack.setDownloadLink(resultSet.getString("track_download_link"));

            tracks.add(curTrack);
        }

        return tracks;
    }

    @SneakyThrows
    public static Optional<Track> rowMap(ResultSet resultSet) {


        while (resultSet.next()) {

            Track curTrack = new Track();

            curTrack.setArtist(resultSet.getString("artist_name"));

            curTrack.setId(resultSet.getLong("track_id"));
            curTrack.setName(resultSet.getString("track_name"));
            curTrack.setTrackGenre(resultSet.getString("tracks_genres_genre"));
            curTrack.setFullTime(resultSet.getInt("track_full_time"));
            curTrack.setDownloadLink(resultSet.getString("download_link"));

            return Optional.ofNullable(curTrack);
        }
        return null;
    }

}
