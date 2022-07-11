package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class TeamDTOToTeamrTest {
    private static final Long ID = 1L;

    public static final String COUNTRY = "country";
    public static final String NAME = "name";
    public static final String _500 = "$500";

    private TeamDTOToTeam teamDTOToTeam;
    private Crypto crypto;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        crypto = (new AESCrypto("the key"));
        teamDTOToTeam = new TeamDTOToTeam(crypto);
    }

    @Test
    public void testTransformsToTeam() {
        // given
        final Team dto = new Team();
        dto.setCountry(COUNTRY);
        dto.setValue(_500);
        dto.setNumberOfPlayers(18);
        dto.setName(NAME);
        dto.setBudget(_500);
        dto.setId(crypto.encrypt(ID.toString()));

        // when
        final com.toptal.soccer.model.Team transformed = teamDTOToTeam.apply(dto);

        // then
        Assertions.assertEquals(COUNTRY, transformed.getCountry());
        Assertions.assertEquals(NAME, transformed.getName());
        Assertions.assertEquals(ID, transformed.getId());

    }

    @Test
    public void testThrowsErrorIfDTOIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> teamDTOToTeam.apply(null));
    }

    @Test
    public void testThrowsErrorIfDTOIdIsNull() {
        // given
        final Team dto = new Team();
        dto.setCountry(COUNTRY);
        dto.setValue(_500);
        dto.setNumberOfPlayers(18);
        dto.setName(NAME);
        dto.setBudget(_500);

        //then
        Assertions.assertThrows(NullPointerException.class, () -> teamDTOToTeam.apply(dto));

    }

    @Test
    public void testThrowsErrorIfDTOCountryIsNull() {

        // given
        final Team dto = new Team();

        dto.setValue(_500);
        dto.setNumberOfPlayers(18);
        dto.setName(NAME);
        dto.setBudget(_500);
        dto.setId(crypto.encrypt(ID.toString()));


        //then
        Assertions.assertThrows(NullPointerException.class, () -> teamDTOToTeam.apply(dto));

    }

    @Test
    public void testThrowsErrorIfDTONameIsNull() {

        // given
        final Team dto = new Team();
        dto.setCountry(COUNTRY);
        dto.setValue(_500);
        dto.setNumberOfPlayers(18);
        dto.setBudget(_500);
        dto.setId(crypto.encrypt(ID.toString()));

        // then
        Assertions.assertThrows(NullPointerException.class, () -> teamDTOToTeam.apply(dto));

    }


}
