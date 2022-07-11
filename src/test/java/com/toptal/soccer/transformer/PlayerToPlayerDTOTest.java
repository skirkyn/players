package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.Month;

public class PlayerToPlayerDTOTest {
    private static final Long ID = 1L;
    public static final String COUNTRY = "country";
    public static final String FIRST = "first";
    public static final String LAST = "last";
    private PlayerToPlayerDTO playerToPlayerDTO;
    private Crypto crypto;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        crypto = (new AESCrypto("the key"));
        playerToPlayerDTO = new PlayerToPlayerDTO(crypto);
    }

    @Test
    public void testTransformsToPlayer() {
        // given
        final Player model = new Player();
        model.setCountry(COUNTRY);
        model.setValue(BigDecimal.valueOf(500000.78));
        model.setFirstName(FIRST);
        model.setDateOfBirth(LocalDateTime.of(1985, Month.APRIL, 5, 5, 0));
        model.setLastName(LAST);
        model.setType(Player.Type.ATTACKER);
        model.setId(ID);

        // when
        final com.toptal.soccer.dto.Player transformed = playerToPlayerDTO.apply(model);

        // then
        Assertions.assertEquals(COUNTRY, transformed.getCountry());
        Assertions.assertEquals(FIRST, transformed.getFirstName());
        Assertions.assertEquals(LAST, transformed.getLastName());
        Assertions.assertEquals(crypto.encrypt(ID.toString()), transformed.getId());

        Assertions.assertTrue(transformed.getAge() >= 37);
        Assertions.assertEquals("$500000.78", transformed.getValue());
        Assertions.assertEquals(com.toptal.soccer.dto.Player.Type.ATTACKER, transformed.getType());

    }
    @Test
    public void testThrowsAnErrorIdDTOIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> playerToPlayerDTO.apply(new Player()));
    }
}
