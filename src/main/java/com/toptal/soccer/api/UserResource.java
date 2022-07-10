package com.toptal.soccer.api;

import com.toptal.soccer.dto.Credentials;
import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.dto.Team;
import com.toptal.soccer.dto.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class defines all web API that logically belongs to the User resource
 */
@RestController
public class UserResource {

    /**
     * This method searches for a user by id
     * @param id - user id, should not be null. The authenticated user has to match the user id that is passed in request.
     * @return a response with a user entity as a body
     */
    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User find(@PathVariable String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method creates a new user if there is no user with such email.
     * @param user - a user object that contains a credentials object with the email amd password
     *
     * @return a newly created user instance with the id
     */
    @PostMapping(path = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody User user) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method performs user login operation
     * @param credentials - credentials object that contains user's email and password
     *
     * @return - in case of success a login object is returned that contains user id and a JWT token
     */
    @PostMapping(path = "/user/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResult login(@RequestBody Credentials credentials) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method searches for a user team by user id.
     *
     * @param id  - user id, should not be null. The authenticated user has to match the user id that is passed in request.
     *
     * @return a team object
     */
    @GetMapping(path = "/user/{id}/team", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team findUserTeam(@PathVariable String id) {
        throw new UnsupportedOperationException();
    }
}
