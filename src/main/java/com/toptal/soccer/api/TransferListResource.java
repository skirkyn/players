package com.toptal.soccer.api;

import com.toptal.soccer.api.exception.NotAuthorizedException;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.Transfer;
import com.toptal.soccer.manager.iface.TransferManager;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.orchestrator.iface.CompleteTransferOrchestrator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;


/**
 * This class defines all web API that logically belongs to the TransferList resource
 */
@RestController
public class TransferListResource extends BaseResource {
    private static final String LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_TRANSFER =
            "Logged user is not authorized to access this transfer: ";
    private static final String USER_IS_NOT_AUTHORIZED_TO_SEE_ALL_THE_TRANSFERS = "User is not authorized to see all the transfers";
    private final Crypto crypto;
    private final TransferManager transferManager;
    private final UserManager userManager;
    private final Function<com.toptal.soccer.model.Transfer, Transfer> transferToTransferDTO;
    private final Function<Transfer, com.toptal.soccer.model.Transfer> transferDTOToTransfer;

    private final CompleteTransferOrchestrator completeTransferOrchestrator;

    public TransferListResource(Crypto crypto,
                                TransferManager transferManager,
                                UserManager userManager,
                                Function<com.toptal.soccer.model.Transfer, Transfer> transferToTransferDTO,
                                Function<Transfer, com.toptal.soccer.model.Transfer> transferDTOToTransfer,
                                CompleteTransferOrchestrator completeTransferOrchestrator) {
        this.crypto = crypto;
        this.transferManager = transferManager;
        this.userManager = userManager;
        this.transferToTransferDTO = transferToTransferDTO;
        this.transferDTOToTransfer = transferDTOToTransfer;
        this.completeTransferOrchestrator = completeTransferOrchestrator;
    }

    /**
     * Add a new entry to the transfer list.
     *
     * @param transfer       a transfer to be added
     * @param authentication current authentication
     * @return an added transfer
     */
    @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transfer> add(@RequestBody Transfer transfer, final Authentication authentication) {
        final Long decrypted = Long.valueOf(crypto.decrypt(transfer.getSellerId()));

        if (!Objects.equals(decrypted, authentication.getPrincipal())) {
            throw new NotAuthorizedException(
                    LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_TRANSFER + transfer.getSellerId());

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                transferToTransferDTO.apply(transferManager.save(transferDTOToTransfer.apply(transfer))));
    }

    /**
     * Complete existing transfer(buy a player). The authenticated user has to be authorized to perform operations on this resource.
     *
     * @param id             a transfer object that has information about the buyer
     * @param buyerId        id of the user
     * @param authentication current authentication
     * @return an updated transfer object
     */
    @PostMapping(path = "/transfer/{id}/to/{buyerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transfer> complete(@PathVariable String id, @PathVariable String buyerId, final Authentication authentication) {

        final Long decryptedBuyer = Long.valueOf(crypto.decrypt(buyerId));
        final Long decryptedTransfer = Long.valueOf(crypto.decrypt(id));

        if (!Objects.equals(decryptedBuyer, authentication.getPrincipal())) {
            throw new NotAuthorizedException(LOGGED_USER_IS_NOT_AUTHORIZED_TO_ACCESS_THIS_TRANSFER + id);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transferToTransferDTO.apply(completeTransferOrchestrator.complete(decryptedTransfer, decryptedBuyer)));

    }

    /**
     * This method returns all the players that are on the transfer list
     *
     * @param userId         - current user id
     * @param start          - parameter for the pagination. Defines the beginning of the result list.Optional. Default = 0
     * @param size           - parameter for the pagination. Defines the size of the list to return. Optional. Default = 0
     * @param authentication current authentication
     * @return a list of players that are on the transfer list, capped with the pagination parameters
     */
    @GetMapping(path = "/transfer", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Transfer> findAll(
            @RequestParam(defaultValue = "0") final int start,
                                  @RequestParam(defaultValue = "20") final int size,
                                  final Authentication authentication) {

        if (!authentication.isAuthenticated()) {
            throw new NotAuthorizedException(USER_IS_NOT_AUTHORIZED_TO_SEE_ALL_THE_TRANSFERS);
        }

        return transferManager.findAll(start, size).stream().map(transferToTransferDTO).toList();
    }
}
