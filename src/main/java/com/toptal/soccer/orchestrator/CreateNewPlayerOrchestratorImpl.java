package com.toptal.soccer.orchestrator;

import com.github.javafaker.Name;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import com.toptal.soccer.orchestrator.iface.CreateNewPlayerOrchestrator;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import static java.time.ZoneOffset.UTC;

/**
 * This class can create a new player with default settings
 */
public class CreateNewPlayerOrchestratorImpl implements CreateNewPlayerOrchestrator {

    private Supplier<Name> fullNameGenerator;
    private Supplier<LocalDateTime> dobGenerator;
    private Supplier<String> countryGenerator;
    private BigDecimal defaultValue;

    public CreateNewPlayerOrchestratorImpl(Supplier<Name> fullNameGenerator,
                                           Supplier<LocalDateTime> dobGenerator,
                                           Supplier<String> countryGenerator,
                                           BigDecimal defaultValue) {
        this.fullNameGenerator = fullNameGenerator;
        this.dobGenerator = dobGenerator;
        this.countryGenerator = countryGenerator;
        this.defaultValue = defaultValue;
    }

    @Override
    public Player create( Player.Type type) {

        Validate.notNull(type, Constants.PLAYER_TYPE_CAN_T_BE_NULL);
        final Player player = new Player();
        final Name name = fullNameGenerator.get();
        player.setFirstName(name.firstName());
        player.setLastName(name.lastName());
        player.setDateOfBirth(dobGenerator.get().atZone(UTC).toInstant().getEpochSecond());
        player.setCountry(countryGenerator.get());
        player.setType(type);
        player.setValue(defaultValue);
        return player;
    }
}
