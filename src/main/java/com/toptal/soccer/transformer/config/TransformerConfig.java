package com.toptal.soccer.transformer.config;

import com.toptal.soccer.crypto.config.CryptoConfig;
import com.toptal.soccer.crypto.iface.Crypto;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import com.toptal.soccer.model.Transfer;
import com.toptal.soccer.model.User;
import com.toptal.soccer.orchestrator.iface.LoginUserOrchestrator;
import com.toptal.soccer.transformer.LoginResultDTOToLoginResult;
import com.toptal.soccer.transformer.LoginResultToLoginResultDTO;
import com.toptal.soccer.transformer.PlayerDTOToPlayer;
import com.toptal.soccer.transformer.PlayerToPlayerDTO;
import com.toptal.soccer.transformer.TeamDTOToTeam;
import com.toptal.soccer.transformer.TeamToTeamDTO;
import com.toptal.soccer.transformer.TransferDTOToTransfer;
import com.toptal.soccer.transformer.TransferToTransferDTO;
import com.toptal.soccer.transformer.UserToUserDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

@Import(CryptoConfig.class)
@Configuration
public class TransformerConfig {

    @Bean
    public Function<com.toptal.soccer.dto.Player, Player> playerDTOToPlayer(final Crypto crypto) {
        return new PlayerDTOToPlayer(crypto);
    }

    @Bean
    public Function<Player, com.toptal.soccer.dto.Player> playerToPlayerDTO(final Crypto crypto) {
        return new PlayerToPlayerDTO(crypto);
    }

    @Bean
    public Function<com.toptal.soccer.dto.Team, Team> teamDTOToTeam(final Crypto crypto) {
        return new TeamDTOToTeam(crypto);
    }

    @Bean
    public BiFunction<Team, BigDecimal, com.toptal.soccer.dto.Team> teamToTeamDTO(final Crypto crypto) {
        return new TeamToTeamDTO(crypto);
    }

    @Bean
    public Function<com.toptal.soccer.dto.Transfer, Transfer> transferDTOToTransfer(final Crypto crypto) {
        return new TransferDTOToTransfer(crypto);
    }

    @Bean
    public Function<Transfer, com.toptal.soccer.dto.Transfer> userToTransferDTO(final Crypto crypto,
                                                                                final Function<Player, com.toptal.soccer.dto.Player> playerToPlayerDTO) {
        return new TransferToTransferDTO(crypto, playerToPlayerDTO);
    }

    @Bean
    public Function<User, com.toptal.soccer.dto.User> userToUserDTO(final Crypto crypto) {
        return new UserToUserDTO(crypto);
    }

    @Bean
    public Function<com.toptal.soccer.dto.LoginResult, LoginUserOrchestrator.LoginResult> loginResultDTOToLoginResult(
            final Crypto crypto) {
        return new LoginResultDTOToLoginResult(crypto);
    }

    @Bean
    public Function<LoginUserOrchestrator.LoginResult, com.toptal.soccer.dto.LoginResult> loginResultToLoginResultDTO(
            final Crypto crypto) {

        return new LoginResultToLoginResultDTO(crypto);

    }
}
