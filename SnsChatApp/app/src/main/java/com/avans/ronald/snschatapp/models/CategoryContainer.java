package com.avans.ronald.snschatapp.models;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luuk on 15-6-2015.
 */
public class CategoryContainer implements Serializable {

    private static final String TAG = ChatsContainer.class.getSimpleName();
    public static final String CATEGORIES_FILE_NAME = "categories_data";

    private List<Category> categories;

    public CategoryContainer(){ this.categories = new ArrayList<>(); }
    public List<Category> getCategories(){ return this.categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

    public static void serializeSelf(CategoryContainer container, FileOutputStream fos){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(container);
            oos.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static CategoryContainer deSerializeSelf(FileInputStream fis){
        CategoryContainer container = null;
        try {
            ObjectInputStream is = new ObjectInputStream(fis);
            container = (CategoryContainer) is.readObject();
            is.close();
            fis.close();
        }catch (FileNotFoundException e){
            Log.e(TAG, e.getMessage());
        }catch (IOException e){
            Log.e(TAG, e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return container;
    }

    public void add(Category c) {
        categories.add(c);
    }

    public Category getById(String id) {
        Category category = null;
        for(Category c : categories) {
            if (c.getId().equals(id)) {
                category = c;
            }
        }

        return category;
    }

    public Category getByName(String name){
        for(Category category : categories){
            if(category.getName().equals(name)){
                return category;
            }
        }
        return null;
    }
}
