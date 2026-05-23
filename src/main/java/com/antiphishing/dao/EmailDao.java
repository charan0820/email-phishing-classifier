package com.antiphishing.dao;

import com.antiphishing.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EmailDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper for converting ResultSet to Email object
    private final RowMapper<Email> emailRowMapper = new RowMapper<Email>() {
        @Override
        public Email mapRow(ResultSet rs, int rowNum) throws SQLException {
            Email email = new Email();
            email.setId(rs.getLong("id"));
            email.setSubject(rs.getString("subject"));
            email.setBody(rs.getString("body"));
            email.setSender(rs.getString("sender"));
            email.setRecipient(rs.getString("recipient"));
            email.setIsPhishing(rs.getBoolean("is_phishing"));
            email.setConfidenceScore(rs.getDouble("confidence_score"));
            email.setFeatures(rs.getString("features"));

            Timestamp timestamp = rs.getTimestamp("created_at");
            if (timestamp != null) {
                email.setCreatedAt(timestamp.toLocalDateTime());
            }

            return email;
        }
    };

    public Email save(Email email) {
        if (email.getId() == null) {
            // Insert new email
            String sql = "INSERT INTO emails (subject, body, sender, recipient, is_phishing, confidence_score, features, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, email.getSubject());
                ps.setString(2, email.getBody());
                ps.setString(3, email.getSender());
                ps.setString(4, email.getRecipient());
                ps.setBoolean(5, email.getIsPhishing());
                ps.setDouble(6, email.getConfidenceScore());
                ps.setString(7, email.getFeatures());
                ps.setTimestamp(8, Timestamp.valueOf(email.getCreatedAt()));
                return ps;
            }, keyHolder);

            email.setId(keyHolder.getKey().longValue());
        } else {
            // Update existing email
            String sql = "UPDATE emails SET subject = ?, body = ?, sender = ?, recipient = ?, " +
                    "is_phishing = ?, confidence_score = ?, features = ? WHERE id = ?";

            jdbcTemplate.update(sql,
                    email.getSubject(),
                    email.getBody(),
                    email.getSender(),
                    email.getRecipient(),
                    email.getIsPhishing(),
                    email.getConfidenceScore(),
                    email.getFeatures(),
                    email.getId());
        }

        return email;
    }

    public List<Email> findAll() {
        String sql = "SELECT * FROM emails ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, emailRowMapper);
    }

    public List<Email> findByIsPhishing(Boolean isPhishing) {
        String sql = "SELECT * FROM emails WHERE is_phishing = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, emailRowMapper, isPhishing);
    }

    public Email findById(Long id) {
        String sql = "SELECT * FROM emails WHERE id = ?";
        List<Email> emails = jdbcTemplate.query(sql, emailRowMapper, id);
        return emails.isEmpty() ? null : emails.get(0);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM emails WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Long countPhishingEmails() {
        String sql = "SELECT COUNT(*) FROM emails WHERE is_phishing = true";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Long countLegitimateEmails() {
        String sql = "SELECT COUNT(*) FROM emails WHERE is_phishing = false";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}