package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Transfer;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

/**
 * This class can transform Transfer domain object to DTOx
 */
public class TransferToTransferDTO implements Function<Transfer, com.toptal.soccer.dto.Transfer> {
    private final Crypto crypto;
    private final Function<Player, com.toptal.soccer.dto.Player> playerTransformer;

    public TransferToTransferDTO(Crypto crypto, Function<Player, com.toptal.soccer.dto.Player> playerTransformer) {
        this.crypto = crypto;
        this.playerTransformer = playerTransformer;
    }

    @Override
    public com.toptal.soccer.dto.Transfer apply(Transfer transfer) {

        Validate.notNull(transfer.getId(), Constants.ID_CAN_T_BE_NULL);

        final com.toptal.soccer.dto.Transfer dto = new com.toptal.soccer.dto.Transfer();

        // mask the id
        dto.setId(crypto.encrypt(transfer.getId().toString()));
        dto.setPlayer(playerTransformer.apply(transfer.getPlayer()));
        dto.setSellerId(crypto.encrypt(transfer.getSeller().getId().toString()));
        if (transfer.getBuyer() != null) {
            dto.setBuyerId(crypto.encrypt(transfer.getBuyer().getId().toString()));

        }


        return dto;
    }
}
