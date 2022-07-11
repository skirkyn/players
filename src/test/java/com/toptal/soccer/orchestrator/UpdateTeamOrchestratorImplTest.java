package com.toptal.soccer.orchestrator;

import com.toptal.soccer.TestUtil;
import com.toptal.soccer.manager.iface.TeamManager;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateTeamOrchestratorImplTest {
    public static final String COUNTRY = "country";
    public static final String COUNTRY_OTHER = "country_other";

    public static final String NAME = "NAME";

    public static final String NAME_OTHER = "NAME_other";

    public static final double GREATER_VAL = 500000.78;

    private static final Long ID = 1L;

    private final TeamManager teamManager = mock(TeamManager.class);

    private UpdateTeamOrchestratorImpl updateTeamOrchestrator;

    @BeforeEach
    public void setUp() {
        updateTeamOrchestrator = new UpdateTeamOrchestratorImpl(teamManager);
    }

    @Test
    public void testUpdatesNamesAndCountry() {
        // given
        final Team model = new Team();
        model.setCountry(COUNTRY);
        model.setBudget(BigDecimal.valueOf(GREATER_VAL));
        model.setPlayers(TestUtil.generatePlayers(Map.of(Player.Type.GOALKEEPER, 1)));
        model.setName(NAME);

        model.setId(ID);


        final Team forUpdate = new Team();
        forUpdate.setCountry(COUNTRY_OTHER);
        forUpdate.setBudget(BigDecimal.valueOf(50000.78));
        forUpdate.setPlayers(TestUtil.generatePlayers(Map.of(Player.Type.GOALKEEPER, 2)));
        forUpdate.setName(NAME_OTHER);
        forUpdate.setId(ID);

        Assertions.assertNotEquals(model.getPlayers().size(), forUpdate.getPlayers().size());
        Assertions.assertNotEquals(model.getBudget(), forUpdate.getBudget());

        Assertions.assertNotEquals(model.getName(), forUpdate.getName());
        Assertions.assertNotEquals(model.getCountry(), forUpdate.getCountry());




        // when
        doAnswer(returnsFirstArg()).when(teamManager).save(any());
        when(teamManager.findById(eq(ID))).thenReturn(Optional.of(model));

        final Team updated = updateTeamOrchestrator.update(forUpdate);

        Assertions.assertEquals(model.getPlayers().size(), updated.getPlayers().size());
        Assertions.assertEquals(model.getBudget(), updated.getBudget());


        Assertions.assertEquals(forUpdate.getName(), updated.getName());
        Assertions.assertEquals(forUpdate.getCountry(), updated.getCountry());

    }


    @Test
    public void testGeneratesErrorIfTeamIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> updateTeamOrchestrator.update(null));
    }

    @Test
    public void testGeneratesErrorIfTeamIdIsNull(){
        // given
        final Team model = new Team();
        model.setCountry(COUNTRY);
        model.setBudget(BigDecimal.valueOf(GREATER_VAL));
        model.setPlayers(TestUtil.generatePlayers(Map.of(Player.Type.GOALKEEPER, 1)));
        model.setName(NAME);


        // then
        Assertions.assertThrows(NullPointerException.class, () -> updateTeamOrchestrator.update(model));

    }

    @Test
    public void testGeneratesErrorIfNameIsEmpty(){
        // given
        final Team model = new Team();
        model.setCountry(COUNTRY);
        model.setBudget(BigDecimal.valueOf(GREATER_VAL));
        model.setPlayers(TestUtil.generatePlayers(Map.of(Player.Type.GOALKEEPER, 1)));
        model.setId(ID);

        // then
        Assertions.assertThrows(NullPointerException.class, () -> updateTeamOrchestrator.update(model));

    }


    @Test
    public void testGeneratesErrorIfCountryIsEmpty(){
        // given
        final Team model = new Team();
        model.setBudget(BigDecimal.valueOf(GREATER_VAL));
        model.setPlayers(TestUtil.generatePlayers(Map.of(Player.Type.GOALKEEPER, 1)));
        model.setName(NAME);

        model.setId(ID);

        // then
        Assertions.assertThrows(NullPointerException.class, () -> updateTeamOrchestrator.update(model));

    }
}
