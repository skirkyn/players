package com.toptal.soccer.repo;

import com.toptal.soccer.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.toptal.soccer.TestUtil.generateUserWithTeam;


public class UserRepoTest extends BaseRepoTest{

    @Autowired
    private UserRepo repo;


    @Test
    @Transactional
    public void testFindsByEmail(){

        // given
        final User user = save(generateUserWithTeam(() -> null));
        Assertions.assertNull(user.getId());

        // then
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(user, repo.findByEmail(user.getEmail()).get());

    }

    @Test
    @Transactional
    public void testFindsTheTeam(){

        // given
        final User user = save(generateUserWithTeam(() -> null));

        // then
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(user.getTeam(), repo.findTeamByUserId(user.getId()).get());

    }





}
