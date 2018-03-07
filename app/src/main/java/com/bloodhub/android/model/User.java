package com.bloodhub.android.model;

/**
 * Created by izelgurbuz on 3.02.2018.
 */

public class User {

    public String username, firstname, surname, email, bloodType, birthdate, address, telephone, available, last_login_ip;
    //public String last_login_date, last_login_time, last_city;
    public int id;

    //String firstname, String surname,String bloodType,String birthdate,String address,String telephone,String available,String last_login_ip,
    //String last_login_date, String last_login_time, String last_city ,
    public User(int id, String username,  String email  , String surname, String firstname  , String bloodType, String birthdate, String address,String telephone){

        this.username = username;
        this.email = email;
        this.surname = surname;
        this.firstname = firstname;
        this.bloodType = bloodType;
        this.birthdate = birthdate;
        this.address= address;
        this.telephone = telephone;
        //this.available = available;
        //this.last_login_ip = last_login_ip;
        //this.last_login_date =last_login_date;
        //this.last_login_time = last_login_time;
        //this.last_city = last_city;
        this.id= id;


    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }
    public String getSurname() {
        return surname;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getBirthdate() {
        return birthdate;
    }
    public String getAddress() {
        return address;
    }
    /*
        public String getTelephone() {
            return telephone;
        }

        public String getAvailable() {
            return available;
        }
        public String getLastLoginIP() {
            return last_login_ip;
        }

        public String getLastLoginDate() {
            return last_login_date;
        }

        public String getLastLoginTime() {
            return last_login_time;
        }
        public String getLastCity() {
            return last_city;
        }
        */
    public int getID() {
        return id;
    }
}
