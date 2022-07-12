package com.toptal.soccer.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @Column(columnDefinition = "DECIMAL(15,2)", nullable = false)
    private BigDecimal budget;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency budgetCurrency = Currency.DOLLAR;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Player> players;
}
