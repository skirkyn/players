package com.toptal.soccer.manager.iface;

import com.toptal.soccer.model.Transfer;

import java.util.List;
import java.util.Optional;

/**
 * This interface describes the API of the player service
 */
public interface TransferManager {
    /**
     * Finds user by id
     * @param id of the player
     * @return an optional objects with the player if the player with such id exists
     */
    Optional<Transfer> findById(Long id);

    /**
     * Persists the transfer
     * @param transfer a player to store
     * @return a persisted player
     */
    Transfer save(Transfer transfer);

    /**
     * Finds all transfers starting from the start. And returns the list with length == pageSize
     * @param start beginning of the list
     * @param pageSize the size of the list
     *
     * @return list with results
     */
    List<Transfer> findAll(int start, int pageSize);

}
