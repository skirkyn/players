package com.toptal.soccer.api;

import com.toptal.soccer.dto.Player;
import jakarta.websocket.server.PathParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class defines all web API that logically belongs to the Player resource
 */
@RestController
public class PlayerResource {

    /**
     * This method searches for a player by their id
     *
     * @param id id of the player
     *
     * @return a player
     */
    @GetMapping(path = "/player/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Player find(@PathParam("id") String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method updates a player. Only first, last names and a country can be updated.
     *
     * @param player a player object with new values of the fields
     * @param id     id of the player
     *
     * @return an updated player
     */
    @PatchMapping(path = "/player/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Player update(@RequestBody Player player, @PathParam("id") String id) {
        return new Player();
    }
}
