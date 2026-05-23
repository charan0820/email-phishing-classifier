package com.antiphishing.dao;

import com.antiphishing.model.TrainingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TrainingDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<TrainingData> trainingDataRowMapper = new RowMapper<TrainingData>() {
        @Override
        public TrainingData mapRow(ResultSet rs, int rowNum) throws SQLException {
            TrainingData trainingData = new TrainingData();
            trainingData.setId(rs.getLong("id"));
            trainingData.setEmailText(rs.getString("email_text"));
            trainingData.setIsPhishing(rs.getBoolean("is_phishing"));
            trainingData.setUsedForTraining(rs.getBoolean("used_for_training"));
            return trainingData;
        }
    };

    public List<TrainingData> findAll() {
        String sql = "SELECT * FROM training_data";
        return jdbcTemplate.query(sql, trainingDataRowMapper);
    }

    public List<TrainingData> findByIsPhishing(Boolean isPhishing) {
        String sql = "SELECT * FROM training_data WHERE is_phishing = ?";
        return jdbcTemplate.query(sql, trainingDataRowMapper, isPhishing);
    }

    public List<TrainingData> findByUsedForTraining(Boolean usedForTraining) {
        String sql = "SELECT * FROM training_data WHERE used_for_training = ?";
        return jdbcTemplate.query(sql, trainingDataRowMapper, usedForTraining);
    }

    public TrainingData save(TrainingData trainingData) {
        if (trainingData.getId() == null) {
            String sql = "INSERT INTO training_data (email_text, is_phishing, used_for_training) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql,
                    trainingData.getEmailText(),
                    trainingData.getIsPhishing(),
                    trainingData.getUsedForTraining());
        } else {
            String sql = "UPDATE training_data SET email_text = ?, is_phishing = ?, used_for_training = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    trainingData.getEmailText(),
                    trainingData.getIsPhishing(),
                    trainingData.getUsedForTraining(),
                    trainingData.getId());
        }
        return trainingData;
    }
}