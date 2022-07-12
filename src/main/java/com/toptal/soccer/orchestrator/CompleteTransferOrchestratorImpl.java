package com.toptal.soccer.orchestrator;

import com.toptal.soccer.generator.Util;
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
import java.util.Objects;

/**
 * This class can complete existing transfer
 */
public class CompleteTransferOrchestratorImpl implements CompleteTransferOrchestrator {

    private final TransferManager transferManager;
    private final UserManager userManager;
    private final int playerValueIncrementPercentMin;
    private final int playerValueIncrementPercentMax;

    public CompleteTransferOrchestratorImpl(TransferManager transferManager,
                                            UserManager userManager,
                                            int playerValueIncrementPercentMin,
                                            int playerValueIncrementPercentMax) {
        this.transferManager = transferManager;
        this.userManager = userManager;
        this.playerValueIncrementPercentMin = playerValueIncrementPercentMin;
        this.playerValueIncrementPercentMax = playerValueIncrementPercentMax + 1;
    }


    @Override
    public Transfer complete(Long transferId, Long buyerId) {

        Validate.notNull(buyerId, Constants.BUYER_ID_CAN_T_BE_NULL);
        Validate.notNull(transferId, Constants.BUYER_ID_CAN_T_BE_NULL);

        final Transfer existingTransfer = transferManager.findById(transferId)
                .orElseThrow(() -> new IllegalArgumentException(Constants.TRANSFER_DOESNT_EXIST));
        if (existingTransfer.getBuyer() != null) {
            throw new IllegalArgumentException(Constants.TRANSFER_DOESNT_EXIST);

        }

        final Player playerForSale = existingTransfer.getPlayer();
        final User seller = existingTransfer.getSeller();

        final User buyer = userManager.findById(buyerId).orElseThrow(() -> new IllegalArgumentException(Constants.BUYER_DOESNT_EXIST));

        if (Objects.equals(buyer.getId(), seller.getId())) {
            throw new IllegalArgumentException(Constants.USER_CAN_T_SELL_THE_PLAYER_TO_THEMSELVES);
        }

        if (buyer.getTeam().getBudget().compareTo(playerForSale.getValue()) < 0) {
            throw new IllegalArgumentException(Constants.NOT_ENOUGH_MONEY);
        }

        if (buyer.getTeam().getBudgetCurrency() != seller.getTeam().getBudgetCurrency()
                || buyer.getTeam().getBudgetCurrency() != playerForSale.getValueCurrency()) {
            throw new IllegalArgumentException(Constants.ONLY_ONE_CURRENCY_IS_SUPPORTED);
        }

        // update money
        buyer.getTeam().setBudget(buyer.getTeam().getBudget().subtract(playerForSale.getValue()));
        seller.getTeam().setBudget(buyer.getTeam().getBudget().add(playerForSale.getValue()));

        // update players
        seller.getTeam().getPlayers().remove(playerForSale);
        userManager.save(seller);
        buyer.getTeam().getPlayers().add(playerForSale);
        userManager.save(buyer);

        int percentOfIncrease = Util.randomFromRange(playerValueIncrementPercentMin,
                playerValueIncrementPercentMax);

        final BigDecimal playerNewValue = BigDecimal.valueOf(percentOfIncrease)
                .divide(BigDecimal.valueOf(Constants._100), RoundingMode.HALF_UP)
                .multiply(playerForSale.getValue())
                .add(playerForSale.getValue());


        playerForSale.setValue(playerNewValue);
        existingTransfer.setBuyer(buyer);

        return transferManager.save(existingTransfer);
    }
}
