package com.toptal.soccer.transformer;

import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.dto.LoginResult;
import com.toptal.soccer.orchestrator.iface.LoginUserOrchestrator;
import org.apache.commons.lang3.Validate;

import java.util.function.Function;

/**
 *  This class can transform LoginResult DTO to the domain object
 */
public class LoginResultToLoginResultDTO implements Function<LoginUserOrchestrator.LoginResult, LoginResult> {

    private final Crypto crypto;

    public LoginResultToLoginResultDTO(Crypto crypto) {
        this.crypto = crypto;
    }
    @Override
    public LoginResult apply(LoginUserOrchestrator.LoginResult loginResult) {

        Validate.notNull(loginResult);
        Validate.notNull(loginResult.getJwtToken(), Constants.TOKEN_CAN_T_BE_NULL);
        Validate.notNull(loginResult.getUserId(), Constants.ID_CAN_T_BE_NULL);

        return new LoginResult(loginResult.getJwtToken(), crypto.encrypt(loginResult.getUserId().toString()));
    }
}
