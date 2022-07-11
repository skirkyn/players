package com.toptal.soccer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String country;

    @Column(columnDefinition = "DECIMAL(15,2)", nullable = false, name = "market_value")
    private BigDecimal value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency valueCurrency = Currency.DOLLAR;

    @Column(nullable = false)
    private LocalDateTime dateOfBirth;

    @Column(nullable = false)
    private Type type;

    public enum Type{
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        ATTACKER
    }
}
