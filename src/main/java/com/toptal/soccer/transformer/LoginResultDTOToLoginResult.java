package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.orchestrator.iface.LoginUserOrchestrator;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

/**
 *  This class can transform LoginResult domain object to DTO
 */
public class LoginResultDTOToLoginResult implements Function<LoginResult, LoginUserOrchestrator.LoginResult> {

    private final Crypto crypto;

    public LoginResultDTOToLoginResult(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public LoginUserOrchestrator.LoginResult apply(LoginResult loginResult) {
        Validate.notNull(loginResult);
        Validate.notNull(loginResult.getToken(), Constants.TOKEN_CAN_T_BE_NULL);
        Validate.notNull(loginResult.getUserId(), Constants.ID_CAN_T_BE_NULL);

        return new LoginUserOrchestrator.LoginResult(loginResult.getToken(),
                Long.valueOf(crypto.decrypt(loginResult.getUserId())));    }
}
