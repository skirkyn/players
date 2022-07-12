package com.toptal.soccer.manager;

import com.toptal.soccer.model.Team;
import com.toptal.soccer.model.User;
import com.toptal.soccer.repo.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserManagerImplTest {
    public static final long ID = 1L;
    public static final String EMAIL = "email";
    private UserRepo userRepo;
    private UserManagerImpl userManager;

    @BeforeEach
    public void setUp(){
        userRepo = mock(UserRepo.class);
        userManager = new UserManagerImpl(userRepo);
    }

    @Test
    public void testFindsByIdIfIdIsNotNull(){
        // when
        userManager.findById(ID);
        // then
        verify(userRepo).findById(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userManager.findById(null));
    }

    @Test
    public void testSavesIfUserNotNull(){
        // when
        final User user = new User();
        userManager.save(user);
        // then
        verify(userRepo).save(eq(user));
    }

    @Test
    public void testGeneratesErrorIfUserIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userManager.save(null));
    }

    @Test
    public void testFindsByEmailIfEmailIsNotNull(){
        // when
        userManager.findUserByEmail(EMAIL);
        // then
        verify(userRepo).findByEmail(eq(EMAIL));
    }

    @Test
    public void testGeneratesErrorIfEmailIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userManager.findUserByEmail(null));
    }


    @Test
    public void testFindsTeamByIdIfIdIsNotNull(){
        // when
        when(userRepo.findTeamByUserId(eq(ID))).thenReturn(Optional.of(new Team()));
        userManager.findUserTeam(ID);
        // then
        verify(userRepo).findTeamByUserId(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfUserIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userManager.findByTeamId(null));
    }

    @Test
    public void testGeneratesErrorIfUserDoesntHaveTeam(){
        Assertions.assertThrows(IllegalStateException.class, () -> userManager.findUserTeam(ID));
    }

    @Test
    public void testFindsByTeamIdIfTeamIdIsNotNull(){
        // when
        userManager.findByTeamId(ID);
        // then
        verify(userRepo).findByTeamId(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfTeamIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userManager.findUserByEmail(null));
    }

    @Test
    public void testFindsByPlayerIdIfPlayerIdIsNotNull(){
        // when
        userManager.findByPlayerId(ID);
        // then
        verify(userRepo).findByPlayerId(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfPlayerIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userManager.findByPlayerId(null));
    }

    @Test
    public void testFindsByTransferIdIfPlayerIdIsNotNull(){
        // when
        userManager.findByPlayerId(ID);
        // then
        verify(userRepo).findByPlayerId(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfTransferIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userManager.findByTransferId(null));
    }

}
