package com.antiphishing.model;

public class PhishingKeyword {
    private Long id;
    private String keyword;
    private Double weight;
    private String category;
    private Boolean isActive;

    // Constructors
    public PhishingKeyword() {}

    public PhishingKeyword(String keyword, Double weight, String category) {
        this.keyword = keyword;
        this.weight = weight;
        this.category = category;
        this.isActive = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}