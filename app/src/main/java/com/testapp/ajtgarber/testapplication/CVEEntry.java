package com.testapp.ajtgarber.testapplication;

/**
 * Created by ajtgarber on 10/31/14.
 */
public class CVEEntry {
    private String id;
    private String description;
    private String publishedDate;
    private String modifiedDate;
    private String accessVector;
    private String accessComplexity;
    private String integrityImpact;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getAccessVector() {
        return accessVector;
    }

    public void setAccessVector(String accessVector) {
        this.accessVector = accessVector;
    }

    public String getAccessComplexity() {
        return accessComplexity;
    }

    public void setAccessComplexity(String accessComplexity) {
        this.accessComplexity = accessComplexity;
    }

    public String getIntegrityImpact() {
        return integrityImpact;
    }

    public void setIntegrityImpact(String integrityImpact) {
        this.integrityImpact = integrityImpact;
    }

    public String getAvailabilityImpact() {
        return availabilityImpact;
    }

    public void setAvailabilityImpact(String availabilityImpact) {
        this.availabilityImpact = availabilityImpact;
    }

    public String getConfidentialityImpact() {
        return confidentialityImpact;
    }

    public void setConfidentialityImpact(String confidentialityImpact) {
        this.confidentialityImpact = confidentialityImpact;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public double getCvsScore() {
        return cvsScore;
    }

    public void setCvsScore(double cvsScore) {
        this.cvsScore = cvsScore;
    }

    private String availabilityImpact;
    private String confidentialityImpact;
    private String authentication;
    private double cvsScore;
}
