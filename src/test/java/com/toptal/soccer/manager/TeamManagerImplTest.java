package com.toptal.soccer.manager;

import com.toptal.soccer.model.Team;
import com.toptal.soccer.repo.TeamRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeamManagerImplTest {
    public static final long ID = 1L;
    private TeamRepo teamRepo;
    private TeamManagerImpl teamManager;

    @BeforeEach
    public void setUp() {
        teamRepo = mock(TeamRepo.class);
        teamManager = new TeamManagerImpl(teamRepo);
    }

    @Test
    public void testFindsByIdIfIdIsNotNull() {
        // when
        teamManager.findById(ID);
        // then
        verify(teamRepo).findById(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfIdIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> teamManager.findById(null));
    }

    @Test
    public void testSavesIfPlayerNotNull() {
        // when
        final Team team = new Team();
        teamManager.save(team);
        // then
        verify(teamRepo).save(eq(team));
    }

    @Test
    public void testGeneratesErrorIfPlayerIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> teamManager.save(null));
    }

    @Test
    public void testFindsAllIfAllParamsValid() {
        // when
        when(teamRepo.findPlayers(eq(ID), any())).thenReturn(Page.empty());
        teamManager.findPlayersByTeamId(ID, 0, 10);
        // then
        verify(teamRepo).findPlayers(eq(ID), any());
    }

    @Test
    public void testGeneratesErrorIfIdIsNullForFindAll() {
        Assertions.assertThrows(NullPointerException.class, () -> teamManager.findPlayersByTeamId(null, 0, 10));
    }

    @Test
    public void testGeneratesErrorIfPagingParamsAreWrongForFindAll() {
        Assertions.assertThrows(NullPointerException.class, () -> teamManager.findPlayersByTeamId(null, 10, 0));
        Assertions.assertThrows(NullPointerException.class, () -> teamManager.findPlayersByTeamId(null, -1, 1));

    }
}
