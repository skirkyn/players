package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Team;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.util.function.BiFunction;

/**
 * This class can transform Team domain object to DTOx
 */
public class TeamToTeamDTO implements BiFunction<Team, BigDecimal, com.toptal.soccer.dto.Team> {
    private final Crypto crypto;

    public TeamToTeamDTO(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public com.toptal.soccer.dto.Team apply(Team team, BigDecimal teamValue) {
        Validate.notNull(team.getId(), Constants.ID_CAN_T_BE_NULL);

        final com.toptal.soccer.dto.Team dto = new com.toptal.soccer.dto.Team();


        // mask the id
        dto.setId(crypto.encrypt(team.getId().toString()));

        dto.setValue(team.getBudgetCurrency().getSign() + teamValue);
        dto.setName(team.getName());
        dto.setCountry(team.getCountry());
        dto.setBudget(team.getBudgetCurrency().getSign() + team.getBudget());
        dto.setNumberOfPlayers(team.getPlayers().size());


        return dto;
    }
}
