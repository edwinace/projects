package com.avans.ronald.snschatapp;

import com.avans.ronald.snschatapp.models.Category;
import com.avans.ronald.snschatapp.models.Chat;
import com.avans.ronald.snschatapp.models.Customer;
import com.avans.ronald.snschatapp.models.Employee;
import com.avans.ronald.snschatapp.models.FAQ;
import com.avans.ronald.snschatapp.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Luuk de Gram on 2-4-2015.
 */
public class JsonToClass
{
    public static Customer toCustomer(JSONObject object)
    {
        Customer customer = null;
        try {
            customer = new Customer(object.getString("_id"), object.getString("name"));
        }catch(JSONException e) {
            System.out.println(e);
        }

        return customer;
    }

    public static Employee toEmployee(JSONObject object)
    {
        Employee employee = null;
        try {
            employee = new Employee(object.getString("_id"), object.getString("username"));
        }catch(JSONException e) {
            System.out.println(e);
        }

        return employee;
    }

    public static Message toMessage(JSONObject object)
    {
        Message message = null;
        try {
            message = new Message(object.getString("text"), object.getBoolean("isEmployee"), object.getString("timeStamp"));
        }catch(JSONException e){
            System.out.println(e);
        }

        return message;
    }

    public static Chat toChat(JSONObject object)
    {
        Chat chat = null;
        try {
            chat = new Chat(object.getString("_id"));
        }catch(JSONException e){
            System.out.println(e);
        }

        return chat;
    }

    public static Category toCategory(JSONObject object)
    {
        Category category = null;
        try {
            category = new Category(object.getString("_id"), object.getString("name"));
        }catch(JSONException e){
            System.out.println(e);
        }

        return category;
    }

    public static FAQ toFaq(JSONObject object){
        FAQ faq = null;
        try {
            JSONObject j =  new JSONObject(object.getString("category"));
            Category category = new Category(j.getString("_id"), j.getString("name"));
            faq = new FAQ(object.getString("_id"), category.getName(), object.getString("question"), object.getString("answer"));
        }catch(JSONException e){
            System.out.println(e);
        }

        return faq;
    }
}
