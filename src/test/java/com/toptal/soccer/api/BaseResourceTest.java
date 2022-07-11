package com.toptal.soccer.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toptal.soccer.dto.Credentials;
import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.Team;
import com.toptal.soccer.dto.Transfer;
import com.toptal.soccer.dto.User;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseResourceTest {

    protected static final String EMAIL = "email";
    protected static final String OTHER_EMAIL = "other_email";
    protected static final String PASSWORD = "password";

    protected static final int TEN = 10;
    protected static final String TEN_STR = "10";
    @Autowired
    protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected ResultActions createUser(final String email, final String password) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new User(null, new Credentials(email, password)))));
    }

    protected User createAndReturnUser(final String email, final String password) throws Exception {
        byte[] userCreateResponseFirst = createUser(email, password)
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse().getContentAsByteArray();

        return objectMapper.readValue(userCreateResponseFirst, User.class);
    }


    protected ResultActions getUser(final String userId, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/user/" + userId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected User getUserAndReturnResult(final String userId, final String token) throws Exception {
        final byte[] userResponse = mockMvc.perform(MockMvcRequestBuilders.get("/user/" + userId)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(userResponse, User.class);

    }

    protected ResultActions login(final String email, final String password) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/user/login")

                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(new Credentials(email, password))));
    }

    protected LoginResult loginAndReturnResult(final String email, final String password) throws Exception {

        byte[] loginResponse = login(email, password).andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(loginResponse, LoginResult.class);

    }

    protected ResultActions getTeam(final String userId, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/user/" + userId + "/team")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected Team getTeamAndReturnResult(final String userId, final String token) throws Exception {
        byte[] teamResponse = getTeam(userId, token).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(teamResponse, Team.class);
    }

    protected ResultActions updateTeam(final Team team, final String teamId, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/team/" + teamId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(team)));
    }

    protected Team updateTeamAndReturnResult(final Team team, final String userId, final String token) throws Exception {
        byte[] teamResponse = updateTeam(team, userId, token).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(teamResponse, Team.class);
    }

    protected ResultActions getPlayers(final String teamId, final int pageSize, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/team/" + teamId + "/players?size=" + pageSize)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }


    protected List<Player> getPlayersAndReturnList(final String teamId, final int pageSize, final String token) throws Exception {
        byte[] players = getPlayers(teamId, pageSize, token).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(players, new TypeReference<List<Player>>() {
        });
    }

    protected ResultActions updatePlayer(final Player player, final String playerId, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/player/" + playerId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(player)));
    }

    protected Player updatePlayerAndReturnResult(final Player player, final String playerId, final String token) throws Exception {
        byte[] playerResponse = updatePlayer(player, playerId, token).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(playerResponse, Player.class);
    }


    protected ResultActions getPlayer(final String playerId, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/player/" + playerId)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    protected Player getPlayerAndReturnResult( final String playerId, final String token) throws Exception {
        byte[] playerResponse = getPlayer(playerId, token).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(playerResponse, Player.class);
    }

    protected ResultActions addTransfer(final Transfer transfer, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post("/transfer/")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(transfer)));
    }

    protected Transfer addTransferAndReturnResult(final Transfer transfer, final String token) throws Exception {
        byte[] transferResponse = addTransfer(transfer, token).andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(transferResponse, Transfer.class);
    }


    protected ResultActions completeTransfer(final String transferId, final String toUserId, final String token) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.patch(String.format("/transfer/%s/to/%s", transferId, toUserId))
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    protected Transfer completeTransferAndReturnResult( final String transferId, final String toUserId, final String token) throws Exception {
        byte[] transferResponse = completeTransfer(transferId, toUserId, token).andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(transferResponse, Transfer.class);
    }


    protected ResultActions getTransfers(final String token, final int pageSize) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get("/transfer?size=" + pageSize)
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }


    protected List<Transfer> getTransfersAndReturnList(final String token, final int pageSize) throws Exception {
        byte[] transfers = getTransfers( token, pageSize).andExpect(status().isOk()).andReturn().getResponse().getContentAsByteArray();
        return objectMapper.readValue(transfers, new TypeReference<List<Transfer>>() {
        });
    }

    protected List<Player> getPlayers(User firstUser, LoginResult loginResultResultFirst) throws Exception {
        // find first user's team
        Team firstUsersTeam = getTeamAndReturnResult(firstUser.getId(), loginResultResultFirst.getToken());

        //
        List<Player> players = getPlayersAndReturnList(firstUsersTeam.getId(), 20, loginResultResultFirst.getToken());

        Assertions.assertNotNull(players);

        return players;
    }
    protected Player getFirstPlayer(User firstUser, LoginResult loginResultResultFirst) throws Exception {
        // find first user's team
        Team firstUsersTeam = getTeamAndReturnResult(firstUser.getId(), loginResultResultFirst.getToken());

        //
        List<Player> players = getPlayersAndReturnList(firstUsersTeam.getId(), 20, loginResultResultFirst.getToken());

        Assertions.assertNotNull(players);
        Assertions.assertNotEquals(0, players.size());

        Player player = players.get(0);
        return player;
    }
}
