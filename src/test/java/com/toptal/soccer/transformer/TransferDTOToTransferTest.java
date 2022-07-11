package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.Transfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class TransferDTOToTransferTest {
    private static final Long ID = 1L;
    private static final Long SELLER_ID = 2L;
    private static final Long PLAYER_ID = 2L;
    private static final Long BUYER_ID = 2L;

    private TransferDTOToTransfer transferDTOToTransfer;
    private Crypto crypto;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        crypto = (new AESCrypto("the key"));
        transferDTOToTransfer = new TransferDTOToTransfer(crypto);
    }

    @Test
    public void testTransformsToTransfer() {
        // given
        final Transfer dto = new Transfer();
        dto.setBuyerId(crypto.encrypt(BUYER_ID.toString()));
        dto.setSellerId(crypto.encrypt(BUYER_ID.toString()));
        final Player player = new Player();
        player.setId(crypto.encrypt(PLAYER_ID.toString()));
        dto.setPlayer(player);
        dto.setId(crypto.encrypt(ID.toString()));

        // when
        final com.toptal.soccer.model.Transfer transformed = transferDTOToTransfer.apply(dto);

        // then
        Assertions.assertEquals(BUYER_ID, transformed.getBuyer().getId());
        Assertions.assertEquals(SELLER_ID, transformed.getSeller().getId());
        Assertions.assertEquals(PLAYER_ID, transformed.getPlayer().getId());
        Assertions.assertEquals(ID, transformed.getId());

    }

    @Test
    public void testThrowsErrorIfDTOIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> transferDTOToTransfer.apply(null));
    }

    @Test
    public void testThrowsErrorIfDTOPlayerIsNull() {
        // given
        final Transfer dto = new Transfer();
        dto.setBuyerId(crypto.encrypt(BUYER_ID.toString()));
        dto.setSellerId(crypto.encrypt(BUYER_ID.toString()));
        dto.setId(crypto.encrypt(ID.toString()));

        //then
        Assertions.assertThrows(NullPointerException.class, () -> transferDTOToTransfer.apply(dto));

    }

    @Test
    public void testThrowsErrorIfDTOPlayerIdIsNull() {

        // given
        final Transfer dto = new Transfer();
        dto.setBuyerId(crypto.encrypt(BUYER_ID.toString()));
        dto.setSellerId(crypto.encrypt(BUYER_ID.toString()));

        dto.setId(crypto.encrypt(ID.toString()));

        //then
        Assertions.assertThrows(NullPointerException.class, () -> transferDTOToTransfer.apply(dto));

    }

    @Test
    public void testThrowsErrorIfDTOSellerIdIsNull() {

        // given
        final Transfer dto = new Transfer();
        dto.setBuyerId(crypto.encrypt(BUYER_ID.toString()));
        final Player player = new Player();
        player.setId(crypto.encrypt(PLAYER_ID.toString()));
        dto.setPlayer(player);
        dto.setId(crypto.encrypt(ID.toString()));

        // then
        Assertions.assertThrows(NullPointerException.class, () -> transferDTOToTransfer.apply(dto));

    }

    @Test
    public void testTransformsToTransferIfNoBuyer() {
        // given
        final Transfer dto = new Transfer();
        dto.setSellerId(crypto.encrypt(BUYER_ID.toString()));
        final Player player = new Player();
        player.setId(crypto.encrypt(PLAYER_ID.toString()));
        dto.setPlayer(player);
        dto.setId(crypto.encrypt(ID.toString()));

        // when
        final com.toptal.soccer.model.Transfer transformed = transferDTOToTransfer.apply(dto);

        // then
        Assertions.assertNull(transformed.getBuyer());
        Assertions.assertEquals(SELLER_ID, transformed.getSeller().getId());
        Assertions.assertEquals(PLAYER_ID, transformed.getPlayer().getId());
        Assertions.assertEquals(ID, transformed.getId());

    }

}
