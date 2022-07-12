package com.toptal.soccer.api;

import com.toptal.soccer.SoccerApplication;
import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.Transfer;
import com.toptal.soccer.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = { SoccerApplication.class })
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransferListrResourceTest extends BaseResourceTest {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ANOTHER_NAME = "another_name_";
    public static final String ANOTHER_COUNTRY = "another_country_";
    public static final double BIG_MONEY = 15000000.0;
    public static final String TRANSFER_PRICE = "$123";


    @Test
    public void testAuthorizedUserCanSubmitTransfer() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        Transfer toAdd = new Transfer(null,  player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());
        Assertions.assertNotNull(transfer.getId());
    }

    @Test
    public void testNonAuthorizedUserCantSubmitTransfer() throws Exception {
        User firstUser = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(firstUser.getId());


        // log in with the second user
        LoginResult firstLoginResultResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(firstUser, firstLoginResultResult);

        User secondUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(secondUser.getId());


        // log in with the second user
        LoginResult secondLoginResultResult = loginAndReturnResult(EMAIL, PASSWORD);
        Transfer toAdd = new Transfer(null,  player, secondUser.getId(), null);
        addTransfer(toAdd, secondLoginResultResult.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testNonAuthenticatedUserCantSubmitTransfer() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        Transfer toAdd = new Transfer(null,  player, user.getId(), null);


        addTransfer(toAdd, null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testAuthorizedUserCanCompleteTransfer() throws Exception {
        // first do all the steps to create a transfer

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNotNull(transfer.getSellerId());

        User anotherUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);

        LoginResult loginResultResultAnotherUser = loginAndReturnResult(OTHER_EMAIL, PASSWORD);
        // transferring to the "another" user so it's another user who is authorized to perform the transfer
        Transfer transferAfterCompletion = completeTransferAndReturnResult(transfer.getId(), anotherUser.getId(),
                loginResultResultAnotherUser.getToken());
        Assertions.assertNotNull(transferAfterCompletion.getBuyerId());

        Player afterTransfer = getPlayerAndReturnResult(player.getId(), loginResultResultAnotherUser.getToken());

        // verify that the players value raised
        Assertions.assertTrue(new BigDecimal(player.getValue().replaceAll("\\$", "")).compareTo(
                new BigDecimal(afterTransfer.getValue().replaceAll("\\$", ""))) < 0);

    }

    @Test
    public void testNonAuthorizedUserCantCompleteTransfer() throws Exception {
        // first do all the steps to create a transfer

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null,  player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNotNull(transfer.getSellerId());

        User anotherUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);

        loginAndReturnResult(OTHER_EMAIL, PASSWORD);
        // transferring to the "another" user so it's another user who is authorized to perform the transfer
        // it should fail for the seller token
        completeTransfer(transfer.getId(), anotherUser.getId(), loginResult.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void testAuthorizedUserCantCompleteTransferToThemselves() throws Exception {
        // first do all the steps to create a transfer

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null,  player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNotNull(transfer.getSellerId());

        // transfering to themselves can't work
        completeTransfer(transfer.getId(), user.getId(), loginResult.getToken()).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void testNonAuthenticatedUserCantCompleteTransfer() throws Exception {
        // first do all the steps to create a transfer

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null,  player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNotNull(transfer.getSellerId());

        User anotherUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);

        loginAndReturnResult(OTHER_EMAIL, PASSWORD);
        // transferring to the "another" user so it's another user who is authorized to perform the transfer
        // it should fail for an empty token
        completeTransfer(transfer.getId(), anotherUser.getId(), null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    public void testAuthorizedUserCanSeeAllTransfersIfPageSizeIsGreater() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null,  player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        createAndReturnUser(OTHER_EMAIL, PASSWORD);

        LoginResult loginResultResultAnotherUser = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        List<Transfer> transfers = getTransfersAndReturnList(loginResultResultAnotherUser.getToken(), 20);
        Assertions.assertNotNull(transfers);
        Assertions.assertEquals(1, transfers.size());
        Assertions.assertEquals(transfer.getId(), transfers.get(0).getId());
    }

    @Test
    public void testAuthorizedUserCanSeeCappedTransfersIfPageSizeIsLess() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        List<Player> players = getPlayers(user, loginResult);

        // for lesser money so newly registered user will have enough
        for (Player player : players) {
            Transfer toAdd = new Transfer(null,  player, user.getId(), null);
            addTransferAndReturnResult(toAdd, loginResult.getToken());
        }


        createAndReturnUser(OTHER_EMAIL, PASSWORD);

        LoginResult loginResultResultAnotherUser = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        List<Transfer> transfers = getTransfersAndReturnList(loginResultResultAnotherUser.getToken(), 3);
        Assertions.assertNotNull(transfers);
        Assertions.assertEquals(3, transfers.size());

    }


    @Test
    public void testNonAuthenticatedUserCantSeeAnyTransfers() throws Exception {
        // first do all the steps to create a transfer

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        LoginResult loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null,  player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNotNull(transfer.getSellerId());

        getTransfers(null, 20).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

}
