package com.toptal.soccer.manager;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.repo.PlayerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayerManagerImplTest {
    public static final long ID = 1L;
    private PlayerRepo playerRepo;
    private PlayerManagerImpl playerManager;

    @BeforeEach
    public void setUp(){
        playerRepo = mock(PlayerRepo.class);
        playerManager = new PlayerManagerImpl(playerRepo);
    }

    @Test
    public void testFindsByIdIfIdIsNotNull(){
        // when
        playerManager.findById(ID);
        // then
        verify(playerRepo).findById(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> playerManager.findById(null));
    }

    @Test
    public void testSavesIfPlayerNotNull(){
        // when
        final Player player = new Player();
        playerManager.save(player);
        // then
        verify(playerRepo).save(eq(player));
    }

    @Test
    public void testGeneratesErrorIfPlayerIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> playerManager.save(null));
    }

}
