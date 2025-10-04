package com.example.demo1;

public class Company {

    private String companyName;
    private String companyCriteria;
    private String description;
    private String higherPackage;
    private String averagePackage;
    private String lowerPackage;
    private String department;
    private String imageUrl;

    public Company() {
        // Default constructor required for calls to DataSnapshot.getValue(Company.class)
    }

    public Company(String companyName, String companyCriteria, String description, String higherPackage, String averagePackage, String lowerPackage, String department, String imageUrl) {
        this.companyName = companyName;
        this.companyCriteria = companyCriteria;
        this.description = description;
        this.higherPackage = higherPackage;
        this.averagePackage = averagePackage;
        this.lowerPackage = lowerPackage;
        this.department = department;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCriteria() {
        return companyCriteria;
    }

    public void setCompanyCriteria(String companyCriteria) {
        this.companyCriteria = companyCriteria;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHigherPackage() {
        return higherPackage;
    }

    public void setHigherPackage(String higherPackage) {
        this.higherPackage = higherPackage;
    }

    public String getAveragePackage() {
        return averagePackage;
    }

    public void setAveragePackage(String averagePackage) {
        this.averagePackage = averagePackage;
    }

    public String getLowerPackage() {
        return lowerPackage;
    }

    public void setLowerPackage(String lowerPackage) {
        this.lowerPackage = lowerPackage;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
