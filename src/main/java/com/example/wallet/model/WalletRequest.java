package com.example.wallet.model;

import java.util.UUID;

public class WalletRequest {
    public UUID walletId;
    public String operationType; // DEPOSIT or WITHDRAW
    public double amount;
}