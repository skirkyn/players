package com.toptal.soccer.repo;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


/**
 * This interface describes available methods to work with the team in the database
 */
public interface TeamRepo extends CrudRepository<Team, Long> {

    @Query(value = "select t.players FROM Team t where t.id = :teamId",
            countQuery = "select cast(size(t.players) as long) FROM Team t where t.id = :teamId")
    Page<Player> findPlayers(@Param("teamId") Long teamId, Pageable pageable);

}
