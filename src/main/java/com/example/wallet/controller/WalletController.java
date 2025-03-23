package com.example.wallet.controller;

import com.example.wallet.model.WalletRequest;
import com.example.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<String> updateBalance(@RequestBody WalletRequest request) {
        try {
            walletService.processTransaction(request);
            return ResponseEntity.ok("Transaction successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<Double> getBalance(@PathVariable UUID walletId) {
        Double balance = walletService.getBalance(walletId);
        return balance == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(balance);
    }
}