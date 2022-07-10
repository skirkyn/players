package com.toptal.soccer.manager;

import com.toptal.soccer.manager.iface.TransferManager;
import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.repo.TransferRepo;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.Validate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * This class implements transfer manager functionality
 */
public class TransferManagerImpl implements TransferManager {

    private final TransferRepo transferRepo;

    public TransferManagerImpl(TransferRepo transferRepo) {
        this.transferRepo = transferRepo;
    }

    @Override
    @Transactional
    public Optional<Transfer> findById(Long id) {
        Validate.notNull(id, Constants.ID_CAN_T_BE_NULL);

        return transferRepo.findById(id);
    }

    @Override
    @Transactional
    public Transfer save(Transfer transfer) {

        Validate.notNull(transfer.getSeller().getId(), Constants.SELLER_ID_CAN_T_BE_NULL);
        Validate.notNull(transfer.getPrice(), Constants.PRICE_CAN_T_BE_NULL);
        Validate.notNull(transfer.getPlayer().getId(), Constants.PLAYER_ID_CAN_T_BE_NULL);

        return transferRepo.save(transfer);
    }

    @Override
    public List<Transfer> findAll(int start, int pageSize) {
        Validate.isTrue(start > 0 && pageSize > 0, Constants.START_AND_PAGE_SIZE_HAVE_TO_BE_GREATER_THAN_ZERO);

        final Page<Transfer> result = transferRepo.findAll(PageRequest.of(start/pageSize, pageSize));

        return result.stream().toList();
    }
}
