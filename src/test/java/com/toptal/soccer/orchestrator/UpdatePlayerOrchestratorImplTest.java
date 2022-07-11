package com.toptal.soccer.orchestrator;

import com.toptal.soccer.manager.iface.PlayerManager;
import com.toptal.soccer.model.Player;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdatePlayerOrchestratorImplTest {
    public static final String COUNTRY = "country";
    public static final String COUNTRY_OTHER = "country_other";

    public static final String FIRST = "first";
    public static final String LAST = "last";

    public static final String FIRST_OTHER = "first_other";
    public static final String LAST_OTHER = "last_other";
    private static final Long ID = 1L;
    public static final double GREATER_VAL = 500000.78;
    public static final LocalDateTime DATE_OF_BIRTH = LocalDateTime.of(1985, Month.APRIL, 5, 5, 0);

    private final PlayerManager playerManager = mock(PlayerManager.class);

    private UpdatePlayerOrchestratorImpl updatePlayerOrchestrator;

    @BeforeEach
    public void setUp() {
        updatePlayerOrchestrator = new UpdatePlayerOrchestratorImpl(playerManager);
    }

    @Test
    public void testUpdatesNamesAndCountry() {
        // given
        final Player model = new Player();
        model.setCountry(COUNTRY);
        model.setValue(BigDecimal.valueOf(GREATER_VAL));
        model.setFirstName(FIRST);
        model.setDateOfBirth(DATE_OF_BIRTH);
        model.setLastName(LAST);
        model.setType(Player.Type.ATTACKER);
        model.setId(ID);


        final Player forUpdate = new Player();
        forUpdate.setCountry(COUNTRY_OTHER);
        forUpdate.setValue(BigDecimal.valueOf(50000.78));
        forUpdate.setFirstName(FIRST_OTHER);
        forUpdate.setDateOfBirth(LocalDateTime.of(1983, Month.APRIL, 5, 5, 0));
        forUpdate.setLastName(LAST_OTHER);
        forUpdate.setType(Player.Type.GOALKEEPER);
        forUpdate.setId(ID);

        Assertions.assertNotEquals(model.getDateOfBirth(), forUpdate.getDateOfBirth());
        Assertions.assertNotEquals(model.getType(), forUpdate.getType());
        Assertions.assertNotEquals(model.getValue(), forUpdate.getValue());

        Assertions.assertNotEquals(model.getFirstName(), forUpdate.getFirstName());
        Assertions.assertNotEquals(model.getLastName(), forUpdate.getLastName());
        Assertions.assertNotEquals(model.getCountry(), forUpdate.getCountry());

        // when
        doAnswer(returnsFirstArg()).when(playerManager).save(any());
        when(playerManager.findById(eq(ID))).thenReturn(Optional.of(model));

        final Player updated = updatePlayerOrchestrator.update(forUpdate);

        Assertions.assertEquals(model.getDateOfBirth(), updated.getDateOfBirth());
        Assertions.assertEquals(model.getType(), updated.getType());
        Assertions.assertEquals(model.getValue(), updated.getValue());


        Assertions.assertEquals(forUpdate.getFirstName(), updated.getFirstName());
        Assertions.assertEquals(forUpdate.getLastName(), updated.getLastName());
        Assertions.assertEquals(forUpdate.getCountry(), updated.getCountry());

    }


    @Test
    public void testGeneratesErrorIfPlayerIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> updatePlayerOrchestrator.update(null));
    }

    @Test
    public void testGeneratesErrorIfPlayerIdIsNull(){
        // given
        final Player model = new Player();
        model.setCountry(COUNTRY);
        model.setValue(BigDecimal.valueOf(GREATER_VAL));
        model.setFirstName(FIRST);
        model.setDateOfBirth(DATE_OF_BIRTH);
        model.setLastName(LAST);
        model.setType(Player.Type.ATTACKER);

        // then
        Assertions.assertThrows(NullPointerException.class, () -> updatePlayerOrchestrator.update(model));

    }

    @Test
    public void testGeneratesErrorIfFirstNameIsEmpty(){
        // given
        final Player model = new Player();
        model.setCountry(COUNTRY);
        model.setValue(BigDecimal.valueOf(GREATER_VAL));
        model.setFirstName(StringUtils.EMPTY);
        model.setDateOfBirth(DATE_OF_BIRTH);
        model.setLastName(LAST);
        model.setType(Player.Type.ATTACKER);
        model.setId(ID);


        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> updatePlayerOrchestrator.update(model));

    }

    @Test
    public void testGeneratesErrorIfLastNameIsEmpty(){
        // given
        final Player model = new Player();
        model.setCountry(COUNTRY);
        model.setValue(BigDecimal.valueOf(GREATER_VAL));
        model.setFirstName(FIRST);
        model.setDateOfBirth(DATE_OF_BIRTH);
        model.setLastName(StringUtils.EMPTY);
        model.setType(Player.Type.ATTACKER);
        model.setId(ID);


        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> updatePlayerOrchestrator.update(model));

    }

    @Test
    public void testGeneratesErrorIfCountryIsEmpty(){
        // given
        final Player model = new Player();
        model.setCountry(StringUtils.EMPTY);
        model.setValue(BigDecimal.valueOf(GREATER_VAL));
        model.setFirstName(FIRST);
        model.setDateOfBirth(DATE_OF_BIRTH);
        model.setLastName(LAST);
        model.setType(Player.Type.ATTACKER);
        model.setId(ID);


        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> updatePlayerOrchestrator.update(model));

    }
}
