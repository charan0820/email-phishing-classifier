package com.antiphishing.classifier;

import com.antiphishing.dao.ClassificationRuleDao;
import com.antiphishing.dao.PhishingDomainDao;
import com.antiphishing.dao.PhishingKeywordDao;
import com.antiphishing.model.PhishingDomain;
import com.antiphishing.model.PhishingKeyword;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class PhishingClassifier {

    @Autowired
    private PhishingKeywordDao phishingKeywordDao;

    @Autowired
    private PhishingDomainDao phishingDomainDao;

    @Autowired
    private ClassificationRuleDao classificationRuleDao;

    private static final Pattern URL_PATTERN = Pattern.compile(
            "https?://[^\\s]+", Pattern.CASE_INSENSITIVE
    );

    private final LevenshteinDistance levenshtein = new LevenshteinDistance();

    public ClassificationResult classifyEmail(String subject, String body, String sender) {
        String fullText = (subject + " " + body).toLowerCase();

        Map<String, Object> features = extractFeatures(subject, body, sender, fullText);
        double score = calculatePhishingScore(features);

        // Get threshold from database
        Double threshold = classificationRuleDao.getRuleValue("PHISHING_THRESHOLD");
        boolean isPhishing = score > (threshold != null ? threshold : 0.6);

        return new ClassificationResult(isPhishing, score, features);
    }

    private Map<String, Object> extractFeatures(String subject, String body, String sender, String fullText) {
        Map<String, Object> features = new HashMap<>();

        // 1. Suspicious keywords count with weighted scoring
        double keywordScore = calculateKeywordScore(fullText);
        features.put("suspicious_keywords_score", keywordScore);
        features.put("suspicious_keywords_count", (long) (keywordScore * 10)); // For backward compatibility

        // 2. URL presence and analysis
        boolean hasUrls = URL_PATTERN.matcher(fullText).find();
        features.put("has_urls", hasUrls);

        // 3. Suspicious sender analysis
        boolean suspiciousSender = analyzeSender(sender);
        features.put("suspicious_sender", suspiciousSender);

        // 4. Urgency detection
        boolean hasUrgency = hasUrgencyKeywords(fullText);
        features.put("has_urgency", hasUrgency);

        // 5. Grammatical errors (simple check)
        int grammarErrors = checkGrammar(fullText);
        features.put("grammar_errors", grammarErrors);

        // 6. Length analysis
        features.put("subject_length", subject.length());
        features.put("body_length", body.length());

        // 7. Detailed keyword analysis by category
        Map<String, Long> keywordCategories = analyzeKeywordsByCategory(fullText);
        features.putAll(keywordCategories);

        return features;
    }

    private double calculateKeywordScore(String text) {
        List<PhishingKeyword> keywords = phishingKeywordDao.findAllActive();
        double totalScore = 0.0;

        for (PhishingKeyword keyword : keywords) {
            if (text.contains(keyword.getKeyword().toLowerCase())) {
                totalScore += keyword.getWeight();
            }
        }

        return totalScore;
    }

    private Map<String, Long> analyzeKeywordsByCategory(String text) {
        List<PhishingKeyword> keywords = phishingKeywordDao.findAllActive();
        Map<String, Long> categoryCounts = new HashMap<>();

        for (PhishingKeyword keyword : keywords) {
            if (text.contains(keyword.getKeyword().toLowerCase())) {
                String category = keyword.getCategory() != null ? keyword.getCategory() : "other";
                categoryCounts.put("keywords_" + category,
                        categoryCounts.getOrDefault("keywords_" + category, 0L) + 1);
            }
        }

        return categoryCounts;
    }

    private boolean hasUrgencyKeywords(String text) {
        List<PhishingKeyword> urgencyKeywords = phishingKeywordDao.findByCategory("urgency");
        for (PhishingKeyword keyword : urgencyKeywords) {
            if (text.contains(keyword.getKeyword().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean analyzeSender(String sender) {
        if (sender == null) return true;

        String senderLower = sender.toLowerCase();
        List<PhishingDomain> domains = phishingDomainDao.findAllActive();

        for (PhishingDomain domain : domains) {
            if (senderLower.contains(domain.getDomainPattern())) {
                return true;
            }
        }

        // Additional sender checks
        return !senderLower.contains("@") ||
                senderLower.matches(".*[0-9]{10,}@.*"); // Many numbers in local part
    }

    private int checkGrammar(String text) {
        int errors = 0;
        // Simple grammar checks
        if (text.contains(" dear ")) errors++; // Generic greeting
        if (text.matches(".*\\b[A-Z]{3,}\\b.*")) errors++; // ALL CAPS words
        if (text.split("\\s+").length < 10) errors++; // Very short text
        return errors;
    }

    private double calculatePhishingScore(Map<String, Object> features) {
        Map<String, Double> rules = classificationRuleDao.getActiveRulesAsMap();

        double score = 0.0;

        // Weighted scoring based on features using database rules
        Double keywordWeight = rules.get("KEYWORD_WEIGHT");
        Double urlWeight = rules.get("URL_WEIGHT");
        Double senderWeight = rules.get("SENDER_WEIGHT");
        Double urgencyWeight = rules.get("URGENCY_WEIGHT");
        Double grammarWeight = rules.get("GRAMMAR_WEIGHT");

        if (keywordWeight != null) {
            score += (Double) features.get("suspicious_keywords_score") * keywordWeight;
        }

        if (urlWeight != null) {
            score += (Boolean) features.get("has_urls") ? urlWeight : 0.0;
        }

        if (senderWeight != null) {
            score += (Boolean) features.get("suspicious_sender") ? senderWeight : 0.0;
        }

        if (urgencyWeight != null) {
            score += (Boolean) features.get("has_urgency") ? urgencyWeight : 0.0;
        }

        if (grammarWeight != null) {
            score += (Integer) features.get("grammar_errors") * grammarWeight;
        }

        // Normalize to 0-1
        Double maxConfidence = rules.get("MAX_CONFIDENCE");
        return Math.min(maxConfidence != null ? maxConfidence : 1.0, score);
    }

    public static class ClassificationResult {
        private final boolean isPhishing;
        private final double confidence;
        private final Map<String, Object> features;

        public ClassificationResult(boolean isPhishing, double confidence, Map<String, Object> features) {
            this.isPhishing = isPhishing;
            this.confidence = confidence;
            this.features = features;
        }

        // Getters
        public boolean isPhishing() { return isPhishing; }
        public double getConfidence() { return confidence; }
        public Map<String, Object> getFeatures() { return features; }
    }
}