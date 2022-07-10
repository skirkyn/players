package com.toptal.soccer.api;

import com.toptal.soccer.dto.Login;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.Transfer;
import com.toptal.soccer.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@TestPropertySource("classpath:application-test.properties")
@WebMvcTest({UserResource.class, TeamResource.class, PlayerResource.class, TransferListResource.class})
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
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());
        Assertions.assertNotNull(transfer.getId());
    }

    @Test
    public void testNonAuthorizedUserCantSubmitTransfer() throws Exception {
        User firstUser = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(firstUser.getId());


        // log in with the second user
        Login firstLoginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(firstUser, firstLoginResult);

        User secondUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(secondUser.getId());


        // log in with the second user
        Login secondLoginResult = loginAndReturnResult(EMAIL, PASSWORD);
        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, firstUser.getId(), null);
        addTransfer(toAdd, secondLoginResult.getToken()).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testNonAuthenticatedUserCantSubmitTransfer() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);


        addTransfer(toAdd, null).andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));

    }

    @Test
    public void testAuthorizedUserCanCompleteTransfer() throws Exception {
        // first do all the steps to create a transfer

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNull(transfer.getSellerId());

        User anotherUser = createAndReturnUser(OTHER_EMAIL, PASSWORD);

        Login loginResultAnotherUser = loginAndReturnResult(OTHER_EMAIL, PASSWORD);
        // transferring to the "another" user so it's another user who is authorized to perform the transfer
        Transfer transferAfterCompletion = completeTransferAndReturnResult(transfer.getId(), anotherUser.getId(), loginResultAnotherUser.getToken());
        Assertions.assertNotNull(transferAfterCompletion.getBuyerId());
    }

    @Test
    public void testNonAuthorizedUserCantCompleteTransfer() throws Exception {
        // first do all the steps to create a transfer

        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNull(transfer.getSellerId());

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
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNull(transfer.getSellerId());

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
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNull(transfer.getSellerId());

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
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        createAndReturnUser(OTHER_EMAIL, PASSWORD);

        Login loginResultAnotherUser = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        List<Transfer> transfers = getTransfersAndReturnList(loginResultAnotherUser.getToken(), 20);
        Assertions.assertNotNull(transfers);
        Assertions.assertEquals(1, transfers.size());
        Assertions.assertEquals(transfer, transfers.get(0));
    }

    @Test
    public void testAuthorizedUserCanSeeCappedTransfersIfPageSizeIsLess() throws Exception {
        User user = createAndReturnUser(EMAIL, PASSWORD);
        // make sure it has been created
        Assertions.assertNotNull(user.getId());


        // log in with the second user
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        List<Player> players = getPlayers(user, loginResult);

        // for lesser money so newly registered user will have enough
        for (Player player : players) {
            Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
            addTransferAndReturnResult(toAdd, loginResult.getToken());
        }


        createAndReturnUser(OTHER_EMAIL, PASSWORD);

        Login loginResultAnotherUser = loginAndReturnResult(OTHER_EMAIL, PASSWORD);

        List<Transfer> transfers = getTransfersAndReturnList(loginResultAnotherUser.getToken(), 3);
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
        Login loginResult = loginAndReturnResult(EMAIL, PASSWORD);

        // find the user's team
        Player player = getFirstPlayer(user, loginResult);

        // for lesser money so newly registered user will have enough
        Transfer toAdd = new Transfer(null, TRANSFER_PRICE, player, user.getId(), null);
        Transfer transfer = addTransferAndReturnResult(toAdd, loginResult.getToken());

        Assertions.assertNull(transfer.getSellerId());

        getTransfers(null, 20).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    }

}
