package com.antiphishing.model;

public class ClassificationRule {
    private Long id;
    private String ruleName;
    private Double ruleValue;
    private String description;
    private Boolean isActive;

    // Constructors
    public ClassificationRule() {}

    public ClassificationRule(String ruleName, Double ruleValue, String description) {
        this.ruleName = ruleName;
        this.ruleValue = ruleValue;
        this.description = description;
        this.isActive = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public Double getRuleValue() { return ruleValue; }
    public void setRuleValue(Double ruleValue) { this.ruleValue = ruleValue; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}