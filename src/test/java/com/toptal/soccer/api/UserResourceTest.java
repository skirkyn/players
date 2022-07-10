package com.toptal.soccer.api;

import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.dto.Team;
import com.toptal.soccer.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@TestPropertySource("classpath:application-test.properties")
@WebMvcTest(UserResource.class)
public class UserResourceTest extends BaseResourceTest {
    @Value("${team.default.budget:5000000}")
    private String defaultBudget;
    @Value("${team.default.player.count:20}")
    private int defaultPlayerCount;
    @Value("${team.default.player.value:1000000}")
    private String defaultPlayerValue;
    @Value("${game.default.currency:$}")
    private String defaultCurrency;


    @Test
    public void testUnregisteredUserCantLogin() throws Exception {
        // has to get unauthorized if not registered
        login(EMAIL, PASSWORD).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void testUserCanRegister() throws Exception {
        // successful registration with non-existing email
        createUser(EMAIL, PASSWORD).andExpect(status().is(HttpStatus.CREATED.value()));
        // has to be able to log in successfully if already registered
        login(EMAIL, PASSWORD).andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void testUserCantRegisterTheSameEmailTwice() throws Exception {
        // successful registration with non-existing email
        createUser(EMAIL, PASSWORD).andExpect(status().is(HttpStatus.CREATED.value()));
        // can't register if the email exists
        createUser(EMAIL, PASSWORD).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testUserCantRegisterIfTheEmailOrPasswordIsEmpty() throws Exception {
        // empty email registration fails
        createUser(null, PASSWORD).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        // empty password registration fails
        createUser(EMAIL, null).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    public void testAuthorizedUserCanReadUser() throws Exception {
        // create the user
        User created = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(created.getId());

        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);
        // perform get request
        Assertions.assertNotNull(getUser(created.getId(),  loginResult.getToken()));
    }

    @Test
    public void testNonAuthenticatedUserCantReadUser() throws Exception {
        // create the user
        User created = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(created.getId());

        // perform get request
        getUser(created.getId(), null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void testNonAuthorizedUserCantReadUser() throws Exception {

        User createdFirst = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(createdFirst.getId());

        User createdSecond = createAndReturnUser(OTHER_EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(createdSecond.getId());

        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        // perform get request for the first user and receive unauthorized
        getUser(createdFirst.getId(), loginResult.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testNonAuthenticatedUserCantReadNonExistingUser() throws Exception {
        // unregistered user can't have and read a team
        // perform get request
        getUser("na", null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testAuthorizedUserCanReadTeam() throws Exception {
        // create the user
        User created = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(created.getId());

        // log in
        LoginResult loginResult = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        // read the team
        Team team = getTeamAndReturnResult(created.getId(), loginResult.getToken());

        // make sure all the fields in the newly created team were correctly defaulted
        Assertions.assertNotNull(team.getId());
        Assertions.assertNotNull(team.getName());
        Assertions.assertNotNull(team.getCountry());
        Assertions.assertEquals(defaultCurrency + defaultBudget, team.getBudget());
        Assertions.assertEquals(defaultPlayerCount, team.getNumberOfPlayers());
        Assertions.assertEquals(defaultCurrency
                + new BigDecimal(defaultPlayerValue).multiply(
                        new BigDecimal(defaultPlayerCount))
                .setScale(2, RoundingMode.HALF_UP), team.getValue());

    }

    @Test
    public void testNonAuthorizedUserCanReadTeam() throws Exception {

        User createdFirst = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(createdFirst.getId());

        User createdSecond = createAndReturnUser(OTHER_EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(createdSecond.getId());

        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        // perform get request for the first user and receive unauthorized
        getTeam(createdFirst.getId(), loginResult.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }
}
