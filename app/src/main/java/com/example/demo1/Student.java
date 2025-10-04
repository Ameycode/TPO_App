package com.example.demo1;

public class Student {
    private String enrollno;
    private String name;
    private String email;
    private String phone;
    private String dob;
    private String adhar;
    private String percentage;
    private String gender;
    private String imageUrl;
    private String branch;
    private String pass;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String enrollno, String name, String email, String phone, String dob, String adhar, String percentage, String gender, String imageUrl, String branch,String pass) {
        this.enrollno = enrollno;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.adhar = adhar;
        this.percentage = percentage;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.branch = branch;
        this.pass = pass;
    }

    public String getEnrollno() {
        return enrollno;
    }

    public void setEnrollno(String enrollno) {
        this.enrollno = enrollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAdhar() {
        return adhar;
    }

    public void setAdhar(String adhar) {
        this.adhar = adhar;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBranch() {
        return branch;
    }
    public String getPass() {
        return pass;
    }


    public void setBranch(String branch) {
        this.branch = branch;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

}
