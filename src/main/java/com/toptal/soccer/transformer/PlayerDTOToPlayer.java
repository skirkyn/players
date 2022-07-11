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
 * This class can transform Player DTO to the domain object
 */
public class PlayerDTOToPlayer implements Function<com.toptal.soccer.dto.Player, Player> {
    private final Crypto crypto;

    public PlayerDTOToPlayer(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public Player apply(com.toptal.soccer.dto.Player dto) {

        Validate.notNull(dto, Constants.DTO_CAN_T_BE_NULL);
        Validate.notNull(dto.getId(), Constants.ID_CAN_T_BE_NULL);
        Validate.notEmpty(dto.getCountry(), Constants.COUNTRY_CAN_T_BE_EMPTY);
        Validate.notEmpty(dto.getFirstName(), Constants.FIRST_NAME_CAN_T_BE_EMPTY);
        Validate.notEmpty(dto.getLastName(),  Constants.LAST_NAME_CAN_T_BE_EMPTY);


        final Player player = new Player();

        player.setId(Long.valueOf(crypto.decrypt(dto.getId())));
        player.setCountry(dto.getCountry());
        player.setFirstName(dto.getFirstName());
        player.setLastName(dto.getLastName());

        return player;
    }
}
