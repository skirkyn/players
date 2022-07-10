package com.toptal.soccer.dto;

import lombok.Data;

@Data
public class LoginResult {
    private String token;
    private String userId;
}
