package com.toptal.soccer.manager;

import com.toptal.soccer.manager.iface.UserManager;
import com.toptal.soccer.model.Team;
import com.toptal.soccer.model.User;
import com.toptal.soccer.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.Validate;

import java.util.Optional;

/**
 * This class implements user manager functionality
 */
public class UserManagerImpl implements UserManager {

    private final UserRepo userRepo;

    public UserManagerImpl(final UserRepo teamRepo) {
        this.userRepo = teamRepo;
    }

    @Override
    @Transactional
    public Optional<User> findById(final Long id) {

        Validate.notNull(id, Constants.ID_CAN_T_BE_NULL);

        return userRepo.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> findUserByEmail(final String email) {
        Validate.notNull(email, Constants.EMAIL_CAN_T_BE_NULL);
        return userRepo.findByEmail(email);
    }


    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public User save(final User team) {
        Validate.notNull(team, Constants.
                PLAYER_ID_CAN_T_BE_NULL);

        return userRepo.save(team);
    }

    @Override
    public Team findUserTeam(final Long userId) {
        Validate.notNull(userId, Constants.ID_CAN_T_BE_NULL);
        return userRepo.findTeamByUserId(userId).orElseThrow(() -> new IllegalStateException(Constants.USER_DOESN_T_HAVE_A_TEAM));
    }

    @Override
    public Optional<User> findByTeamId(final Long teamId) {
        Validate.notNull(teamId, Constants.ID_CAN_T_BE_NULL);
        return userRepo.findByTeamId(teamId);
    }

    @Override
    public Optional<User> findByPlayerId(final Long playerId) {
        Validate.notNull(playerId, Constants.ID_CAN_T_BE_NULL);
        return userRepo.findByPlayerId(playerId);
    }

    @Override
    public Optional<User> findByTransferId(Long playerId) {
        Validate.notNull(playerId, Constants.ID_CAN_T_BE_NULL);

        return userRepo.findByTransferId(playerId);
    }
}
