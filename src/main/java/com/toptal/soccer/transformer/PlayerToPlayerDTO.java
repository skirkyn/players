package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Player;
import org.apache.commons.lang3.Validate;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.function.Function;

import static java.time.ZoneOffset.UTC;

/**
 * This class can transform Player domain object to DTOx
 */
public class PlayerToPlayerDTO implements Function<Player, com.toptal.soccer.dto.Player> {
    private final Crypto crypto;

    public PlayerToPlayerDTO(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public com.toptal.soccer.dto.Player apply(Player player) {
        Validate.notNull(player.getId(), Constants.ID_CAN_T_BE_NULL);

        final com.toptal.soccer.dto.Player dto = new com.toptal.soccer.dto.Player();

        // mask the id
        dto.setId(crypto.encrypt(player.getId().toString()));

        dto.setType(com.toptal.soccer.dto.Player.Type.valueOf(player.getType().name()));

        // find player's age
        dto.setAge(Period.between(player.getDateOfBirth().toLocalDate(),
                LocalDateTime.now(UTC).toLocalDate()).getYears());
        dto.setLastName(player.getLastName());
        dto.setFirstName(player.getFirstName());
        dto.setCountry(player.getCountry());

        // format the value
        dto.setValue(player.getValueCurrency().getSign()
                + player.getValue().setScale(2, RoundingMode.HALF_UP));


        return dto;
    }
}
