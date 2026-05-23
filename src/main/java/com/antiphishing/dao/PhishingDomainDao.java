package com.antiphishing.dao;

import com.antiphishing.model.PhishingDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PhishingDomainDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<PhishingDomain> domainRowMapper = new RowMapper<PhishingDomain>() {
        @Override
        public PhishingDomain mapRow(ResultSet rs, int rowNum) throws SQLException {
            PhishingDomain domain = new PhishingDomain();
            domain.setId(rs.getLong("id"));
            domain.setDomainPattern(rs.getString("domain_pattern"));
            domain.setWeight(rs.getDouble("weight"));
            domain.setIsActive(rs.getBoolean("is_active"));
            return domain;
        }
    };

    public List<PhishingDomain> findAllActive() {
        String sql = "SELECT * FROM phishing_domains WHERE is_active = TRUE ORDER BY weight DESC";
        return jdbcTemplate.query(sql, domainRowMapper);
    }

    public List<PhishingDomain> findAll() {
        String sql = "SELECT * FROM phishing_domains ORDER BY weight DESC";
        return jdbcTemplate.query(sql, domainRowMapper);
    }

    public PhishingDomain save(PhishingDomain domain) {
        if (domain.getId() == null) {
            String sql = "INSERT INTO phishing_domains (domain_pattern, weight, is_active) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql,
                    domain.getDomainPattern(),
                    domain.getWeight(),
                    domain.getIsActive());
        } else {
            String sql = "UPDATE phishing_domains SET domain_pattern = ?, weight = ?, is_active = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    domain.getDomainPattern(),
                    domain.getWeight(),
                    domain.getIsActive(),
                    domain.getId());
        }
        return domain;
    }
}