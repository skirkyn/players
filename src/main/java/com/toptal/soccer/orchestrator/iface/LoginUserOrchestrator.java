package com.toptal.soccer.orchestrator.iface;

import com.toptal.soccer.model.User;
import lombok.Data;

/**
 * A specification for the user login action
 */
public interface LoginUserOrchestrator {

    /**
     * Logs in a new user with given email and password if curent email exists in the databse, and the stored password hash matches
     * a hash from given password
     *
     * @param email    user's email
     * @param password user's password
     * @return a registered user
     */
    LoginResult login(final String email, final String password);

    @Data
    class LoginResult {
        private final String jwtToken;
        private final Long userId;
    }
}
