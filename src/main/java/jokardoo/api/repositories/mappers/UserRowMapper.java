package jokardoo.api.repositories.mappers;

import jokardoo.api.domain.music.Track;
import jokardoo.api.domain.user.Role;
import jokardoo.api.domain.user.User;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.*;

public class UserRowMapper {

    @SneakyThrows
    public static User rowMap(ResultSet resultSet) {

        List<Track> trackList = new ArrayList<>();
        Set<Role> roles = new HashSet<>();

        while (resultSet.next()) {
            Track curTrack = new Track();
            Role role = Role.valueOf(resultSet.getString("user_role_role"));

            curTrack.setArtist(resultSet.getString("artist_name"));
            curTrack.setId(resultSet.getLong("track_id"));
            curTrack.setName(resultSet.getString("track_name"));

            roles.add(role);
            trackList.add(curTrack);
        }

        resultSet.beforeFirst();


        if (resultSet.next()) {
            User user = new User();

            user.setName(resultSet.getString("user_name"));
            user.setEmail(resultSet.getString("user_email"));
            user.setId(resultSet.getLong("user_id"));
            user.setTracks(trackList);
            user.setPassword(resultSet.getString("user_password"));
            user.setUsername(resultSet.getString("user_username"));
            user.setRoles(roles);

            return user;
        }
        return null;
    }
}
