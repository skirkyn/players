package com.toptal.soccer.orchestrator;

import com.toptal.soccer.generator.Util;
import com.toptal.soccer.manager.iface.PlayerManager;
import com.toptal.soccer.manager.iface.TransferManager;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.model.User;
import com.toptal.soccer.orchestrator.iface.CompleteTransferOrchestrator;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.Validate;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class can complete existing transfer
 */
public class CompleteTransferOrchestratorImpl implements CompleteTransferOrchestrator {

    private final TransferManager transferManager;
    private final UserManager userManager;

    public CompleteTransferOrchestratorImpl(TransferManager transferManager,
                                            UserManager userManager,
                                            PlayerManager playerManager,
                                            int playerValueIncrementPercentMin,
                                            int playerValueIncrementPercentMax) {
        this.transferManager = transferManager;
        this.userManager = userManager;
        this.playerManager = playerManager;
        this.playerValueIncrementPercentMin = playerValueIncrementPercentMin;
        this.playerValueIncrementPercentMax = playerValueIncrementPercentMax;
    }

    private final PlayerManager playerManager;
    private final int playerValueIncrementPercentMin;
    private final int playerValueIncrementPercentMax;

    @Override
    @Transactional
    public Transfer complete(Long transferId, Long buyerId) {

        Validate.notNull(buyerId, Constants.BUYER_ID_CAN_T_BE_NULL);
        Validate.notNull(transferId, Constants.BUYER_ID_CAN_T_BE_NULL);

        final Transfer existingTransfer = transferManager.findById(transferId).filter(t -> t.getBuyer() == null)
                .orElseThrow(() -> new IllegalArgumentException(Constants.TRANSFER_ALREADY_COMPLETED));
        final Player playerForSale = existingTransfer.getPlayer();
        final User seller = existingTransfer.getSeller();

        final User buyer = userManager.findById(buyerId).filter(u -> !u.getId().equals(seller.getId()))
                .orElseThrow(() -> new IllegalArgumentException(Constants.USER_CAN_T_SELL_THE_PLAYER_TO_THEMSELVES));

        if (buyer.getTeam().getBudget().compareTo(existingTransfer.getPrice()) < 0) {
            throw new IllegalArgumentException(Constants.NOT_ENOUGH_MONEY);
        }

        if (existingTransfer.getValueCurrency() != seller.getTeam().getBudgetCurrency()
                || existingTransfer.getValueCurrency() != buyer.getTeam().getBudgetCurrency()
                || existingTransfer.getValueCurrency() != playerForSale.getValueCurrency()) {
            throw new IllegalArgumentException(Constants.ONLY_ONE_CURRENCY_IS_SUPPORTED);
        }
        int percentOfIncrease = Util.randomFromRange(playerValueIncrementPercentMin,
                playerValueIncrementPercentMax);

        final BigDecimal playerNewValue = BigDecimal.valueOf(percentOfIncrease)
                .divide(BigDecimal.valueOf(Constants._100), RoundingMode.HALF_UP)
                .multiply(playerForSale.getValue())
                .add(playerForSale.getValue());

        playerForSale.setValue(playerNewValue);


        buyer.getTeam().setBudget(buyer.getTeam().getBudget().subtract(existingTransfer.getPrice()));
        seller.getTeam().setBudget(buyer.getTeam().getBudget().add(existingTransfer.getPrice()));

        return transferManager.save(existingTransfer);
    }
}
