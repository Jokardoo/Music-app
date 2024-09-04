//package jokardoo.api.repositories.impl;
//
//import jokardoo.api.domain.exceptions.ArtistCantBeCreatedException;
//import jokardoo.api.domain.exceptions.ArtistNotFoundException;
//import jokardoo.api.domain.exceptions.GenreNotFoundException;
//import jokardoo.api.domain.music.Artist;
//import jokardoo.api.domain.music.Track;
//import jokardoo.api.repositories.ArtistRepository;
//import jokardoo.api.repositories.DataSourceConfig;
//import jokardoo.api.repositories.mappers.ArtistRowMapper;
//import jokardoo.api.repositories.mappers.TrackRowMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.sql.*;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//@Repository
//@RequiredArgsConstructor
//public class ArtistRepositoryImpl implements ArtistRepository {
//
//    private final DataSourceConfig dataSourceConfig;
//
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
//                FROM artists a
//                LEFT JOIN artists_tracks atr on a.id = atr.artist_id
//                LEFT JOIN tracks t on atr.track_id = t.id
//                LEFT JOIN tracks_genres tg on t.id = tg.track_id
//                WHERE a.name = ?
//                """;
//
//    private final String DELETE = """
//            DELETE FROM artists
//            WHERE id = ?
//            """;
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
//                FROM artists a
//                LEFT JOIN artists_tracks atr on a.id = atr.artist_id
//                LEFT JOIN tracks t on atr.track_id = t.id
//                LEFT JOIN tracks_genres tg on t.id = tg.track_id
//                WHERE tg.genre = ?
//                """;
//
//    private final String FIND_TRACKS_BY_ARTIST_NAME = """
//           SELECT a.id as artist_id,
//                a.name as artist_name,
//                a.description as artist_description,
//                atr.track_id as artists_tracks_track_id,
//                tg.genre as tracks_genres_genre,
//                t.id as track_id,
//                t.name as track_name,
//                t.full_time as track_full_time,
//                t.download_link as track_download_link
//                FROM artists a
//                LEFT JOIN artists_tracks atr on a.id = atr.artist_id
//                LEFT JOIN tracks t on atr.track_id = t.id
//                LEFT JOIN tracks_genres tg on t.id = tg.track_id
//                WHERE a.name = ?
//            """;
//
//    private final String FIND_BY_ID = """
//           SELECT a.id as artist_id,
//                a.name as artist_name,
//                a.description as artist_description,
//                atr.track_id as artists_tracks_track_id,
//                tg.genre as tracks_genres_genre,
//                t.id as track_id,
//                t.name as track_name,
//                t.full_time as track_full_time,
//                t.download_link as track_download_link
//                FROM artists a
//                LEFT JOIN artists_tracks atr on a.id = atr.artist_id
//                LEFT JOIN tracks t on atr.track_id = t.id
//                LEFT JOIN tracks_genres tg on t.id = tg.track_id
//                WHERE a.id = ?
//            """;
//
//    private final String CREATE = """
//            INSERT INTO artists (name, description)
//            VALUES (?, ?);
//            """;
//
//    private final String UPDATE = """
//            UPDATE artists
//            SET name = ?,
//            SET description = ?
//            WHERE id = ?
//            """;
//
//    // Метод можно поменять, чтобы он возвращал boolean
//    private final String IS_ARTIST_CONTAINS = """
//             SELECT a.id as artist_id,
//                a.name as artist_name,
//                a.description as artist_description,
//                atr.track_id as artists_tracks_track_id,
//                tg.genre as tracks_genres_genre,
//                t.id as track_id,
//                t.name as track_name,
//                t.full_time as track_full_time,
//                t.download_link as track_download_link
//                FROM artists a
//                LEFT JOIN artists_tracks atr on a.id = atr.artist_id
//                LEFT JOIN tracks t on atr.track_id = t.id
//                LEFT JOIN tracks_genres tg on t.id = tg.track_id
//                WHERE a.name = ?
//            """;
//
//    @Override
//    public Optional<Artist> findById(Long id) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
//                    ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//
//            statement.setLong(1, id);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                Artist foundArtist = ArtistRowMapper.rowMap(rs);
//                return Optional.ofNullable(foundArtist);
//            }
//        }
//        catch (SQLException e ) {
//            throw new ArtistNotFoundException("Artist with this id not found");
//        }
//    }
//    @Override
//    public Optional<Artist> findByName(String name) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME,
//                    ResultSet.TYPE_SCROLL_INSENSITIVE, // пробегаем без учета изменений в базе данных
//                    ResultSet.CONCUR_READ_ONLY); // только для чтения
//            statement.setString(1, name);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return Optional.ofNullable(ArtistRowMapper.rowMap(rs));
//            }
//
//        } catch (SQLException e) {
//            throw new ArtistNotFoundException("Artist not found exception");
//        }
//    }
//
//    @Override
//    public Set<Artist> findByGenre(String genre) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_GENRE);
//
//            statement.setString(1, genre);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return ArtistRowMapper.rowMaps(rs);
//            }
//        } catch (SQLException e) {
//            throw new GenreNotFoundException("Genre name not found");
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
//
//    @Override
//    public Artist create(String name, String description) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
//
//
//
//            statement.setString(1, name);
//            statement.setString(2, description);
//
//            statement.executeUpdate();
//
//            try (ResultSet rs = statement.getGeneratedKeys()) {
//                rs.next();
//                Long artistId = statement.getGeneratedKeys().getLong("id");
//
//                Artist createdArtist = new Artist(artistId, name);
//                createdArtist.setDescription(description);
//
//                return createdArtist;
//            }
//
//
//
//        } catch (SQLException e) {
//            throw new ArtistCantBeCreatedException("Artist can not be created Exception");
//        }
//    }
//
//    @Override
//    public List<Track> findTracksByArtistName(String artistName) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_TRACKS_BY_ARTIST_NAME);
//
//            statement.setString(1, artistName);
//
//            List<Track> tracks = TrackRowMapper.rowMaps(statement.executeQuery());
//
//
//            return tracks;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void update(Artist artist, Long id) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(UPDATE);
//
//            statement.setString(1, artist.getName());
//            statement.setString(2, artist.getDescription());
//            statement.setLong(3, id);
//
//            statement.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public boolean isArtistContains(String artistName) {
//        try{
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(IS_ARTIST_CONTAINS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            statement.setString(1, artistName);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                // Артист есть, все ок
//                Artist foundArtist = ArtistRowMapper.rowMap(rs);
//
//                if (foundArtist == null) {
//                    // Если артиста нет
//                    return false;
//                }
//
//                return true;
//            }
//        } catch (SQLException e) {
//            return false;
//        }
//    }
//}
