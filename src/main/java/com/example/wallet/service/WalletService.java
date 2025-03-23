package com.example.wallet.service;

import com.example.wallet.entity.Wallet;
import com.example.wallet.model.WalletRequest;
import com.example.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final ReentrantLock lock = new ReentrantLock();

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void processTransaction(WalletRequest request) {
        lock.lock();
        try {
            Wallet wallet = walletRepository.findById(request.walletId)
                    .orElse(new Wallet(request.walletId, 0.0));

            if ("DEPOSIT".equalsIgnoreCase(request.operationType)) {
                wallet.setBalance(wallet.getBalance() + request.amount);
            } else if ("WITHDRAW".equalsIgnoreCase(request.operationType)) {
                if (wallet.getBalance() < request.amount) {
                    throw new IllegalArgumentException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance() - request.amount);
            } else {
                throw new IllegalArgumentException("Invalid operation type");
            }

            walletRepository.save(wallet);
        } finally {
            lock.unlock();
        }
    }

    public Double getBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElse(null);
    }
}