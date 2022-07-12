package com.toptal.soccer.api;

import com.toptal.soccer.api.exception.InvalidRequestException;
import com.toptal.soccer.api.exception.NotAuthorizedException;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.Credentials;
import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.dto.Team;
import com.toptal.soccer.dto.User;
import com.toptal.soccer.manager.iface.TeamManager;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.orchestrator.iface.LoginUserOrchestrator;
import com.toptal.soccer.orchestrator.iface.RegisterUserOrchestrator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This class defines all web API that logically belongs to the User resource
 */
@RestController
public class UserResource extends BaseResource{

    private static final String LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_TEAM_FOR_THIS_USER =
            "Logged user is not authorized to access team for this user: ";
    private final RegisterUserOrchestrator registerUserOrchestrator;
    private final LoginUserOrchestrator loginUserOrchestrator;
    private final UserManager userManager;
    private final TeamManager teamManager;
    private final Crypto crypto;
    private final Function<com.toptal.soccer.model.User, User> userToUserDTO;
    private final Function<LoginUserOrchestrator.LoginResult, com.toptal.soccer.dto.LoginResult> loginResultToLoginResultDTO;
    private final BiFunction<com.toptal.soccer.model.Team, BigDecimal, Team> teamToTeamDTO;

    public UserResource(final RegisterUserOrchestrator registerUserOrchestrator,
                        final LoginUserOrchestrator loginUserOrchestrator,
                        final UserManager userManager,
                        TeamManager teamManager, final Crypto crypto,
                        final Function<com.toptal.soccer.model.User, User> userToUserDTO,
                        final Function<LoginUserOrchestrator.LoginResult, LoginResult> loginResultToLoginResultDTO, BiFunction<com.toptal.soccer.model.Team, BigDecimal, Team> teamToTeamDTO) {

        this.registerUserOrchestrator = registerUserOrchestrator;
        this.loginUserOrchestrator = loginUserOrchestrator;
        this.userManager = userManager;
        this.teamManager = teamManager;
        this.crypto = crypto;
        this.userToUserDTO = userToUserDTO;
        this.loginResultToLoginResultDTO = loginResultToLoginResultDTO;
        this.teamToTeamDTO = teamToTeamDTO;
    }

    /**
     * This method searches for a user by id
     *
     * @param id             - user id, should not be null. The authenticated user has to match the user id that is passed in request.
     * @param authentication current authentication
     * @return a response with a user entity as a body
     */
    @GetMapping(path = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User find(@PathVariable final String id, final Authentication authentication) {

        final Long decrypted = Long.valueOf(crypto.decrypt(id));

        if (!Objects.equals(authentication.getPrincipal(), decrypted)) {
            throw new NotAuthorizedException("Logged user is not authorized to access this user: " + id);
        }

        return userManager.findById(decrypted)
                .map(userToUserDTO)
                .orElseThrow(() -> new InvalidRequestException("User with id doesn't exist: " + id));
    }


    /**
     * This method creates a new user if there is no user with such email.
     *
     * @param user - a user object that contains a credentials object with the email amd password
     * @return a newly created user instance with the id
     */
    @PostMapping(path = "/user/register", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userToUserDTO.apply(
                registerUserOrchestrator.register(user.getEmail(), user.getPassword())));
    }

    /**
     * This method performs user login operation
     *
     * @param credentials - credentials object that contains user's email and password
     * @return - in case of success a login object is returned that contains user id and a JWT token
     */
    @PostMapping(path = "/user/login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResult>  login(@RequestBody Credentials credentials) {

            return ResponseEntity.status(HttpStatus.CREATED).body(loginResultToLoginResultDTO.apply(
                    loginUserOrchestrator.login(credentials.getEmail(), credentials.getPassword())));

    }

    /**
     * This method searches for a user team by user id.
     *
     * @param id             - user id, should not be null. The authenticated user has to match the user id that is passed in request.
     * @param authentication current authentication
     * @return a team object
     */
    @GetMapping(path = "/user/{id}/team", consumes = MediaType.TEXT_HTML_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Team findUserTeam(@PathVariable String id, final Authentication authentication) {
        final Long decrypted = Long.valueOf(crypto.decrypt(id));

        if (!Objects.equals(decrypted, authentication.getPrincipal())) {
            throw new NotAuthorizedException(LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_TEAM_FOR_THIS_USER + id);

        }

        final com.toptal.soccer.model.Team userTeam = userManager.findUserTeam(decrypted);

        return teamToTeamDTO.apply(userTeam, teamManager.findTotalTeamValue(userTeam.getId()));
    }
}
