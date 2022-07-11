package com.toptal.soccer.manager;

import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.repo.TransferRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferManagerImplTest {
    public static final long ID = 1L;
    private TransferRepo transferRepo;
    private TransferManagerImpl transferManager;

    @BeforeEach
    public void setUp() {
        transferRepo = mock(TransferRepo.class);
        transferManager = new TransferManagerImpl(transferRepo);
    }

    @Test
    public void testFindsByIdIfIdIsNotNull() {
        // when
        transferManager.findById(ID);
        // then
        verify(transferRepo).findById(eq(ID));
    }

    @Test
    public void testGeneratesErrorIfIdIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> transferManager.findById(null));
    }

    @Test
    public void testSavesIfPlayerNotNull() {
        // when
        final Transfer transfer = new Transfer();
        transferManager.save(transfer);
        // then
        verify(transferRepo).save(eq(transfer));
    }

    @Test
    public void testGeneratesErrorIfPlayerIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> transferManager.save(null));
    }

    @Test
    public void testFindsAllIfAllParamsValid() {
        // when
        when(transferRepo.findAllByBuyerIsNull(any())).thenReturn(Page.empty());
        transferManager.findAll(0, 10);
        // then
        verify(transferRepo).findAllByBuyerIsNull(any());
    }

    @Test
    public void testGeneratesErrorIfIdIsNullForFindAll() {
        Assertions.assertThrows(NullPointerException.class, () -> transferManager.findAll(0, 10));
    }

    @Test
    public void testGeneratesErrorIfPagingParamsAreWrongForFindAll() {
        Assertions.assertThrows(NullPointerException.class, () -> transferManager.findAll(10, 0));
        Assertions.assertThrows(NullPointerException.class, () -> transferManager.findAll(-1, 1));

    }
}
