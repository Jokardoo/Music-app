package jokardoo.api.repositories;

import jokardoo.api.domain.user.Role;
import jokardoo.api.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//@Mapper
public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    void update(User user);

    void create(User user);

    void insertUserRole(@Param("userId") Long userId, @Param("role") Role role);

    boolean isTrackContains(@Param("userId") Long userId, @Param("trackId") Long trackId);


    void delete(Long id);
}
