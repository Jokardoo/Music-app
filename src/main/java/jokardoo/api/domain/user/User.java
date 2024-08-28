package jokardoo.api.domain.user;

import jokardoo.api.domain.music.Artist;
import jokardoo.api.domain.music.Track;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class User implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String password;

    private String passwordConfirmation;

    private String email;

    private LocalDateTime userCreationTime;

    private Set<Role> roles;

    private List<Track> tracks;

    private Set<Artist> artists;

    public User() {

    }
}
