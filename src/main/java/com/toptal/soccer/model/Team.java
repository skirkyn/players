package com.toptal.soccer.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "user", optional = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(columnDefinition = "DECIMAL(15,2)", nullable = false)
    private BigDecimal budget;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency budgetCurrency = Currency.DOLLAR;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Player> players;
}
