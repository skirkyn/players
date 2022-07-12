package com.toptal.soccer.api;

import com.toptal.soccer.SoccerApplication;
import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.Team;
import com.toptal.soccer.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = { SoccerApplication.class })
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TeamResourceTest extends BaseResourceTest {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ANOTHER_NAME = "another_name_";
    public static final String ANOTHER_COUNTRY = "another_country_";

    @Value("${team.default.budget:5000000.00}")
    private String defaultBudget;

    @Value("${team.default.player.count:20}")
    private int defaultPlayerCount;

    @Test
    public void testAuthorizedUserCanUpdateTeam() throws Exception {

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team

        Team team = getTeamAndReturnResult(user.getId(), loginResult.getToken());
        // create update request
        Team updated = getTeamForUpdate(team);

        Team updatedAfterRequest = updateTeamAndReturnResult(updated, user.getId(), loginResult.getToken());

        Assertions.assertEquals(team.getId(), updatedAfterRequest.getId());
        // check that the rest of the fields is still the same
        Assertions.assertEquals(team.getNumberOfPlayers(), updatedAfterRequest.getNumberOfPlayers());
        Assertions.assertEquals(team.getBudget(), updatedAfterRequest.getBudget());
        Assertions.assertEquals(team.getValue(), updatedAfterRequest.getValue());

        // check that name and country have changed
        Assertions.assertNotEquals(team.getName(), updatedAfterRequest.getName());
        Assertions.assertNotEquals(team.getCountry(), updatedAfterRequest.getCountry());

    }



    @Test
    public void testNonAuthorizedUserCantUpdatePlayer() throws Exception {

        User firstUser = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(firstUser.getId());

        User secondUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(secondUser.getId());

        LoginResult loginResultResultFirst = loginAndReturnResult(EMAIL, PASSWORD);
        Assertions.assertNotNull(loginResultResultFirst);

        LoginResult loginResultResultSecond = loginAndReturnResult(OTHER_EMAIL, PASSWORD);
        Assertions.assertNotNull(loginResultResultSecond);

        Team firstUsersTeam = getTeamAndReturnResult(firstUser.getId(), loginResultResultFirst.getToken());

        // create update request
        Team updated = getTeamForUpdate(firstUsersTeam);
        updateTeam(updated, firstUsersTeam.getId(), loginResultResultSecond.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testNonAuthenticatedUserCantUpdatePlayer() throws Exception {
        User firstUser = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(firstUser.getId());


        LoginResult loginResultResultFirst = loginAndReturnResult(EMAIL, PASSWORD);
        Assertions.assertNotNull(loginResultResultFirst);

        Team firstUsersTeam = getTeamAndReturnResult(firstUser.getId(), loginResultResultFirst.getToken());
        Team updated = getTeamForUpdate(firstUsersTeam);
        updateTeam(updated, firstUsersTeam.getId(), null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Test
    public void testAuthorizedUserCanReadAllPlayersIfSizeIsGreater() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());

        // log in with second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team

        Team team = getTeamAndReturnResult(user.getId(), loginResult.getToken());

        List<Player> players = getPlayersAndReturnList(team.getId(), defaultPlayerCount + TEN, loginResult.getToken());

        Assertions.assertEquals(defaultPlayerCount, players.size());
    }

    @Test
    public void testAuthorizedUserCanReadAllPlayersIfSizeIsLess() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());

        // log in with second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team

        Team team = getTeamAndReturnResult(user.getId(), loginResult.getToken());

        List<Player> players = getPlayersAndReturnList(team.getId(), defaultPlayerCount / 2, loginResult.getToken());

        Assertions.assertEquals(defaultPlayerCount / 2, players.size());
    }
    @Test
    public void testNonAuthorizedUserCantReadAllPlayers() throws Exception {
        User firstUser = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(firstUser.getId());

        // log in with second firstUser
        LoginResult loginResultResultFirst = loginAndReturnResult(EMAIL, PASSWORD);

        // find the firstUser's team
        Team team = getTeamAndReturnResult(firstUser.getId(), loginResultResultFirst.getToken());

        User secondUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(secondUser.getId());

        // log in with second firstUser
        LoginResult loginResultResultSecond = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        getPlayers(team.getId(), defaultPlayerCount + TEN, loginResultResultSecond.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testNonAuthenticatedUserCantReadAllPlayers() throws Exception {
        User firstUser = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(firstUser.getId());

        // log in with second firstUser
        LoginResult loginResultResultFirst = loginAndReturnResult(EMAIL, PASSWORD);

        // find the firstUser's team
        Team team = getTeamAndReturnResult(firstUser.getId(), loginResultResultFirst.getToken());

        getPlayers(team.getId(), defaultPlayerCount + TEN, null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    private Team getTeamForUpdate(Team team) {
        Team updated = new Team();
        // set the same id
        updated.setId(team.getId());
        // different name and country (and players)
        String newName = ANOTHER_NAME + team.getName();
        String newCountry = ANOTHER_COUNTRY + team.getCountry();

        updated.setName(newName);
        updated.setCountry(newCountry);
        // should not be updated
        updated.setBudget(TEN + team.getBudget());
        updated.setNumberOfPlayers(team.getNumberOfPlayers() + TEN);
        updated.setValue(TEN + team.getValue());
        return updated;
    }
}
