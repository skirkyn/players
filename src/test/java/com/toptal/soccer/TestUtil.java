package com.toptal.soccer;

import com.github.javafaker.Faker;
import com.toptal.soccer.generator.Util;
import com.toptal.soccer.model.Currency;
import com.toptal.soccer.model.Player;
import com.toptal.soccer.model.Team;
import com.toptal.soccer.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.time.ZoneOffset.UTC;

public class TestUtil {

    private static final Faker FAKER = new Faker();

    public static Player generatePlayer(Supplier<Long> idSupplier, int age, Player.Type type) {
        return new Player(idSupplier.get(),
                FAKER.name().firstName(),
                FAKER.name().lastName(),
                FAKER.address().country(),
                BigDecimal.valueOf(1_000_000),
                Currency.DOLLAR,
                LocalDateTime.now().minus(age, ChronoUnit.YEARS).toInstant(UTC).getEpochSecond(),
                type);

    }

    public static Player generatePlayer() {
        return generatePlayer(TestUtil::id, Util.randomFromRange(18, 41),
                Player.Type.values()[Util.randomFromRange(0, Player.Type.values().length)]);
    }


    public static List<Player> generatePlayers(Map<Player.Type, Integer> playersToGenerate) {
        return generatePlayers(TestUtil::id, playersToGenerate);
    }

    public static List<Player> generatePlayers(Supplier<Long> idSupplier, Map<Player.Type, Integer> playersToGenerate) {
        return new LinkedList<>(playersToGenerate.entrySet().stream()
                .flatMap(e -> Stream.generate(() -> generatePlayer(idSupplier, (Util.randomFromRange(18, 41)), e.getKey()))
                        .limit(e.getValue()))
                .toList());
    }

    public static Team generateTeamWithPlayers(Supplier<Long> idSupplier) {
        return new Team(idSupplier.get(),
                FAKER.team().name(),
                FAKER.address().country(),
                BigDecimal.valueOf(5_000_000),
                Currency.DOLLAR,
                generatePlayers(idSupplier,
                        Map.of(Player.Type.GOALKEEPER, 3,
                                Player.Type.DEFENDER, 6,
                                Player.Type.MIDFIELDER, 6,
                                Player.Type.ATTACKER, 6)
                )
        );
    }

    public static Team generateTeamWithPlayers() {
        return generateTeamWithPlayers(TestUtil::id);
    }

    public static User generateUserWithTeam(Supplier<Long> idSupplier) {
        return new User(idSupplier.get(),
                FAKER.internet().emailAddress(),
                FAKER.internet().macAddress(),
                generateTeamWithPlayers(idSupplier));
    }

    public static User generateUserWithTeam() {
        return generateUserWithTeam(TestUtil::id);
    }

    public static User generateUserWithoutTeam(Supplier<Long> idSupplier) {
        return new User(idSupplier.get(),
                FAKER.internet().emailAddress(),
                FAKER.internet().macAddress(),
                null);
    }

    public static User generateUserWithoutTeam() {
        return generateUserWithoutTeam(TestUtil::id);
    }

    private static Long id() {
        return Long.valueOf(FAKER.idNumber().valid().replaceAll("-", ""));
    }

}
