package com.bloodhub.android.model;

/**
 * Created by mustafaculban on 23.04.2018.
 */

public class EmergencyFive {

    int id;
    String fullname;
    String email;
    int status;
    String dateOfRequest;
    String transactionDate;
    String telephone;

    public EmergencyFive(int id, String fullname, String email, int status, String dateOfRequest, String transactionDate, String telephone) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.status = status;
        this.dateOfRequest = dateOfRequest;
        this.transactionDate = transactionDate;
        this.telephone = telephone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(String dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


}
