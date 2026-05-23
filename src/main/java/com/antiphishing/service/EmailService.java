package com.antiphishing.service;

import com.antiphishing.classifier.PhishingClassifier;
import com.antiphishing.dao.EmailDao;
import com.antiphishing.model.Email;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private PhishingClassifier classifier;

    private ObjectMapper objectMapper = new ObjectMapper();

    public Email analyzeEmail(Email email) {
        try {
            PhishingClassifier.ClassificationResult result =
                    classifier.classifyEmail(email.getSubject(), email.getBody(), email.getSender());

            email.setIsPhishing(result.isPhishing());
            email.setConfidenceScore(result.getConfidence());

            try {
                email.setFeatures(objectMapper.writeValueAsString(result.getFeatures()));
            } catch (JsonProcessingException e) {
                email.setFeatures("{}");
                logger.warn("Failed to serialize features: {}", e.getMessage());
            }

            return emailDao.save(email);
        } catch (DataAccessException e) {
            logger.error("Database error while saving email: {}", e.getMessage());
            // Return the analyzed email even if save fails
            return email;
        } catch (Exception e) {
            logger.error("Unexpected error while analyzing email: {}", e.getMessage());
            return email;
        }
    }

    public List<Email> getAllEmails() {
        try {
            return emailDao.findAll();
        } catch (DataAccessException e) {
            logger.error("Database error while fetching emails: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Email> getPhishingEmails() {
        try {
            return emailDao.findByIsPhishing(true);
        } catch (DataAccessException e) {
            logger.error("Database error while fetching phishing emails: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Email> getLegitimateEmails() {
        try {
            return emailDao.findByIsPhishing(false);
        } catch (DataAccessException e) {
            logger.error("Database error while fetching legitimate emails: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    public Email getEmailById(Long id) {
        try {
            return emailDao.findById(id);
        } catch (DataAccessException e) {
            logger.error("Database error while fetching email by id: {}", e.getMessage());
            return null;
        }
    }

    public void deleteEmail(Long id) {
        try {
            emailDao.deleteById(id);
        } catch (DataAccessException e) {
            logger.error("Database error while deleting email: {}", e.getMessage());
            throw new RuntimeException("Failed to delete email: " + e.getMessage());
        }
    }

    public Statistics getStatistics() {
        try {
            Long phishingCount = emailDao.countPhishingEmails();
            Long legitimateCount = emailDao.countLegitimateEmails();
            Long total = phishingCount + legitimateCount;

            double phishingPercentage = total > 0 ? (phishingCount * 100.0) / total : 0;
            double legitimatePercentage = total > 0 ? (legitimateCount * 100.0) / total : 0;

            return new Statistics(phishingCount, legitimateCount, total,
                    phishingPercentage, legitimatePercentage);
        } catch (DataAccessException e) {
            logger.error("Database error while fetching statistics: {}", e.getMessage());
            return new Statistics(0L, 0L, 0L, 0.0, 0.0);
        }
    }

    // Statistics inner class - FIXED: Added public static
    public static class Statistics {
        private final Long phishingCount;
        private final Long legitimateCount;
        private final Long totalEmails;
        private final Double phishingPercentage;
        private final Double legitimatePercentage;

        public Statistics(Long phishingCount, Long legitimateCount, Long totalEmails,
                          Double phishingPercentage, Double legitimatePercentage) {
            this.phishingCount = phishingCount;
            this.legitimateCount = legitimateCount;
            this.totalEmails = totalEmails;
            this.phishingPercentage = phishingPercentage;
            this.legitimatePercentage = legitimatePercentage;
        }

        // Getters - FIXED: Added getters for JSON serialization
        public Long getPhishingCount() {
            return phishingCount;
        }

        public Long getLegitimateCount() {
            return legitimateCount;
        }

        public Long getTotalEmails() {
            return totalEmails;
        }

        public Double getPhishingPercentage() {
            return phishingPercentage;
        }

        public Double getLegitimatePercentage() {
            return legitimatePercentage;
        }
    }
}