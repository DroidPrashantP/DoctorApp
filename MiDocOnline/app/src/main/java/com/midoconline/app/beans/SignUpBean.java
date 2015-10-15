package com.midoconline.app.beans;

/**
 * Created by sandeep on 15/10/15.
 */
public class SignUpBean {
    String name;
    String surname;
    String email;
    String gender;
    String mobilenumber;
    String birthday;
    String password;
    String specialize;

    public SignUpBean(String name, String surname, String email, String gender, String mobilenumber, String birthday, String password, String specialize) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.mobilenumber = mobilenumber;
        this.birthday = birthday;
        this.password = password;
        this.specialize =specialize;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
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
