package com.avans.ronald.snschatapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.JsonToClass;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.SnsClient;
import com.avans.ronald.snschatapp.Storage;
import com.avans.ronald.snschatapp.models.Category;
import com.avans.ronald.snschatapp.models.CategoryContainer;
import com.avans.ronald.snschatapp.models.Chat;
import com.avans.ronald.snschatapp.models.ChatsContainer;
import com.avans.ronald.snschatapp.models.Customer;
import com.avans.ronald.snschatapp.models.Employee;
import com.avans.ronald.snschatapp.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class CreateChatActivity extends ActionBarActivity {
    private static final String TAG = FaqActivity.class.getSimpleName();

    public static final String COMES_FROM_FAQ = "comesFromFaq";

    private EditText question;
    private Spinner spinner;
    private Intent viewChats;
    private CategoryContainer categoryContainer;
    private ChatsContainer chatsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);
        spinner = (Spinner) findViewById(R.id.spinner);
        question = (EditText) findViewById(R.id.question);
        try {
            chatsContainer = ChatsContainer.deSerializeSelf(openFileInput(ChatsContainer.CHATS_FILE_NAME));
            categoryContainer = CategoryContainer.deSerializeSelf(openFileInput(CategoryContainer.CATEGORIES_FILE_NAME));
            if (chatsContainer == null) {
                chatsContainer = new ChatsContainer();
            }
        }catch (FileNotFoundException e){
            Log.e(TAG, e.getMessage());
        }
        String category;
        if(this.getIntent().getBooleanExtra(COMES_FROM_FAQ, false)){
            category = this.getIntent().getStringExtra("category");
            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
                    android.R.layout.simple_spinner_item, categoryContainer.getCategories());
            spinner.setAdapter(adapter);
            for(int i = 0; i < categoryContainer.getCategories().size(); i++){
                if(categoryContainer.getCategories().get(i).getName().equals(category)){
                    spinner.setSelection(i, true);
                    break;
                }
            }
        }else{
            ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(this,
                    android.R.layout.simple_spinner_item, categoryContainer.getCategories());
            spinner.setAdapter(adapter);
        }

        viewChats = new Intent(this, ViewAllChatsActivity.class);
    }

    private Chat createChat(JSONObject jsonObject) {
        Chat chat = JsonToClass.toChat(jsonObject);
        if (chat != null) {

            List<Employee> employees = new ArrayList<Employee>();
            List<Message> messages = new ArrayList<Message>();

            try {
                JSONArray employeeArray = jsonObject.getJSONArray("employees");
                for (int i = 0; i < employeeArray.length(); i++) {
                    Employee employee = JsonToClass.toEmployee(employeeArray.getJSONObject(i));

                    if (employee != null) {
                        employees.add(employee);
                    }
                }
            } catch(JSONException e) {
                System.out.println(e);
            }

            try {
                if (jsonObject.isNull("messages") == false) {

                    JSONArray messageArray = jsonObject.getJSONArray("messages");
                    for (int i = 0; i < messageArray.length(); i++) {
                        Message message = JsonToClass.toMessage(messageArray.getJSONObject(i));
                        if (message != null) {
                            if (messageArray.getJSONObject(i).isNull("user") == false) {
                                Customer customerMessage = JsonToClass.toCustomer(messageArray.getJSONObject(i).getJSONObject("user"));
                                if (customerMessage != null) {
                                    message.setCustomer(customerMessage);
                                }
                            }

                            if (messageArray.getJSONObject(i).isNull("employee") == false) {
                                Employee employeeMessage = JsonToClass.toEmployee(messageArray.getJSONObject(i).getJSONObject("employee"));
                                if (employeeMessage != null) {
                                    message.setEmployee(employeeMessage);
                                }
                            }

                            messages.add(message);
                        }
                    }
                }
            } catch(JSONException e) {
                System.out.println(e);
            }

            try {
                Customer customer = JsonToClass.toCustomer(jsonObject.getJSONObject("customer"));

                if (customer != null) {
                    chat.setCustomer(customer);
                }
            } catch (JSONException e) {
                System.out.println(e);
            }

            try {
                Category category = JsonToClass.toCategory(jsonObject.getJSONObject("category"));

                if (category != null) {
                    chat.setCategory(category);
                }
            }catch (JSONException e) {
                System.out.println(e);
            }

            chat.setEmployees(employees);
            chat.setMessages(messages);
        }

        return chat;
    }

    @Override
    protected void onPause() {
        super.onPause();
        GcmIntentService.checkRunningMode = false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        GcmIntentService.checkRunningMode = true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_chat, menu);
        return true;
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

    public void createChat(View v)
    {
        (new AsynchAddNewChat(this.getApplicationContext())).execute("/customers/"+ Storage.getCustomerId(this) +"/chats");
    }

    private class AsynchAddNewChat extends AsyncTask<String, Void, Void> {

        String responseCode = null;
        private Context context;
        private Chat chat;

        public AsynchAddNewChat(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            if(responseCode.equals("OK")) {
                chatsContainer.add(chat);
                try {
                    ChatsContainer.serializeSelf(chatsContainer, openFileOutput(ChatsContainer.CHATS_FILE_NAME, Context.MODE_PRIVATE));
                }catch (FileNotFoundException e){
                    Log.e(TAG, e.getMessage());
                }

                Bundle bundle = new Bundle();
                bundle.putString("CHAT_ID", chat.get_id());
                bundle.putSerializable("MESSAGES", (Serializable) chat.getMessages());
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Chat aangemaakt", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Chat is niet aangemaakt", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            String input = question.getText().toString();
            Category category = categoryContainer.getCategories().get(spinner.getSelectedItemPosition());
            SnsClient client = new SnsClient(context);
            client.get(params[0]);

            if (!input.equals("")) {
                try {
                    JSONObject response = new JSONObject(client.post(params[0], "category="+category.getId()+"&message=" + input));
                    JSONObject data = new JSONObject(response.getString("data"));
                    responseCode = response.getString("status");

                    chat = createChat(data);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Er is geen vraag gesteld", Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
