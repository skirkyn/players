package com.toptal.soccer.repo;

import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.toptal.soccer.TestUtil.generateUserWithTeam;

public class TransferRepoTest extends BaseRepoTest {
    @Autowired
    private TransferRepo repo;


    @Test
    @Transactional
    public void testFindsPlayersAndPages() {
        // given
        final User seller = save(generateUserWithTeam(() -> null));

        save(seller);
        final User buyer = save(generateUserWithTeam(() -> null));

        save(buyer);

        seller.getTeam().getPlayers().forEach(
                p -> {
                    final Transfer transfer = new Transfer(null, p, seller, null);
                    save(transfer);
                }
        );

        final int totalPlayers = seller.getTeam().getPlayers().size();


        final List<Transfer> transfers = repo.findAllByBuyerIsNull(PageRequest.of(0, totalPlayers)).stream().toList();
        Assertions.assertEquals(totalPlayers, transfers.size());


        Assertions.assertTrue(repo.findAllByBuyerIsNull(PageRequest.of(1, totalPlayers)).isEmpty());

        final Transfer transfer = transfers.get(0);

        transfer.setBuyer(buyer);

        save(transfer);
        Assertions.assertEquals(totalPlayers - 1, repo.findAllByBuyerIsNull(PageRequest.of(0, totalPlayers)).stream().toList().size());

    }
}
