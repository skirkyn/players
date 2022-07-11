package com.toptal.soccer.orchestrator;

import com.toptal.soccer.crypto.iface.PasswordHasher;
import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.model.User;
import com.toptal.soccer.orchestrator.iface.CreateNewTeamOrchestrator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.toptal.soccer.TestUtil.generateUserWithoutTeam;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RegisterUserOrchestratorImplTest{

    private final UserManager userManager = mock(UserManager.class);
    private final CreateNewTeamOrchestrator createNewTeamOrchestrator = mock(CreateNewTeamOrchestrator.class);

    private final PasswordHasher passwordHasher = mock(PasswordHasher.class);

    private RegisterUserOrchestratorImpl registerUserOrchestrator;

    @BeforeEach
    public void setUp() throws Exception{

        registerUserOrchestrator = new RegisterUserOrchestratorImpl(
                userManager,
                passwordHasher,
                createNewTeamOrchestrator);
    }

    @Test
    public void testCreatesUserIfNoUserExistWithTheEmail() {
        // given
        final String email = "some@email";
        final String password = "password";
        registerUserOrchestrator.register(email, password);

        //then
        verify(userManager).findUserByEmail(eq(email));
        verify(userManager).save(any());
    }

    @Test
    public void testWontCreateUserIfUserExistsWithTheEmail() {
        // given
        final User user = generateUserWithoutTeam();

        final String email = user.getEmail();
        final String password = "password";

        // when
        when(userManager.findUserByEmail(eq(email))).thenReturn(Optional.of(user));

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> registerUserOrchestrator.register(email, password));

    }

    @Test
    public void testWontCreateUserIfCantHash() throws Exception{
        // given
        final String email = "some@email";
        final String password = "password";
        registerUserOrchestrator.register(email, password);

        // when
        when(passwordHasher.hash(eq(password))).thenThrow(RuntimeException.class);

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> registerUserOrchestrator.register(email, password));

    }

}
