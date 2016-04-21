package com.avans.ronald.snschatapp.models;

import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luuk on 5-3-2015.
 */
public class Chat implements Serializable
{
    private List <Message> messages;
    private String _id;
    private Customer customer;
    private Category category;
    private List <Employee> employees;

    public Chat(String id) {
        this._id = id;
    }

    public String get_id() { return this._id; }

    public Customer getCustomer() { return this.customer; }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public void setCategory(Category category) { this.category = category; }

    public Category getCategory() { return this.category; }

    public void setMessages(List<Message> messages) { this.messages = messages; }
    public List<Message> getMessages(){
        return this.messages;
    }

    public void setEmployees(List<Employee> employees) { this.employees = employees; }
    public List<Employee> getEmployees() { return this.employees; }
}

