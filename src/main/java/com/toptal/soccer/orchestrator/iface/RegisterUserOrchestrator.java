package com.toptal.soccer.orchestrator.iface;

import com.toptal.soccer.model.User;

/**
 * A specification for the user registration action
 */
public interface RegisterUserOrchestrator {

    /**
     * Resisters a new user with given email and password
     * @param email user's email
     * @param password user's password
     *
     * @return a registered user
     */
    User register(final String email, final String password);
}
