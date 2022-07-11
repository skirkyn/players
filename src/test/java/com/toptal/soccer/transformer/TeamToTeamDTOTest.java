package com.toptal.soccer.transformer;

import com.toptal.soccer.TestUtil;
import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class TeamToTeamDTOTest {
    private static final Long ID = 1L;
    public static final String COUNTRY = "country";
    public static final String NAME = "name";


    private TeamToTeamDTO teamToTeamDTO;
    private Crypto crypto;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        crypto = (new AESCrypto("the key"));
        teamToTeamDTO = new TeamToTeamDTO(crypto);
    }

    @Test
    public void testTransformsToTeam() {
        // given
        final Team model = new Team();

        model.setCountry(COUNTRY);
        model.setPlayers(TestUtil.generatePlayers(Map.of(
                Player.Type.GOALKEEPER, 1,
                Player.Type.DEFENDER, 1
        )));

        final BigDecimal total = model.getPlayers().stream().map(Player::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);

        model.setBudget(BigDecimal.valueOf(3000.67));
        model.setName(NAME);
        model.setId(ID);

        final com.toptal.soccer.dto.Player dto = new com.toptal.soccer.dto.Player();


        // when
        final com.toptal.soccer.dto.Team transformed = teamToTeamDTO.apply(model, total);


        // then
        Assertions.assertEquals(crypto.encrypt(ID.toString()), transformed.getId());
        Assertions.assertEquals(COUNTRY,  transformed.getCountry());
        Assertions.assertEquals(NAME, transformed.getName());
        Assertions.assertEquals("$3000.67", transformed.getBudget());
        Assertions.assertEquals("$" + total, transformed.getValue());
    }

    @Test
    public void testThrowsAnErrorIdDTOIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> teamToTeamDTO.apply(new Team(), BigDecimal.ZERO));
    }
}
