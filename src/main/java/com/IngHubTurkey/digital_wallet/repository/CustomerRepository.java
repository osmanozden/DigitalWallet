package com.IngHubTurkey.digital_wallet.repository;

import com.IngHubTurkey.digital_wallet.model.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private static final String INSERT_SQL = "INSERT INTO customer (name, surname, tckn) " +
            "VALUES (:name, :surname, :tckn)";
    private static final String SELECT_LAST_ID = "SELECT SCOPE_IDENTITY()";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM customer WHERE id = :id";
    private static final String SELECT_BY_TCKN_SQL = "SELECT * FROM customer WHERE tckn = :tckn";
    private static final String SELECT_ALL_SQL = "SELECT * FROM customer";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CustomerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("surname"),
            rs.getString("tckn")
    );

    public User save(User customer) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", customer.getName())
                .addValue("surname", customer.getSurname())
                .addValue("tckn", customer.getTckn());

        jdbcTemplate.update(INSERT_SQL, params);

        Long id = jdbcTemplate.getJdbcTemplate().queryForObject(SELECT_LAST_ID, Long.class);
        customer.setId(id);
        return customer;
    }


    public Optional<User> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(SELECT_BY_ID_SQL, params, rowMapper).stream().findFirst();
    }

    public Optional<User> findByTckn(String tckn) {
        MapSqlParameterSource params = new MapSqlParameterSource("tckn", tckn);
        return jdbcTemplate.query(SELECT_BY_TCKN_SQL, params, rowMapper).stream().findFirst();
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, new MapSqlParameterSource(), rowMapper);
    }
}
