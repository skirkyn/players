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

    Optional<User> findByTeamId(Long teamId);

    @Query(value = "select u.* from users u " +
            "join team t on t.id = u.team_id " +
            "join team_players tp on tp.team_id = t.id where tp.players_id = :playerId", nativeQuery = true)
    Optional<User> findByPlayerId(Long playerId);

    @Query(value = "select u.* from users u join transfer t on u.id = t.seller_id where  t.id = :transferId", nativeQuery = true)
    Optional<User> findByTransferId(Long transferId);
}
