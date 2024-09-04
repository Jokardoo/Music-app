package jokardoo.api.domain.user;

import jakarta.persistence.*;
import jokardoo.api.domain.music.Track;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    private String name;

    private String password;

    // Это поле не будет храниться в БД, поэтому помечаем аннотацией
    @Transient
    private String passwordConfirmation;

    private String email;

    // Это не отдельная сущность, поэтому хранить в БД ее не будем.
    // Поэтому будем хранить его как название (Enumerated EnumType.STRING)
    // таблицу users_roles Hibernate может создать сам
    @Column(name = "role")  // в БД название таблицы role, а в нашем приложении roles
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @CollectionTable(name = "users_tracks")
    @OneToMany
    @JoinColumn(name = "track_id")
    private List<Track> tracks;


    public User() {

    }
}
