package com.avans.ronald.snschatapp.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ronald on 7-3-2015.
 */
public class Message implements Serializable {

    private String text;
    private Boolean isEmployee;
    private String timeStamp;
    private Customer user;
    private Employee employee;

    public Message(String text, Boolean isEmployee, String timeStamp){
        this.text = text;
        this.isEmployee = isEmployee;
        this.timeStamp = timeStamp;
    }

    public Customer getCustomer() {
        return user;
    }

    public void setCustomer(Customer customer) {
        this.user = customer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(Boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Employee getEmployee() { return this.employee; }

    public void setEmployee(Employee employee){ this.employee = employee; }
}
