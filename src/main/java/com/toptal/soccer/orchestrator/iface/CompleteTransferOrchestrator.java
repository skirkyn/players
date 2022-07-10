package com.toptal.soccer.orchestrator.iface;

import com.toptal.soccer.model.Transfer;

/**
 * A specification for complete transfer action
 */
public interface CompleteTransferOrchestrator {

    /**
     * Add a new transfer to the transfer list
     * @param transferId  id of the transfer to complete
     * @param buyerId an id of the buying user
     *
     * @return a completed transfer
     */
    Transfer complete(final Long transferId, final Long buyerId);
}
