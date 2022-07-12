package com.toptal.soccer.orchestrator;

import com.toptal.soccer.crypto.iface.PasswordHasher;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.model.User;
import com.toptal.soccer.orchestrator.iface.LoginUserOrchestrator;
import com.toptal.soccer.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * This class provides functionality for a new user registration
 */
@Slf4j
public class LoginUserOrchestratorImpl implements LoginUserOrchestrator {

    private final UserManager userManager;
    private final PasswordHasher passwordHasher;
    private final BiFunction<Map<String, Object>, User, String> jwtGenerator;

    public LoginUserOrchestratorImpl(UserManager userManager,
                                     PasswordHasher passwordHasher,
                                     BiFunction<Map<String, Object>, User, String> jwtGenerator) {
        this.userManager = userManager;
        this.passwordHasher = passwordHasher;
        this.jwtGenerator = jwtGenerator;
    }


    @Override
    public LoginResult login(String email, String password) {
        Validate.notEmpty(email, Constants.EMAIL_CAN_T_BE_EMPTY);
        Validate.notEmpty(password, Constants.PASSWORD_CAN_T_BE_EMPTY);

        final User user = userManager.findUserByEmail(email)
                .filter(u -> checkHashesMatch(password, u))
                .orElseThrow(() -> new SecurityException("Invalid email or password"));

        final String token = jwtGenerator.apply(new HashMap<>(), user);

        return new LoginUserOrchestrator.LoginResult(token, user.getId());
    }

    private boolean checkHashesMatch(final String password, final User u) {
        try {
            return StringUtils.equals(u.getPasswordHash(), passwordHasher.hash(password));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
