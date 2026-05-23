package com.antiphishing.model;

public class TrainingData {
    private Long id;
    private String emailText;
    private Boolean isPhishing;
    private Boolean usedForTraining;

    // Constructors, Getters and Setters
    public TrainingData() {
        this.usedForTraining = false;
    }

    public TrainingData(String emailText, Boolean isPhishing) {
        this();
        this.emailText = emailText;
        this.isPhishing = isPhishing;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmailText() { return emailText; }
    public void setEmailText(String emailText) { this.emailText = emailText; }

    public Boolean getIsPhishing() { return isPhishing; }
    public void setIsPhishing(Boolean phishing) { isPhishing = phishing; }

    public Boolean getUsedForTraining() { return usedForTraining; }
    public void setUsedForTraining(Boolean usedForTraining) { this.usedForTraining = usedForTraining; }
}