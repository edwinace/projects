package com.avans.ronald.snschatapp.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avans.ronald.snschatapp.Format;
import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.JsonToClass;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.SnsClient;
import com.avans.ronald.snschatapp.Storage;
import com.avans.ronald.snschatapp.adapters.ChatsAdapter;
import com.avans.ronald.snschatapp.models.Category;
import com.avans.ronald.snschatapp.models.Chat;
import com.avans.ronald.snschatapp.models.ChatsContainer;
import com.avans.ronald.snschatapp.models.Customer;
import com.avans.ronald.snschatapp.models.Employee;
import com.avans.ronald.snschatapp.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class ViewAllChatsActivity extends ActionBarActivity {

    private static final String TAG = ViewAllChatsActivity.class.getSimpleName();

    private ChatListener listener;
    private IntentFilter filter;
    private ChatsAdapter adapter;
    private List<Category> mCategories;
    private ChatsContainer chatsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_chats);


        final ListView listView = (ListView) findViewById(android.R.id.list);

        try {
            chatsContainer = ChatsContainer.deSerializeSelf(openFileInput(ChatsContainer.CHATS_FILE_NAME));
        }catch(FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat chat = adapter.getChats().get(position);
                Bundle bundle = new Bundle();
                bundle.putString("CHAT_ID", chat.get_id());
                bundle.putSerializable("MESSAGES", (Serializable) chat.getMessages());
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listener = new ChatListener();
        filter = new IntentFilter("android.intent.action.MESSAGE_ALL_CHATS");

        if (chatsContainer == null) {
            chatsContainer = new ChatsContainer();
        }

        adapter = new ChatsAdapter(this, chatsContainer.getChats());

        adapter.sort(new Comparator<Chat>() {
            @Override
            public int compare(Chat arg0, Chat arg1) {
                Date first = Format.formatTimeStamp(arg0.getMessages().get(arg0.getMessages().size()-1).getTimeStamp());
                Date second = Format.formatTimeStamp(arg1.getMessages().get(arg1.getMessages().size()-1).getTimeStamp());
                return first.compareTo(second);
            }
        });

        listView.setAdapter(adapter);

        String s = Storage.getCustomerId(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GcmIntentService.checkRunningMode = false;
        unregisterReceiver(listener);
    }

    @Override
    protected void onResume(){
        super.onResume();
        registerReceiver(listener, filter);
        GcmIntentService.checkRunningMode = true;
        try {
            chatsContainer = ChatsContainer.deSerializeSelf(openFileInput(ChatsContainer.CHATS_FILE_NAME));
            for(int i = 0; i<chatsContainer.getChats().size(); i++) {
                if (i >= adapter.getChats().size()) {
                    adapter.add(chatsContainer.getChats().get(i));
                } else {
                    adapter.getChats().set(i, chatsContainer.getChats().get(i));
                }
            }

            adapter.sort(new Comparator<Chat>() {
                @Override
                public int compare(Chat arg0, Chat arg1) {
                    Date first = Format.formatTimeStamp(arg0.getMessages().get(arg0.getMessages().size()-1).getTimeStamp());
                    Date second = Format.formatTimeStamp(arg1.getMessages().get(arg1.getMessages().size()-1).getTimeStamp());
                    return second.compareTo(first);
                }
            });

            adapter.notifyDataSetChanged();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_all_chats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.create_chat_button:
                Intent createChat = new Intent(this, CreateChatActivity.class);
                startActivity(createChat);
                break;
        }
    }

    private void addMessageToChat(Message newMessage, String chatId, Employee employee){
        Chat chat = chatsContainer.getById(chatId);

        for(int i =0; i < adapter.getChats().size(); i++){
            if(adapter.getChats().get(i).get_id().equals(chatId)){
                adapter.getChats().get(i).getMessages().add(newMessage);
                if (employee != null) {
                    adapter.getChats().get(i).getEmployees().add(employee);
                    chat.getEmployees().add(employee);
                }
                adapter.sort(new Comparator<Chat>() {
                    @Override
                    public int compare(Chat arg1, Chat arg0) {
                        Date first = Format.formatTimeStamp(arg0.getMessages().get(arg0.getMessages().size()-1).getTimeStamp());
                        Date second = Format.formatTimeStamp(arg1.getMessages().get(arg1.getMessages().size()-1).getTimeStamp());
                        return first.compareTo(second);
                    }
                });
                adapter.notifyDataSetChanged();
                break;
            }
        }

        try {
            ChatsContainer.serializeSelf(chatsContainer, openFileOutput(ChatsContainer.CHATS_FILE_NAME, Context.MODE_PRIVATE));
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private class ChatListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String messageString = intent.getStringExtra("MESSAGE");
                String chatId = intent.getStringExtra("CHAT_ID");
                Employee employee = null;
                if (intent.hasExtra("EMPLOYEE")) {
                    employee = JsonToClass.toEmployee(new JSONObject(intent.getStringExtra("EMPLOYEE")));
                }
                Message message = convertJSONObjectToMessage(new JSONObject(messageString));

                addMessageToChat(message, chatId, employee);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private Message convertJSONObjectToMessage(JSONObject jsonObject) throws JSONException {

        Message message = JsonToClass.toMessage(jsonObject);
        if (message != null) {

            if (!jsonObject.isNull("user")) {
                Customer customer = JsonToClass.toCustomer(jsonObject.getJSONObject("user"));
                message.setCustomer(customer);
            }

            if (!jsonObject.isNull("employee")) {
                Employee employee = JsonToClass.toEmployee(jsonObject.getJSONObject("employee"));
                message.setEmployee(employee);
            }
        }
        return message;
    }
}