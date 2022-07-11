package com.toptal.soccer.repo;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.toptal.soccer.TestUtil.generateUserWithTeam;

public class TeamRepoTest extends BaseRepoTest{
    @Autowired
    private TeamRepo repo;


    @Test
    @Transactional
    public void testFindsPlayersAndPages(){
        // given
        final User user = save(generateUserWithTeam(() -> null));

        final Long teamId = user.getTeam().getId();
        final int totalPlayers = user.getTeam().getPlayers().size();


        final List<Player> firstPortion = repo.findPlayers(teamId, PageRequest.of(0, totalPlayers)).stream().toList();
        Assertions.assertEquals(totalPlayers, firstPortion.size());


        Assertions.assertTrue(repo.findPlayers(teamId, PageRequest.of(1, totalPlayers)).isEmpty());

    }
}
