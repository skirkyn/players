package com.toptal.soccer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    private String id;

    private String name;

    private String country;

    private String budget;

    private String value;

    private int numberOfPlayers;
}
