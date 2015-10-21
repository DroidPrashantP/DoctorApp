package com.midoconline.app.beans;

/**
 * Created by sandeep on 15/10/15.
 */
public class SignUpBean {
    String name;
    String city;
    String email;
    String country;
    String mobilenumber;
    String birthday;
    String password;
    String specialize;

    public SignUpBean(String name, String city, String email, String country, String mobilenumber, String password, String specialize) {
        this.name = name;
        this.city = city;
        this.email = email;
        this.country = country;
        this.mobilenumber = mobilenumber;
        this.password = password;
        this.specialize =specialize;
    }

    public String getName() {
        return name;
    }

    public String getcity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getcountry() {
        return country;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }
    public String getSpecialize() {
        return specialize;
    }
}
