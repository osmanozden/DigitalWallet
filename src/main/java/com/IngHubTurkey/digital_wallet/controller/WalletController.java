package com.IngHubTurkey.digital_wallet.controller;

import com.IngHubTurkey.digital_wallet.config.CustomUserDetails;
import com.IngHubTurkey.digital_wallet.model.Wallet;
import com.IngHubTurkey.digital_wallet.service.WalletService;
import com.IngHubTurkey.digital_wallet.type.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet,
                                               Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        wallet.setCustomerId(user.userId());
        Wallet created = walletService.createWallet(wallet);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> getWallets(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        List<Wallet> wallets = walletService.getWalletsByCustomerId(user.userId());
        return ResponseEntity.ok(wallets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Long id,
                                                Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Wallet wallet = walletService.getWalletById(id);
        if (!wallet.getCustomerId().equals(user.userId()) &&
                !user.role().equals(UserRole.EMPLOYEE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(wallet);
    }
}
