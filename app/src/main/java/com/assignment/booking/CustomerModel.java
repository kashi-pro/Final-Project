package com.assignment.booking;

public class CustomerModel {
    //Data Members
    String name;
    String email;
    String phone;
    String dob;
    String password;
    String imagePath;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public CustomerModel() {}
    public CustomerModel(String inName, String inEmail, String inPhone, String  inDob, String inPass, String inImage) {
        name= inName;
        email= inEmail;
        phone=  inPhone;
        dob= inDob;
        password= inPass;
        imagePath= inImage;
    }
}
