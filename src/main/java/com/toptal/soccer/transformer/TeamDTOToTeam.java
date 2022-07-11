package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Team;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

/**
 * This class can transform Team DTO to the domain object
 */
public class TeamDTOToTeam implements Function<com.toptal.soccer.dto.Team, Team> {

    private final Crypto crypto;

    public TeamDTOToTeam(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public Team apply(com.toptal.soccer.dto.Team dto) {
        Validate.notNull(dto, Constants.DTO_CAN_T_BE_NULL);
        Validate.notNull(dto.getId(), Constants.ID_CAN_T_BE_NULL);
        Validate.notEmpty(dto.getCountry(), Constants.COUNTRY_CAN_T_BE_EMPTY);
        Validate.notEmpty(dto.getName(), Constants.NAME_CAN_T_BE_EMPTY);


        final Team team = new Team();

        team.setId(Long.valueOf(crypto.decrypt(dto.getId())));
        team.setCountry(dto.getCountry());
        team.setName(dto.getName());

        return team;
    }
}
