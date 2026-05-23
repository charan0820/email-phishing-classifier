package com.antiphishing.dao;

import com.antiphishing.model.ClassificationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ClassificationRuleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ClassificationRule> ruleRowMapper = new RowMapper<ClassificationRule>() {
        @Override
        public ClassificationRule mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClassificationRule rule = new ClassificationRule();
            rule.setId(rs.getLong("id"));
            rule.setRuleName(rs.getString("rule_name"));
            rule.setRuleValue(rs.getDouble("rule_value"));
            rule.setDescription(rs.getString("description"));
            rule.setIsActive(rs.getBoolean("is_active"));
            return rule;
        }
    };

    public Map<String, Double> getActiveRulesAsMap() {
        String sql = "SELECT rule_name, rule_value FROM classification_rules WHERE is_active = TRUE";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        Map<String, Double> rules = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String ruleName = (String) row.get("rule_name");
            Double ruleValue = ((Number) row.get("rule_value")).doubleValue();
            rules.put(ruleName, ruleValue);
        }
        return rules;
    }

    public List<ClassificationRule> findAllActive() {
        String sql = "SELECT * FROM classification_rules WHERE is_active = TRUE";
        return jdbcTemplate.query(sql, ruleRowMapper);
    }

    public List<ClassificationRule> findAll() {
        String sql = "SELECT * FROM classification_rules";
        return jdbcTemplate.query(sql, ruleRowMapper);
    }

    public ClassificationRule save(ClassificationRule rule) {
        if (rule.getId() == null) {
            String sql = "INSERT INTO classification_rules (rule_name, rule_value, description, is_active) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql,
                    rule.getRuleName(),
                    rule.getRuleValue(),
                    rule.getDescription(),
                    rule.getIsActive());
        } else {
            String sql = "UPDATE classification_rules SET rule_name = ?, rule_value = ?, description = ?, is_active = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    rule.getRuleName(),
                    rule.getRuleValue(),
                    rule.getDescription(),
                    rule.getIsActive(),
                    rule.getId());
        }
        return rule;
    }

    public Double getRuleValue(String ruleName) {
        String sql = "SELECT rule_value FROM classification_rules WHERE rule_name = ? AND is_active = TRUE";
        List<Double> results = jdbcTemplate.queryForList(sql, Double.class, ruleName);
        return results.isEmpty() ? null : results.get(0);
    }
}