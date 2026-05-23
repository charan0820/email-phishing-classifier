package com.antiphishing.dao;

import com.antiphishing.model.PhishingKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PhishingKeywordDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<PhishingKeyword> keywordRowMapper = new RowMapper<PhishingKeyword>() {
        @Override
        public PhishingKeyword mapRow(ResultSet rs, int rowNum) throws SQLException {
            PhishingKeyword keyword = new PhishingKeyword();
            keyword.setId(rs.getLong("id"));
            keyword.setKeyword(rs.getString("keyword"));
            keyword.setWeight(rs.getDouble("weight"));
            keyword.setCategory(rs.getString("category"));
            keyword.setIsActive(rs.getBoolean("is_active"));
            return keyword;
        }
    };

    public List<PhishingKeyword> findAllActive() {
        String sql = "SELECT * FROM phishing_keywords WHERE is_active = TRUE ORDER BY weight DESC";
        return jdbcTemplate.query(sql, keywordRowMapper);
    }

    public List<PhishingKeyword> findAll() {
        String sql = "SELECT * FROM phishing_keywords ORDER BY weight DESC";
        return jdbcTemplate.query(sql, keywordRowMapper);
    }

    public List<PhishingKeyword> findByCategory(String category) {
        String sql = "SELECT * FROM phishing_keywords WHERE category = ? AND is_active = TRUE ORDER BY weight DESC";
        return jdbcTemplate.query(sql, keywordRowMapper, category);
    }

    public PhishingKeyword save(PhishingKeyword keyword) {
        if (keyword.getId() == null) {
            String sql = "INSERT INTO phishing_keywords (keyword, weight, category, is_active) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    keyword.getKeyword(),
                    keyword.getWeight(),
                    keyword.getCategory(),
                    keyword.getIsActive());
        } else {
            String sql = "UPDATE phishing_keywords SET keyword = ?, weight = ?, category = ?, is_active = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    keyword.getKeyword(),
                    keyword.getWeight(),
                    keyword.getCategory(),
                    keyword.getIsActive(),
                    keyword.getId());
        }
        return keyword;
    }

    public void deactivateKeyword(Long id) {
        String sql = "UPDATE phishing_keywords SET is_active = FALSE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}