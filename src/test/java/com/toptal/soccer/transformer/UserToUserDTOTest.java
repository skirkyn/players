package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class UserToUserDTOTest {
    private static final Long ID = 1L;
    public static final String EMAIL = "email";
    public static final String NAME = "name";

    private UserToUserDTO userToUserDTO;
    private Crypto crypto;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        crypto = (new AESCrypto("the key"));
        userToUserDTO = new UserToUserDTO(crypto);
    }

    @Test
    public void testTransformsToUser() {
        // given
        final User model = new User();

        model.setEmail(EMAIL);

        model.setId(ID);

        final com.toptal.soccer.dto.Player dto = new com.toptal.soccer.dto.Player();


        // when
        final com.toptal.soccer.dto.User transformed = userToUserDTO.apply(model);


        // then
        Assertions.assertEquals(crypto.encrypt(ID.toString()), transformed.getId());
        Assertions.assertEquals(EMAIL,  transformed.getEmail());

    }

    @Test
    public void testThrowsAnErrorIdDTOIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> userToUserDTO.apply(new User()));
    }
}
