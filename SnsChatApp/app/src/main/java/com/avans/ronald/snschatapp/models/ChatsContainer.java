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
 * Created by Luuk on 2-6-2015.
 */
public class ChatsContainer implements Serializable {

    private static final String TAG = ChatsContainer.class.getSimpleName();
    public static final String CHATS_FILE_NAME = "chats_data";

    private List<Chat> chats;

    public ChatsContainer(){ this.chats = new ArrayList<>(); }
    public List<Chat> getChats(){ return this.chats; }
    public void setChats(List<Chat> chats) { this.chats = chats; }

    public static void serializeSelf(ChatsContainer container, FileOutputStream fos){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(container);
            oos.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ChatsContainer deSerializeSelf(FileInputStream fis){
        ChatsContainer container = null;
        try {
            ObjectInputStream is = new ObjectInputStream(fis);
            container = (ChatsContainer) is.readObject();
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

    public void add(Chat c) {
        chats.add(c);
    }

    public void deleteById(String id){
        for(Chat c : chats) {
            if (c.get_id().equals(id)) {
                chats.remove(c);
                break;
            }
        }
    }

    public Chat getById(String id) {
        Chat chat = null;
        for(Chat c : chats) {
            if (c.get_id().equals(id)) {
                chat = c;
                break;
            }
        }

        return chat;
    }

}
