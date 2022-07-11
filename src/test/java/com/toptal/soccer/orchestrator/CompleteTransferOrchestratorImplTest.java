package com.toptal.soccer.orchestrator;

import com.toptal.soccer.manager.iface.TransferManager;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.toptal.soccer.TestUtil.generateUserWithTeam;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompleteTransferOrchestratorImplTest {

    public static final int PLAYER_VALUE_INCREMENT_PERCENT_MIN = 10;
    public static final int PLAYER_VALUE_INCREMENT_PERCENT_MAX = 100;
    private TransferManager transferManager = mock(TransferManager.class);
    private UserManager userManager = mock(UserManager.class);

    private CompleteTransferOrchestratorImpl completeTransferOrchestrator;

    @BeforeEach
    public void setUp() {

        transferManager = mock(TransferManager.class);
        userManager = mock(UserManager.class);

        completeTransferOrchestrator = new CompleteTransferOrchestratorImpl(transferManager,
                userManager,
                PLAYER_VALUE_INCREMENT_PERCENT_MIN,
                PLAYER_VALUE_INCREMENT_PERCENT_MAX);
    }

    @Test
    public void testTransferSuccess() {

        // given
        final User seller = generateUserWithTeam();
        final User buyer = generateUserWithTeam();

        final Player forSale = seller.getTeam().getPlayers().get(0);
        final Transfer transfer = new Transfer(1L, forSale, seller, null);

        Assertions.assertNull(transfer.getBuyer());
        Assertions.assertNotEquals(seller.getId(), buyer.getId());

        Assertions.assertEquals(seller.getTeam().getBudget(), buyer.getTeam().getBudget());
        Assertions.assertEquals(seller.getTeam().getPlayers().size(), buyer.getTeam().getPlayers().size());

        // when
        when(userManager.findById(eq(buyer.getId()))).thenReturn(Optional.of(buyer));
        when(transferManager.findById(eq(transfer.getId()))).thenReturn(Optional.of(transfer));
        doAnswer(returnsFirstArg()).when(transferManager).save(eq(transfer));

        // then
        final BigDecimal oldPlayerValue = forSale.getValue();

        final Transfer afterSave = completeTransferOrchestrator.complete(transfer.getId(), buyer.getId());

        Assertions.assertEquals(buyer, afterSave.getBuyer());
        Assertions.assertTrue(seller.getTeam().getBudget().compareTo(buyer.getTeam().getBudget()) > 0);
        Assertions.assertTrue(seller.getTeam().getPlayers().size() < buyer.getTeam().getPlayers().size());

        final int incrementFactor = forSale.getValue().subtract(oldPlayerValue).divide(oldPlayerValue, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).intValue();


        Assertions.assertTrue(incrementFactor >= PLAYER_VALUE_INCREMENT_PERCENT_MIN && incrementFactor <= PLAYER_VALUE_INCREMENT_PERCENT_MAX);

    }

    @Test
    public void testCantTransferToThemselves() {
        // given
        final User seller = generateUserWithTeam();

        final Player forSale = seller.getTeam().getPlayers().get(0);
        final Transfer transfer = new Transfer(1L, forSale, seller, null);

        Assertions.assertNull(transfer.getBuyer());


        // when
        when(transferManager.findById(eq(transfer.getId()))).thenReturn(Optional.of(transfer));
        when(userManager.findById(eq(seller.getId()))).thenReturn(Optional.of(seller));

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> completeTransferOrchestrator.complete(transfer.getId(), seller.getId()));

    }

    @Test
    public void testCantTransferToNonExistingUser() {
        // given
        final User seller = generateUserWithTeam();

        final Player forSale = seller.getTeam().getPlayers().get(0);
        final Transfer transfer = new Transfer(1L, forSale, seller, null);

        Assertions.assertNull(transfer.getBuyer());


        // when
        when(transferManager.findById(eq(transfer.getId()))).thenReturn(Optional.of(transfer));

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> completeTransferOrchestrator.complete(transfer.getId(), seller.getId() * 2));

    }

    @Test
    public void testCantTransferToNonExistingTransfer() {
        // given
        final User seller = generateUserWithTeam();

        final Player forSale = seller.getTeam().getPlayers().get(0);
        final Transfer transfer = new Transfer(1L, forSale, seller, null);

        Assertions.assertNull(transfer.getBuyer());


        // when
        when(transferManager.findById(eq(transfer.getId()))).thenReturn(Optional.of(transfer));

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> completeTransferOrchestrator.complete(transfer.getId() * 2, seller.getId()));

    }
}
