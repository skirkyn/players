package com.toptal.soccer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private String id;
    private String price;
    private Player player;
    private String sellerId;
    private String buyerId;
}
