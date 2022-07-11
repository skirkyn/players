package com.toptal.soccer.orchestrator;

import com.github.javafaker.Name;
import com.toptal.soccer.model.Currency;
import com.toptal.soccer.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateNewPlayerOrchestratorImplTest{

    @Test
    public void testGeneratesPlayer(){
        // given
        final Name name = mock(Name.class);
        final String first = "first";
        final String last = "last";
        final LocalDateTime now = LocalDateTime.now();
        final String country = "country";
        final BigDecimal defaultValue = BigDecimal.valueOf(100000);
        // when

        when(name.firstName()).thenReturn(first);
        when(name.lastName()).thenReturn(last);

        final CreateNewPlayerOrchestratorImpl createNewPlayerOrchestrator = new CreateNewPlayerOrchestratorImpl(
                () -> name,
                () -> now,
                () -> country,
                defaultValue

        );

        final Player player = createNewPlayerOrchestrator.create(Player.Type.ATTACKER);

        //then
        Assertions.assertEquals(defaultValue, player.getValue());
        Assertions.assertEquals(Currency.DOLLAR, player.getValueCurrency());
        Assertions.assertEquals(country, player.getCountry());
        Assertions.assertEquals(first, player.getFirstName());
        Assertions.assertEquals(last, player.getLastName());
        Assertions.assertEquals(now.atZone(UTC).toInstant().getEpochSecond(), player.getDateOfBirth());

    }
}
