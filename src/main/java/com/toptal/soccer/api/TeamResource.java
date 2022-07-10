package com.toptal.soccer.api;

import com.toptal.soccer.dto.Player;
import com.toptal.soccer.dto.Team;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class defines all web API that logically belongs to the Team resource
 */
@RestController
public class TeamResource {

    /**
     * This method copies name and country from the team object that is passed to the method
     * @param id - team id, should not be null. The authenticated user has to own the team whose id is passed in request.
     * @return a response with a team entity as a body
     */
    @PatchMapping(path = "/team/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Team update(@RequestBody Team team, @PathVariable String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method returns team players by team id.
     * @param id team id, should not be null. The authenticated user has to own the team whose id is passed in request.
     * @param start - parameter for the pagination. Defines the beginning of the result list.Optional. Default = 0
     * @param size - parameter for the pagination. Defines the size of the list to return. Optional. Default = 0
     *
     * @return a list of players that belong to the team, capped with the pagination parameters
     */
    @GetMapping(path = "/team/{id}/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Player> findPlayers(@PathVariable String id,
                                    @RequestParam(defaultValue = "0", required = false) int start,
                                    @RequestParam(defaultValue = "20", required = false) int size) {
        throw new UnsupportedOperationException();
    }
}
