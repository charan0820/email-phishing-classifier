package com.antiphishing.model;

import java.time.LocalDateTime;

public class Email {
    private Long id;
    private String subject;
    private String body;
    private String sender;
    private String recipient;
    private Boolean isPhishing;
    private Double confidenceScore;
    private String features;
    private LocalDateTime createdAt;

    // Constructors
    public Email() {
        this.createdAt = LocalDateTime.now();
    }

    public Email(String subject, String body, String sender, String recipient) {
        this();
        this.subject = subject;
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public Boolean getIsPhishing() { return isPhishing; }
    public void setIsPhishing(Boolean phishing) { isPhishing = phishing; }

    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}