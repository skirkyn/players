package com.toptal.soccer.manager.iface;

import com.toptal.soccer.model.Team;
import com.toptal.soccer.model.User;

import java.util.Optional;

/**
 * This interface describes the API of the user service
 */
public interface UserManager {

    /**
     * Finds user by id
     * @param id id of the user
     *
     * @return an optional object with the user if the user with such id exists. Otherwise, an empty optional
     */
    Optional<User> findById(final Long id);

    /**
     * Finds user by email
     * @param email email of the user
     *
     * @return an optional object with the user if the user with such email exists. Otherwise, an empty optional
     */
    Optional<User> findUserByEmail(final String email);

    /**
     * Save a user
     * @param user to save
     *
     * @return a saved user
     */
    User save(User user);


    /**
     * Returns team  by the user id
     * @param userId user id
     * @return user's team
     */
    Team findUserTeam(Long userId);
}
