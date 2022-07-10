package com.toptal.soccer.repo;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.User;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepo extends CrudRepository<Player, Long> {
}
