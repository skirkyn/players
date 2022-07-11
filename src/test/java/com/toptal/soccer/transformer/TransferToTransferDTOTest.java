package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.AESCrypto;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferToTransferDTOTest {
    private static final Long ID = 1L;
    private static final Long SELLER_ID = 2L;
    private static final Long PLAYER_ID = 3L;
    private static final Long BUYER_ID = 4L;

    private TransferToTransferDTO transferToTransferDTO;
    private PlayerToPlayerDTO playerToPlayerDTO;
    private Crypto crypto;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        crypto = (new AESCrypto("the key"));
        playerToPlayerDTO = mock(PlayerToPlayerDTO.class);
        transferToTransferDTO = new TransferToTransferDTO(crypto, playerToPlayerDTO);
    }

    @Test
    public void testTransformsToTransfer() {
        // given
        final Transfer model = new Transfer();

        final User buyer = new User();
        buyer.setId(BUYER_ID);
        model.setBuyer(buyer);

        final User seller = new User();
        seller.setId(SELLER_ID);
        model.setSeller(seller);

        final Player player = new Player();
        player.setId(PLAYER_ID);
        model.setPlayer(player);
        model.setId(ID);

        final com.toptal.soccer.dto.Player dto = new com.toptal.soccer.dto.Player();


        // when
        final com.toptal.soccer.dto.Transfer transformed = transferToTransferDTO.apply(model);
        when(playerToPlayerDTO.apply(eq(player))).thenReturn(dto);


        // then
        Assertions.assertEquals(crypto.encrypt(BUYER_ID.toString()), transformed.getBuyerId());
        Assertions.assertEquals(crypto.encrypt(SELLER_ID.toString()), transformed.getSellerId());
        Assertions.assertEquals(crypto.encrypt(ID.toString()), transformed.getId());
        Assertions.assertNull(transformed.getPlayer());

        verify(playerToPlayerDTO).apply(eq(player));
    }

    @Test
    public void testThrowsAnErrorIdDTOIdIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> transferToTransferDTO.apply(new Transfer()));
    }
}
