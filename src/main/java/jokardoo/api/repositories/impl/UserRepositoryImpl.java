//package jokardoo.api.repositories.impl;
//
//import jokardoo.api.domain.exceptions.*;
//import jokardoo.api.domain.user.Role;
//import jokardoo.api.domain.user.User;
//import jokardoo.api.repositories.DataSourceConfig;
//import jokardoo.api.repositories.UserRepository;
//import jokardoo.api.repositories.mappers.UserRowMapper;
//import lombok.RequiredArgsConstructor;
//import org.apache.ibatis.annotations.Mapper;
//import org.springframework.stereotype.Repository;
//
//import java.sql.*;
//import java.util.Optional;
//
//@Repository
//
//@RequiredArgsConstructor
//public class UserRepositoryImpl implements UserRepository {
//
//    private final DataSourceConfig dataSourceConfig;
//
//    private final String FIND_BY_USERNAME = """
//            SELECT u.id as user_id,
//            u.username as user_username,
//            u.name as user_name,
//            u.email as user_email,
//            u.password as user_password,
//            ur.role as user_role_role,
//            ut.track_id as user_track_track_id,
//            t.name as track_name,
//            t.id as track_id,
//            a.name as artist_name
//            FROM users u
//            LEFT JOIN users_roles ur on u.id = ur.user_id
//            LEFT JOIN users_tracks ut on u.id = ut.user_id
//            LEFT JOIN tracks t on ut.track_id = t.id
//            LEFT JOIN artists_tracks at on t.id = at.track_id
//            LEFT JOIN artists a on at.artist_id = a.id
//            WHERE u.username = ?
//            """;
//
//    private final String FIND_BY_ID = """
//            SELECT u.id as user_id,
//            u.username as user_username,
//            u.name as user_name,
//            u.email as user_email,
//            u.password as user_password,
//            ur.role as user_role_role,
//            ut.track_id as user_track_track_id,
//            t.name as track_name,
//            t.id as track_id,
//            a.name as artist_name
//            FROM users u
//            LEFT JOIN users_roles ur on u.id = ur.user_id
//            LEFT JOIN users_tracks ut on u.id = ut.user_id
//            LEFT JOIN tracks t on ut.track_id = t.id
//            LEFT JOIN artists_tracks at on t.id = at.track_id
//            LEFT JOIN artists a on at.artist_id = a.id
//            WHERE u.id = ?
//            """;
//
//    private final String UPDATE = """
//            UPDATE users
//            SET username = ?,
//                password = ?,
//                name = ?
//                WHERE id = ?
//            """;
//
//    private final String CREATE = """
//            INSERT INTO users
//            (username, name, password, email)
//            VALUES
//            (?, ?, ?, ?)
//            """;
//
//    private final String INSERT_USER_ROLE = """
//            INSERT INTO users_roles (user_id, role)
//            VALUES
//            (?, ?)
//            """;
//
//    private final String IS_TRACK_CONTAINS = """
//            SELECT exists(
//            SELECT 1
//            FROM users_tracks
//            WHERE user_id = ? AND
//            track_id = ?
//            )
//            """;
//
//    private final String DELETE = """
//            DELETE FROM users
//            WHERE id = ?
//            """;
//
//    @Override
//    public Optional<User> findById(Long id) {
//
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID);
//
//            statement.setLong(1, id);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return Optional.ofNullable(UserRowMapper.rowMap(rs));
//            }
//        } catch (SQLException e) {
//            throw new UserNotFoundException("User not found exception!");
//        }
//
//    }
//
//    @Override
//    public Optional<User> findByUsername(String username) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
//                    ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//
//            statement.setString(1, username);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return Optional.ofNullable(UserRowMapper.rowMap(rs));
//            }
//        } catch (SQLException e) {
//            throw new UserNotFoundException("User not found exception!");
//        }
//    }
//
//    @Override
//    public void update(User user) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//
//            PreparedStatement statement = connection.prepareStatement(UPDATE);
//            statement.setString(1, user.getUsername());
//            statement.setString(2, user.getPassword());
//            statement.setString(3, user.getName());
//            statement.setLong(4, user.getId());
//
//            statement.executeUpdate();
//        }
//        catch (SQLException e) {
//            throw new UserCanNotBeUpdatedException("User cannot be updated exception");
//        }
//
//    }
//
//    @Override
//    public void create(User user) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//
//            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
//            statement.setString(1, user.getUsername());
//            statement.setString(2, user.getName());
//            statement.setString(3, user.getPassword());
//            statement.setString(4, user.getEmail());
//
//            statement.executeUpdate();
//
//            try (ResultSet rs = statement.getGeneratedKeys()) {
//                rs.next();
//                user.setId(rs.getLong(1));
//            }
//        }
//        catch (SQLException e) {
//            throw new UserCanNotBeCreatedException("User cannot be created exception");
//        }
//    }
//
//    @Override
//    public void insertUserRole(Long userId, Role role) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//
//            PreparedStatement statement = connection.prepareStatement(INSERT_USER_ROLE);
//            statement.setLong(1, userId);
//            statement.setString(2, role.name());
//
//            statement.executeUpdate();
//        }
//        catch (SQLException e) {
//            throw new UserCanNotBeCreatedException("User cannot be created exception");
//        }
//    }
//
//    @Override
//    public boolean isTrackContains(Long userId, Long trackId) {
//        try {
//            Connection connection = dataSourceConfig.getConnection();
//            PreparedStatement statement = connection.prepareStatement(IS_TRACK_CONTAINS);
//            statement.setLong(1, userId);
//            statement.setLong(2, trackId);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                return rs.getBoolean(1);
//            }
//        } catch (SQLException e) {
//            throw new TrackNotContainsException("Track not contains exception");
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
//        } catch (SQLException e) {
//            throw new UserCannotBeDeletedException("User cannot be deleted exception!");
//        }
//    }
//}
