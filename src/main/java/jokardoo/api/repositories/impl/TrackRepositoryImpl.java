//package jokardoo.api.repositories.impl;
//
//import jokardoo.api.domain.exceptions.AssignToUserStorageException;
//import jokardoo.api.domain.exceptions.AssignTrackToArtistException;
//import jokardoo.api.domain.exceptions.TrackCannotBeCreatedException;
//import jokardoo.api.domain.exceptions.TrackNotFoundException;
//import jokardoo.api.domain.music.Artist;
//import jokardoo.api.domain.music.Genre;
//import jokardoo.api.domain.music.Track;
//import jokardoo.api.repositories.DataSourceConfig;
//import jokardoo.api.repositories.TrackRepository;
//import jokardoo.api.repositories.mappers.ArtistRowMapper;
//import jokardoo.api.repositories.mappers.TrackRowMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.apache.ibatis.annotations.Mapper;
//import org.springframework.stereotype.Repository;
//
//import java.sql.*;
//import java.util.List;
//import java.util.Optional;
//
//
//@Repository
//
//@RequiredArgsConstructor
//public class TrackRepositoryImpl implements TrackRepository {
//
//    private final DataSourceConfig dataSourceConfig;
//
//    private final String FIND_BY_GENRE = """
//            SELECT a.id as artist_id,
//                a.name as artist_name,
//                a.description as artist_description,
//                atr.track_id as artists_tracks_track_id,
//                tg.genre as tracks_genres_genre,
//                t.id as track_id,
//                t.name as track_name,
//                t.full_time as track_full_time,
//                t.download_link as track_download_link
//                FROM tracks_genres tg
//                LEFT JOIN tracks t on tg.track_id = t.id
//                LEFT JOIN artists_tracks atr on t.id = atr.artist_id
//                LEFT JOIN artists a on atr.artist_id = a.id
//                WHERE tg.genre = ?
//                """;
//
//    private final String FIND_BY_NAME = """
//            SELECT a.id as artist_id,
//                a.name as artist_name,
//                a.description as artist_description,
//                atr.track_id as artists_tracks_track_id,
//                tg.genre as tracks_genres_genre,
//                t.id as track_id,
//                t.name as track_name,
//                t.full_time as track_full_time,
//                t.download_link as track_download_link
//                FROM tracks t
//                LEFT JOIN tracks_genres tg on tg.track_id = t.id
//                LEFT JOIN artists_tracks atr on t.id = atr.track_id
//                LEFT JOIN artists a on atr.artist_id = a.id
//                WHERE t.name = ?
//                """;
//
//    private final String FIND_BY_ID = """
//            SELECT a.id as artist_id,
//                a.name as artist_name,
//                a.description as artist_description,
//                atr.track_id as artists_tracks_track_id,
//                tg.genre as tracks_genres_genre,
//                t.id as track_id,
//                t.name as track_name,
//                t.full_time as track_full_time,
//                t.download_link as track_download_link
//                FROM tracks t
//                LEFT JOIN tracks_genres tg on tg.track_id = t.id
//                LEFT JOIN artists_tracks atr on t.id = atr.track_id
//                LEFT JOIN artists a on atr.artist_id = a.id
//                WHERE t.id = ?
//                """;
//
//    private final String FIND_BY_TRACK_NAME_AND_ARTIST_NAME = """
//            SELECT a.id as artist_id,
//                a.name as artist_name,
//                a.description as artist_description,
//                atr.track_id as artists_tracks_track_id,
//                tg.genre as tracks_genres_genre,
//                t.id as track_id,
//                t.name as track_name,
//                t.full_time as track_full_time,
//                t.download_link as track_download_link
//                FROM tracks t
//                LEFT JOIN tracks_genres tg on tg.track_id = t.id
//                LEFT JOIN artists_tracks atr on t.id = atr.track_id
//                LEFT JOIN artists a on atr.artist_id = a.id
//                WHERE t.name = ? AND a.name = ?
//                """;
//
//    private final String DELETE = """
//            DELETE FROM tracks
//            WHERE id = ?
//            """;
//
//    private final String CREATE_TRACK = """
//            INSERT INTO tracks (name, full_time, download_link, artist, trackGenre)
//            VALUES (?, ?, ?, ?, ?)
//            """;
//
//    private final String UPDATE = """
//            UPDATE tracks
//                SET name = ?,
//                    full_time = ?,
//                    download_link = ?,
//                    artist = ?
//            WHERE id = ?
//            """;
//
//    private final String ASSIGN_TRACK_TO_USER_STORAGE = """
//            INSERT INTO users_tracks (user_id, track_id) VALUES (?, ?)
//            """;
//
//    private final String CREATE_RELATION_BETWEEN_TRACK_AND_ARTIST = """
//            INSERT INTO artists_tracks (artist_id, track_id)
//            VALUES (?, ?)
//            """;
//
//    private final String CREATE_RELATION_BETWEEN_TRACK_AND_Genre = """
//            INSERT INTO tracks_genres (track_id, genre)
//            VALUES (?, ?)
//            """;
//
//    @Override
//    public List<Track> findByGenre(String genre) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_GENRE);
//
//            statement.setString(1, genre);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return TrackRowMapper.rowMaps(rs);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public List<Track> findByName(String name) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME);
//            statement.setString(1, name);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return TrackRowMapper.rowMaps(rs);
//            }
//        } catch (SQLException e) {
//            throw new TrackNotFoundException("Track not found!");
//        }
//    }
//
//    @Override
//    public void update(Track track) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(UPDATE);
//
//            statement.setString(1, track.getName());
//            statement.setLong(2, track.getFullTime());
//            statement.setString(3, track.getDownloadLink());
//            statement.setString(4, track.getArtist());
//
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Track findById(Long id) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
//            statement.setLong(1, id);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return TrackRowMapper.rowMap(rs).orElseThrow(() -> new TrackNotFoundException("Track not found!"));
//            }
//        } catch (SQLException e) {
//            throw new TrackNotFoundException("SQL exception!");
//        }
//    }
//
//    @Override
//    public List<Track> findByTrackNameAndArtistName(String trackName, String artistName) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_TRACK_NAME_AND_ARTIST_NAME,
//                    ResultSet.CONCUR_READ_ONLY,
//                    ResultSet.TYPE_SCROLL_INSENSITIVE);
//
//            statement.setString(1, trackName);
//            statement.setString(2, artistName);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return TrackRowMapper.rowMaps(rs);
//            }
//
//        } catch (SQLException e) {
//            throw new TrackNotFoundException("Track with artist name not found!");
//        }
//    }
//
//    @Override
//    public Optional<Track> findOneByTrackNameAndArtistName(String trackName, String artistName) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_TRACK_NAME_AND_ARTIST_NAME,
//                    ResultSet.CONCUR_READ_ONLY,
//                    ResultSet.TYPE_SCROLL_INSENSITIVE);
//
//            statement.setString(1, trackName);
//            statement.setString(2, artistName);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                List<Track> tracks = TrackRowMapper.rowMaps(rs);
//
//                return tracks.stream()
//                        .filter(track -> track.getArtist().equalsIgnoreCase(artistName))
//                        .filter(track -> track.getName().equalsIgnoreCase(trackName))
//                        .distinct().findFirst();
//            }
//
//        } catch (SQLException e) {
//            throw new TrackNotFoundException("Track with artist name not found!");
//        }
//    }
//
//    @Override
//    public void delete(Long id) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(DELETE);
//            statement.setLong(1, id);
//
//            statement.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
////    @Override
////    public void createRelationshipBetweenTrackAndGenre(Long trackId, Genre genre) {
////        try {
////            Connection connection = dataSourceConfig.getConnection();
////            PreparedStatement statement = connection.prepareStatement(CREATE_RELATION_BETWEEN_TRACK_AND_Genre);
////
////            statement.setLong(1, trackId);
////            statement.setString(2, genre.name());
////
////            statement.executeUpdate();
////        } catch (SQLException e) {
////            throw new TrackCannotBeCreatedException("An exception was thrown when trying to establish a connection between a track and a genre");
////        }
////    }
//
//    @Override
//    @SneakyThrows
//    public Track create(Track track, Genre genre) {
//        try {
//            // создаем трек
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(CREATE_TRACK, PreparedStatement.RETURN_GENERATED_KEYS);
//
//
//
//
//            statement.setString(1, track.getName());
//            statement.setLong(2, track.getFullTime());
//            statement.setString(3, track.getDownloadLink());
//            statement.setString(4, track.getArtist());
//            statement.setString(5, genre.name());
//
//            statement.executeUpdate();
//
//            try (ResultSet rs = statement.getGeneratedKeys()) {
//                rs.next();
//                long trackId = rs.getLong("id");
//                track.setId(trackId);
//            }
//
//
//            return track;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//    @Override
//    public void assignToArtistById(Long trackId, Long artistId) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//
//            PreparedStatement statement = connection.prepareStatement(CREATE_RELATION_BETWEEN_TRACK_AND_ARTIST
//            , Statement.RETURN_GENERATED_KEYS);
//            statement.setLong(1, artistId);
//            statement.setLong(2, trackId);
//
//            statement.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new AssignTrackToArtistException("Assign track to artist exception!");
//        }
//    }
//
//    @Override
//    public void assignTrackToUserStorage(Long userId, Long trackId) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//
//            PreparedStatement statement = connection.prepareStatement(ASSIGN_TRACK_TO_USER_STORAGE);
//            statement.setLong(1, userId);
//            statement.setLong(2, trackId);
//            statement.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new AssignToUserStorageException("Assign track to user storage exception!");
//        }
//    }
//}
