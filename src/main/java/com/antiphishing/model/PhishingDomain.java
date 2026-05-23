package com.antiphishing.model;

public class PhishingDomain {
    private Long id;
    private String domainPattern;
    private Double weight;
    private Boolean isActive;

    // Constructors
    public PhishingDomain() {}

    public PhishingDomain(String domainPattern, Double weight) {
        this.domainPattern = domainPattern;
        this.weight = weight;
        this.isActive = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDomainPattern() { return domainPattern; }
    public void setDomainPattern(String domainPattern) { this.domainPattern = domainPattern; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean active) { isActive = active; }
}