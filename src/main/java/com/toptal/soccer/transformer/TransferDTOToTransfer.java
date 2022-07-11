package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.model.User;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

/**
 * This class can transform Transfer DTO to the domain object
 */
public class TransferDTOToTransfer implements Function<com.toptal.soccer.dto.Transfer, Transfer> {
    private final Crypto crypto;

    public TransferDTOToTransfer(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public Transfer apply(com.toptal.soccer.dto.Transfer dto) {

        Validate.notNull(dto, Constants.DTO_CAN_T_BE_NULL);
        Validate.notNull(dto.getPlayer(), Constants.DTO_CAN_T_BE_NULL);
        Validate.notNull(dto.getPlayer().getId(), Constants.ID_CAN_T_BE_NULL);
        Validate.notNull(dto.getSellerId(), Constants.ID_CAN_T_BE_NULL);

        final Transfer transfer = new Transfer();

        transfer.setId(dto.getId() == null ? null : Long.valueOf(crypto.decrypt(dto.getId())));

        final Player player = new Player();
        player.setId(Long.valueOf(crypto.decrypt(dto.getPlayer().getId())));

        transfer.setPlayer(player);

        final User seller = new User();
        seller.setId(Long.valueOf(crypto.decrypt(dto.getSellerId())));
        transfer.setSeller(seller);

        if(dto.getBuyerId() != null){
            final User buyer = new User();
            buyer.setId(Long.valueOf(crypto.decrypt(dto.getBuyerId())));
            transfer.setBuyer(buyer);
        }

        return transfer;
    }
}
