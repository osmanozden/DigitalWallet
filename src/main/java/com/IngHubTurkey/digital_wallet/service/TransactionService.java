package com.IngHubTurkey.digital_wallet.service;

import com.IngHubTurkey.digital_wallet.model.Transaction;
import com.IngHubTurkey.digital_wallet.model.Wallet;
import com.IngHubTurkey.digital_wallet.repository.TransactionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    public TransactionService(TransactionRepository transactionRepository, WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
    }

    public Transaction deposit(Long walletId, Transaction tx) {
        Wallet wallet = walletService.getWalletById(walletId);
        String status = tx.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0 ? "PENDING" : "APPROVED";
        tx.setStatus(status);
        tx.setType("DEPOSIT");
        tx.setWalletId(walletId);

        if (status.equals("APPROVED")) {
            wallet.setBalance(wallet.getBalance().add(tx.getAmount()));
            wallet.setUsableBalance(wallet.getUsableBalance().add(tx.getAmount()));
        } else {
            wallet.setBalance(wallet.getBalance().add(tx.getAmount()));
        }

        walletService.updateWallet(wallet);
        return transactionRepository.save(tx);
    }


    public Transaction withdraw(Long walletId, Transaction tx) {
        Wallet wallet = walletService.getWalletById(walletId);

        if (!wallet.isActiveForWithdraw() && !wallet.isActiveForShopping()) {
            throw new RuntimeException("Withdraw not allowed");
        }

        String status = tx.getAmount().compareTo(BigDecimal.valueOf(1000)) > 0 ? "PENDING" : "APPROVED";
        tx.setStatus(status);
        tx.setType("WITHDRAW");
        tx.setWalletId(walletId);

        if (status.equals("APPROVED")) {
            if (wallet.getUsableBalance().compareTo(tx.getAmount()) < 0)
                throw new RuntimeException("Insufficient funds");
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(tx.getAmount()));
            wallet.setBalance(wallet.getBalance().subtract(tx.getAmount()));
        } else {
            if (wallet.getUsableBalance().compareTo(tx.getAmount()) < 0)
                throw new RuntimeException("Insufficient funds for PENDING withdrawal");
            wallet.setUsableBalance(wallet.getUsableBalance().subtract(tx.getAmount()));
        }

        walletService.updateWallet(wallet);
        return transactionRepository.save(tx);
    }


    public Transaction approveTransaction(Long transactionId, String status) {
        Transaction tx = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!tx.getStatus().equals("PENDING")) {
            throw new RuntimeException("Only PENDING transactions can be approved/denied");
        }

        Wallet wallet = walletService.getWalletById(tx.getWalletId());

        if (status.equals("APPROVED")) {
            if (tx.getType().equals("DEPOSIT")) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(tx.getAmount()));
            } else if (tx.getType().equals("WITHDRAW")) {
                wallet.setBalance(wallet.getBalance().subtract(tx.getAmount()));
            }
        } else if (status.equals("DENIED")) {
            if (tx.getType().equals("WITHDRAW")) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(tx.getAmount()));
            } else if (tx.getType().equals("DEPOSIT")) {
                wallet.setBalance(wallet.getBalance().subtract(tx.getAmount()));
            }
        }

        walletService.updateWallet(wallet);
        transactionRepository.updateStatus(transactionId, status);
        tx.setStatus(status);
        return tx;
    }


    public List<Transaction> getTransactionsByWalletId(Long walletId) {
        return transactionRepository.findByWalletId(walletId);
    }
}
