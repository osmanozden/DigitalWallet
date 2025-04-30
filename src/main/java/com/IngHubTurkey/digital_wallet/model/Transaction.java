package com.IngHubTurkey.digital_wallet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Long walletId;
    private BigDecimal amount;
    private String type;
    private String oppositePartyType;
    private String oppositeParty;
    private String status;
    private LocalDateTime createdAt;
}
