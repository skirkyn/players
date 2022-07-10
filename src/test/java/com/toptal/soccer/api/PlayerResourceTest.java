package com.toptal.soccer.api;

import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@TestPropertySource("classpath:application-test.properties")
@WebMvcTest({UserResource.class, TeamResource.class, PlayerResource.class})
public class PlayerResourceTest extends BaseResourceTest {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ANOTHER_NAME = "another_name_";
    public static final String ANOTHER_COUNTRY = "another_country_";


    @Test
    public void testAuthorizedUserCanUpdatePlayer() throws Exception {

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);
        // create update request
        Player updated = getPlayerForUpdate(player);

        Player updatedAfterRequest = updatePlayerAndReturnResult(updated, user.getId(), loginResult.getToken());

        Assertions.assertEquals(player.getId(), updatedAfterRequest.getId());
        // check that the rest of the fields is still the same
        Assertions.assertEquals(player.getAge(), updatedAfterRequest.getAge());
        Assertions.assertEquals(player.getType(), updatedAfterRequest.getType());
        Assertions.assertEquals(player.getValue(), updatedAfterRequest.getValue());

        // check that name and country have changed
        Assertions.assertNotEquals(player.getFirstName(), updatedAfterRequest.getFirstName());
        Assertions.assertNotEquals(player.getLastName(), updatedAfterRequest.getLastName());
        Assertions.assertNotEquals(player.getCountry(), updatedAfterRequest.getCountry());

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

        Player player = getFirstPlayer(firstUser, loginResultResultFirst);
        // create update request
        Player updated = getPlayerForUpdate(player);

        updatePlayer(updated, player.getId(), loginResultResultSecond.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));


    }


    @Test
    public void testNonAuthenticatedUserCanUpdatePlayer() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);
        updatePlayer(player, player.getId(), null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }


    @Test
    public void testAuthorizedUserCanReadPlayer() throws Exception {

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        getPlayer(player.getId(), loginResult.getToken()).andExpect(status().isOk());

    }


    @Test
    public void testNonAuthorizedUserCantReadPlayer() throws Exception {

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

        Player player = getFirstPlayer(firstUser, loginResultResultFirst);


        getPlayer(player.getId(), loginResultResultSecond.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));


    }


    @Test
    public void testNonAuthenticatedUserCantReadPlayer() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);
        getPlayer(player.getId(), null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }


    private Player getPlayerForUpdate( Player player) {
        Player updated = new Player();
        // set the same id
        updated.setId(player.getId());
        // different name and country
        String newFirstName = ANOTHER_NAME + player.getFirstName();
        String newLastName = ANOTHER_NAME + player.getLastName();

        String newCountry = ANOTHER_COUNTRY + player.getCountry();

        updated.setFirstName(newFirstName);
        updated.setLastName(newLastName);
        updated.setCountry(newCountry);
        // should not be updated
        updated.setAge(TEN + player.getAge());
        updated.setType(Player.Type.values()[Player.Type.values().length - 1 - player.getType().ordinal()]);
        updated.setValue(TEN + player.getValue());
        return updated;
    }

}
