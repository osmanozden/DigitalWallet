package com.IngHubTurkey.digital_wallet.repository;

import com.IngHubTurkey.digital_wallet.model.Wallet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WalletRepository {

    private static final String INSERT_SQL = "INSERT INTO wallet (customer_id, wallet_name, currency, active_for_shopping, active_for_withdraw, balance, usable_balance) " +
            "VALUES (:customerId, :walletName, :currency, :activeForShopping, :activeForWithdraw, :balance, :usableBalance)";
    private static final String SELECT_BY_ID = "SELECT * FROM wallet WHERE id = :id";
    private static final String SELECT_BY_CUSTOMER_ID = "SELECT * FROM wallet WHERE customer_id = :customerId";
    private static final String UPDATE_SQL = "UPDATE wallet SET balance = :balance, usable_balance = :usableBalance WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public WalletRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Wallet> rowMapper = (rs, rowNum) -> new Wallet(
            rs.getLong("id"),
            rs.getLong("customer_id"),
            rs.getString("wallet_name"),
            rs.getString("currency"),
            rs.getBoolean("active_for_shopping"),
            rs.getBoolean("active_for_withdraw"),
            rs.getBigDecimal("balance"),
            rs.getBigDecimal("usable_balance")
    );

    public Wallet save(Wallet wallet) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("customerId", wallet.getCustomerId())
                .addValue("walletName", wallet.getWalletName())
                .addValue("currency", wallet.getCurrency())
                .addValue("activeForShopping", wallet.isActiveForShopping())
                .addValue("activeForWithdraw", wallet.isActiveForWithdraw())
                .addValue("balance", wallet.getBalance())
                .addValue("usableBalance", wallet.getUsableBalance());

        jdbcTemplate.update(INSERT_SQL, params);
        Long id = jdbcTemplate.getJdbcTemplate().queryForObject("SELECT IDENTITY()", Long.class);
        wallet.setId(id);
        return wallet;
    }

    public Optional<Wallet> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(SELECT_BY_ID, params, rowMapper).stream().findFirst();
    }

    public List<Wallet> findByCustomerId(Long customerId) {
        MapSqlParameterSource params = new MapSqlParameterSource("customerId", customerId);
        return jdbcTemplate.query(SELECT_BY_CUSTOMER_ID, params, rowMapper);
    }

    public void update(Wallet wallet) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", wallet.getId())
                .addValue("balance", wallet.getBalance())
                .addValue("usableBalance", wallet.getUsableBalance());
        jdbcTemplate.update(UPDATE_SQL, params);
    }
}
