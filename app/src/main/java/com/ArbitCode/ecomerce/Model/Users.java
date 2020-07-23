package com.ArbitCode.ecomerce.Model;

public class Users {

    private String countryCode, email, name, password, phone;

    public Users() {
    }

    public Users(String countryCode, String email, String name, String password, String phone) {
        this.countryCode = countryCode;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
