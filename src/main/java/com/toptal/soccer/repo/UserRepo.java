package com.toptal.soccer.repo;

import com.toptal.soccer.model.Team;
import com.toptal.soccer.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * This interface describes available methods to work with the user in the database
 */
public interface UserRepo extends CrudRepository<User, Long> {

    @Query(value = "select u.team FROM User u where u.id = :userId")
    Optional<Team> findTeamByUserId(@Param("userId") Long userId);

    Optional<User> findByEmail(String email);
}
