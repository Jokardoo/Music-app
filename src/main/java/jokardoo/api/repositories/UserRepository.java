package jokardoo.api.repositories;

import jokardoo.api.domain.user.Role;
import jokardoo.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Mapper
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // т.к. мы отошли от ручного написания запросов в БД в сторону JpaRepository, нам нет необходимости
    // реализовывать все эти методы

    //    Optional<User> findById(Long id);
//
    Optional<User> findByUsername(String username);

    User save(User user);

//    void update(User user);
//
//    void create(User user);


    @Query(value = """
            INSERT INTO users_roles (user_id, role)
                        VALUES
                        (:userId, :role)
            """, nativeQuery = true)
    void insertUserRole(@Param("userId") Long userId, @Param("role") Role role);


    @Query(value = """
            SELECT exists (
                        SELECT 1 
                        FROM users_tracks 
                        WHERE user_id = :userId AND 
                        track_id = :trackId
                        )
            """, nativeQuery = true)
    boolean isTrackContains(@Param("userId") Long userId, @Param("trackId") Long trackId);


//    void delete(Long id);
}
