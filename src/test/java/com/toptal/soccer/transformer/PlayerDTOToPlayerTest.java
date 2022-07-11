package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class PlayerDTOToPlayerTest {
    private static final Long ID = 1L;
    public static final String COUNTRY = "country";
    public static final String FIRST = "first";
    public static final String LAST = "last";
    private PlayerDTOToPlayer playerDTOToPlayer;
    private Crypto crypto;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        crypto = (new AESCrypto("the key"));
        playerDTOToPlayer = new PlayerDTOToPlayer(crypto);
    }

    @Test
    public void testTransformsToPlayer() {
        // given
        final Player dto = new Player();
        dto.setCountry(COUNTRY);
        dto.setValue("$500");
        dto.setFirstName(FIRST);
        dto.setAge(18);
        dto.setLastName(LAST);
        dto.setType(Player.Type.ATTACKER);
        dto.setId(crypto.encrypt(ID.toString()));

        // when
        final com.toptal.soccer.model.Player transformed = playerDTOToPlayer.apply(dto);

        // then
        Assertions.assertEquals(COUNTRY, transformed.getCountry());
        Assertions.assertEquals(FIRST, transformed.getFirstName());
        Assertions.assertEquals(LAST, transformed.getLastName());
        Assertions.assertEquals(ID, transformed.getId());

        Assertions.assertNull(transformed.getDateOfBirth());
        Assertions.assertNull(transformed.getValue());
        Assertions.assertNull(transformed.getType());

    }

    @Test
    public void testThrowsErrorIfDTOIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> playerDTOToPlayer.apply(null));
    }

    @Test
    public void testThrowsErrorIfDTOIdIsNull() {

        // given
        final Player dto = new Player();
        dto.setCountry(COUNTRY);
        dto.setValue("$500");
        dto.setFirstName(FIRST);
        dto.setAge(18);
        dto.setLastName(LAST);
        dto.setType(Player.Type.ATTACKER);

        //then
        Assertions.assertThrows(NullPointerException.class, () -> playerDTOToPlayer.apply(dto));

    }

    @Test
    public void testThrowsErrorIfDTOCountryIsNull() {
        // given
        final Player dto = new Player();
        dto.setValue("$500");
        dto.setFirstName(FIRST);
        dto.setAge(18);
        dto.setLastName(LAST);
        dto.setType(Player.Type.ATTACKER);
        dto.setId(crypto.encrypt(ID.toString()));

        //then
        Assertions.assertThrows(NullPointerException.class, () -> playerDTOToPlayer.apply(dto));

    }

    @Test
    public void testThrowsErrorIfDTOFirstNameIsNull() {

        // given
        final Player dto = new Player();
        dto.setCountry(COUNTRY);
        dto.setValue("$500");
        dto.setAge(18);
        dto.setLastName(LAST);
        dto.setType(Player.Type.ATTACKER);
        dto.setId(crypto.encrypt(ID.toString()));

        //then
        Assertions.assertThrows(NullPointerException.class, () -> playerDTOToPlayer.apply(dto));

    }

    @Test
    public void testThrowsErrorIfDTOLastNameIsNull() {

        // given
        final Player dto = new Player();
        dto.setCountry(COUNTRY);
        dto.setValue("$500");
        dto.setFirstName(FIRST);
        dto.setAge(18);
        dto.setType(Player.Type.ATTACKER);
        dto.setId(crypto.encrypt(ID.toString()));

        // then
        Assertions.assertThrows(NullPointerException.class, () -> playerDTOToPlayer.apply(dto));

    }
}
