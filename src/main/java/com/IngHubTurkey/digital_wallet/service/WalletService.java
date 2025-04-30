package com.IngHubTurkey.digital_wallet.service;

import com.IngHubTurkey.digital_wallet.model.Wallet;
import com.IngHubTurkey.digital_wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public Wallet createWallet(Wallet wallet) {
        wallet.setBalance(wallet.getBalance() != null ? wallet.getBalance() : BigDecimal.ZERO);
        wallet.setUsableBalance(wallet.getUsableBalance() != null ? wallet.getUsableBalance() : BigDecimal.ZERO);
        return walletRepository.save(wallet);
    }

    public List<Wallet> getWalletsByCustomerId(Long customerId) {
        return walletRepository.findByCustomerId(customerId);
    }

    public Wallet getWalletById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    public void updateWallet(Wallet wallet) {
        walletRepository.update(wallet);
    }
}
