package com.avans.ronald.snschatapp.models;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ronald on 18-5-2015.
 */
public class FaqListContainer implements Serializable {

    private static final String TAG = FaqListContainer.class.getSimpleName();

    private HashMap<String, List<FAQ>> data;

    public FaqListContainer(){
        data = new HashMap<>();
    }

    public HashMap<String, List<FAQ>> getData() {
        return data;
    }

    public void setData(HashMap<String, List<FAQ>> data) {
        this.data = data;
    }


    public static void serializeSelf(FaqListContainer container, FileOutputStream fos){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(container);
            fos.close();
        }catch (FileNotFoundException e){
            Log.e(TAG, e.getMessage());
        }catch (IOException e){
            Log.e(TAG, e.getMessage());
        }
    }

    public static FaqListContainer deSerializeSelf(FileInputStream fis){
        FaqListContainer container = null;
        try {
            ObjectInputStream is = new ObjectInputStream(fis);
            container = (FaqListContainer) is.readObject();
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

    public void deleteFaqById(String id){
      for(List<FAQ> faqs: data.values()){
          for(FAQ faqToBeDeleted : faqs){
              if(faqToBeDeleted.getId().equals(id)){
                  faqs.remove(faqToBeDeleted);
                  return;
              }
          }
      }
    }

    public void updateFaqById(String category, FAQ newFaq){
        List<FAQ> faqs = data.get(category);
        for(FAQ f : faqs){
            if(f.getId().equals(newFaq.getId())){
                f.setAnswer(newFaq.getAnswer());
                f.setTitle(newFaq.getTitle());
                f.setId(newFaq.getId());
                f.setCategory(newFaq.getCategory());
                break;
            }
        }
    }

    public void addFaqToList(FAQ newFaq){
        data.get(newFaq.getCategory()).add(newFaq);
    }


}
