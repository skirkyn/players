package com.toptal.soccer.dto;

import lombok.Data;

@Data
public class Player {

    private String id;

    private String firstName;

    private String lastName;

    private String country;

    private String value;

    private int age;

    private Type type;

    public enum Type{
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        ATTACKER
    }
}
