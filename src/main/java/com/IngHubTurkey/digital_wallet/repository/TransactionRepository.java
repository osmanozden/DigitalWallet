package com.IngHubTurkey.digital_wallet.repository;

import com.IngHubTurkey.digital_wallet.model.Transaction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {

    private static final String INSERT_SQL = "INSERT INTO transaction (wallet_id, amount, type, opposite_party_type, opposite_party, status) " +
            "VALUES (:walletId, :amount, :type, :oppositePartyType, :oppositeParty, :status)";
    private static final String SELECT_BY_ID = "SELECT * FROM transaction WHERE id = :id";
    private static final String SELECT_BY_WALLET_ID = "SELECT * FROM transaction WHERE wallet_id = :walletId";
    private static final String UPDATE_STATUS = "UPDATE transaction SET status = :status WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;


    private final RowMapper<Transaction> rowMapper = (rs, rowNum) -> new Transaction(
            rs.getLong("id"),
            rs.getLong("wallet_id"),
            rs.getBigDecimal("amount"),
            rs.getString("type"),
            rs.getString("opposite_party_type"),
            rs.getString("opposite_party"),
            rs.getString("status"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public TransactionRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Transaction save(Transaction tx) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("walletId", tx.getWalletId())
                .addValue("amount", tx.getAmount())
                .addValue("type", tx.getType())
                .addValue("oppositePartyType", tx.getOppositePartyType())
                .addValue("oppositeParty", tx.getOppositeParty())
                .addValue("status", tx.getStatus());

        jdbcTemplate.update(INSERT_SQL, params);
        Long id = jdbcTemplate.getJdbcTemplate().queryForObject("SELECT IDENTITY()", Long.class);
        tx.setId(id);
        return tx;
    }

    
    public Optional<Transaction> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(SELECT_BY_ID, params, rowMapper).stream().findFirst();
    }

    public List<Transaction> findByWalletId(Long walletId) {
        MapSqlParameterSource params = new MapSqlParameterSource("walletId", walletId);
        return jdbcTemplate.query(SELECT_BY_WALLET_ID, params, rowMapper);
    }

    public void updateStatus(Long id, String status) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", status);
        jdbcTemplate.update(UPDATE_STATUS, params);
    }
}
