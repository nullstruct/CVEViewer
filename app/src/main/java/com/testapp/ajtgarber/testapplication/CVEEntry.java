package com.testapp.ajtgarber.testapplication;

import android.net.Uri;

import java.util.Calendar;
import java.util.List;

/**
 * Describes an entry provided by NIST's NVD RSS feed
 */
public class CVEEntry implements Comparable<Comparable> {
    private String id;
    private String description;
    private String publishedDate;
    private Calendar published;
    private String lastModified;

    private double cvsScore;
    private String accessVector;

    private String authentication;

    private String accessComplexity;
    private String confidentialityImpact;
    private String integrityImpact;
    private String availabilityImpact;

    private List<Uri> resources;

    private boolean shouldNotify = false;

    public CVEEntry(String id, String description, double cvsScore) {
        this.id = id;
        this.description = description;
        this.cvsScore = cvsScore;
    }

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
        String[] parts = publishedDate.split("-");
        int[] intParts = {Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])};
        published = Calendar.getInstance();
        published.set(intParts[0], intParts[1] - 1, intParts[2]);
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public double getCvsScore() {
        return cvsScore;
    }

    @SuppressWarnings("unused")
    public void setCvsScore(double cvsScore) {
        this.cvsScore = cvsScore;
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

    public String getConfidentialityImpact() {
        return confidentialityImpact;
    }

    public void setConfidentialityImpact(String confidentialityImpact) {
        this.confidentialityImpact = confidentialityImpact;
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

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public List<Uri> getResources() {
        return resources;
    }

    public void setResources(List<Uri> resources) {
        this.resources = resources;
    }

    @SuppressWarnings("unused")
    public boolean isShouldNotify() {
        return shouldNotify;
    }

    public void setShouldNotify(boolean shouldNotify) {
        this.shouldNotify = shouldNotify;
    }

    public int compareTo(Comparable other) {
        if(other instanceof CVEEntry) {
            CVEEntry otherEntry = (CVEEntry)other;
            return published.compareTo(otherEntry.published);
        }
        int code = hashCode();
        int code2 = other.hashCode();
        if(code == code2) return 0;
        if(code < code2) return -1;
        return 1;
    }
}
