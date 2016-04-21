package com.avans.ronald.snschatapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.JsonToClass;
import com.avans.ronald.snschatapp.fragments.ChatMessagesFragment;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.fragments.SendChatMessageFragment;
import com.avans.ronald.snschatapp.models.Chat;
import com.avans.ronald.snschatapp.models.ChatsContainer;
import com.avans.ronald.snschatapp.models.Customer;
import com.avans.ronald.snschatapp.models.Employee;
import com.avans.ronald.snschatapp.models.Message;

import org.json.JSONException;
import org.json.JSONObject;


public class ChatActivity extends FragmentActivity implements ChatMessagesFragment.OnFragmentInteractionListener, SendChatMessageFragment.OnFragmentInteractionListener {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatListener listener;
    private IntentFilter filter;
    private ChatsContainer chatsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listener = new ChatListener();
        filter = new IntentFilter("android.intent.action.MESSAGE");
        try {
            chatsContainer = ChatsContainer.deSerializeSelf(openFileInput(ChatsContainer.CHATS_FILE_NAME));
        }catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }

        if (chatsContainer == null) {
            chatsContainer = new ChatsContainer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onSendMessage(Message message) {
        ChatMessagesFragment fragment = (ChatMessagesFragment) getSupportFragmentManager().findFragmentById(R.id.messages_fragment);

        fragment.addMessageToList(message);

        Chat chat = chatsContainer.getById(fragment.getChatId());
        chat.getMessages().add(message);

        try {
            ChatsContainer.serializeSelf(chatsContainer, openFileOutput(ChatsContainer.CHATS_FILE_NAME, Context.MODE_PRIVATE));
        }catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void addMessageToChat(Message newMessage, String chatId){
        ChatMessagesFragment fragment = (ChatMessagesFragment) getSupportFragmentManager().findFragmentById(R.id.messages_fragment);
        Chat chat = chatsContainer.getById(chatId);
        boolean exists = false;

        for(Message m : chat.getMessages()) {
            if (m.getTimeStamp().equals(newMessage.getTimeStamp())) {
                exists = true;
            }
        }

        if (!exists) {
            chat.getMessages().add(newMessage);

            try {
                ChatsContainer.serializeSelf(chatsContainer, openFileOutput(ChatsContainer.CHATS_FILE_NAME, Context.MODE_PRIVATE));
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            for (int i = 0; i < fragment.getAdapter().getCount(); i++) {
                Log.d(TAG, "count = " + i);
                if (chatId.equals(fragment.getChatId())) {
                    fragment.getAdapter().add(newMessage);
                    fragment.getAdapter().notifyDataSetChanged();
                    break;
                }
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


    private class ChatListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String message = intent.getStringExtra("MESSAGE");
                String chatId = intent.getStringExtra("CHAT_ID");
                addMessageToChat(convertJSONObjectToMessage(new JSONObject(message)), chatId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
