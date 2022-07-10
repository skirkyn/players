package com.toptal.soccer.repo;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


/**
 * This interface describes available methods to work with the team in the database
 */
public interface TeamRepo extends CrudRepository<Team, Long> {

    Page<Player> findPlayers(Pageable pageable);

}
