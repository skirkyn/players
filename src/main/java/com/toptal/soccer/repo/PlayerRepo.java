package com.toptal.soccer.repo;

import com.toptal.soccer.model.Player;
import org.springframework.data.repository.CrudRepository;

/**
 * This interface give the access to the persistent player object
 */
public interface PlayerRepo extends CrudRepository<Player, Long> {

}
