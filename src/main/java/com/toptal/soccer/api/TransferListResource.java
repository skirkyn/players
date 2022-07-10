package com.toptal.soccer.api;

import com.toptal.soccer.dto.Transfer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * This class defines all web API that logically belongs to the TransferList resource
 */
@RestController
public class TransferListResource {

    /**
     * Add a new entry to the transfer list.
     *
     * @param transfer a transfer to be added
     * @return an added transfer
     */
    @PostMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Transfer add(@RequestBody Transfer transfer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Complete existing transfer(buy a player). The authenticated user has to be authorized to perform operations on this resource.
     *
     * @param id      a transfer object that has information about the buyer
     * @param buyerId id of the user
     * @return an updated transfer object
     */
    @PostMapping(path = "/transfer/{id}/to/{buyerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Transfer complete(@PathVariable String id, @PathVariable String buyerId) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method returns all the players that are on the transfer list
     *
     * @param start - parameter for the pagination. Defines the beginning of the result list.Optional. Default = 0
     * @param size  - parameter for the pagination. Defines the size of the list to return. Optional. Default = 0
     * @return a list of players that are on the transfer list, capped with the pagination parameters
     */
    @GetMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Transfer findAll(@RequestParam(defaultValue = "0") int start,
                            @RequestParam(defaultValue = "20") int size) {
        throw new UnsupportedOperationException();
    }
}
