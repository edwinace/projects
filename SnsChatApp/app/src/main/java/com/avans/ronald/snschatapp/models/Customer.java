package com.avans.ronald.snschatapp.models;

import java.io.Serializable;

/**
 * Created by Luuk on 12-3-2015.
 */
public class Customer implements Serializable
{
    private String _id;
    private String name;

    public Customer(String id, String name)
    {
        this._id = id;
        this.name = name;
    }

    public void setId(String id) { this._id = id; }
    public String getId() { return this._id; }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }
}
