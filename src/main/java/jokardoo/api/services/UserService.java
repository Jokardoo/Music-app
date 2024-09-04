package jokardoo.api.services;

import jokardoo.api.domain.user.User;
import org.springframework.data.repository.query.Param;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    boolean isTrackContains(@Param("userId") Long userId, @Param("trackId") Long trackId);

    void delete(Long id);

}
