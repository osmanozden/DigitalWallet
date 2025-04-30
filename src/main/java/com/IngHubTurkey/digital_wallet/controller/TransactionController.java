package com.IngHubTurkey.digital_wallet.controller;

import com.IngHubTurkey.digital_wallet.config.CustomUserDetails;
import com.IngHubTurkey.digital_wallet.model.Transaction;
import com.IngHubTurkey.digital_wallet.model.Wallet;
import com.IngHubTurkey.digital_wallet.service.TransactionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final WalletService walletService;

    public TransactionController(TransactionService transactionService,
                                 WalletService walletService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
    }

    @PostMapping("/deposit/{walletId}")
    public ResponseEntity<Transaction> deposit(@PathVariable Long walletId,
                                               @RequestBody Transaction transaction,
                                               Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Wallet wallet = walletService.getWalletById(walletId);
        if (!wallet.getCustomerId().equals(user.userId()) &&
                !user.role().equals(UserRole.EMPLOYEE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Transaction result = transactionService.deposit(walletId, transaction);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/withdraw/{walletId}")
    public ResponseEntity<Transaction> withdraw(@PathVariable Long walletId,
                                                @RequestBody Transaction transaction,
                                                Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Wallet wallet = walletService.getWalletById(walletId);
        if (!wallet.getCustomerId().equals(user.userId()) &&
                !user.role().equals(UserRole.EMPLOYEE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Transaction result = transactionService.withdraw(walletId, transaction);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/approve/{transactionId}")
    public ResponseEntity<Transaction> approve(@PathVariable Long transactionId,
                                               @RequestParam String status,
                                               Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        if (!user.role().equals(UserRole.EMPLOYEE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Transaction result = transactionService.approveTransaction(transactionId, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<List<Transaction>> list(@PathVariable Long walletId,
                                                  Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Wallet wallet = walletService.getWalletById(walletId);
        if (!wallet.getCustomerId().equals(user.userId()) &&
                !user.role().equals(UserRole.EMPLOYEE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(transactionService.getTransactionsByWalletId(walletId));
    }
}
