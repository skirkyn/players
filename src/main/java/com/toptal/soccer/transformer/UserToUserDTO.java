package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.User;

import java.util.function.Function;

/**
 * This class can transform User domain object to DTOx
 */
public class UserToUserDTO implements Function<User, com.toptal.soccer.dto.User> {
    private final Crypto crypto;

    public UserToUserDTO(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public com.toptal.soccer.dto.User apply(User user) {
        final com.toptal.soccer.dto.User dto = new com.toptal.soccer.dto.User();
        if (user != null) {
            // mask the id
            dto.setId(crypto.encrypt(user.getId().toString()));
            dto.setEmail(user.getEmail());
        }

        return dto;
    }
}
