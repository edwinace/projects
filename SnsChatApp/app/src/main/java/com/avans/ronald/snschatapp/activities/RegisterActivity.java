package com.avans.ronald.snschatapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.JsonToClass;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.SnsClient;
import com.avans.ronald.snschatapp.Storage;
import com.avans.ronald.snschatapp.models.CategoryContainer;
import com.avans.ronald.snschatapp.models.Chat;
import com.avans.ronald.snschatapp.models.ChatsContainer;
import com.avans.ronald.snschatapp.models.FAQ;
import com.avans.ronald.snschatapp.models.FaqListContainer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RegisterActivity extends ActionBarActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private static final String EXTRA_MESSAGE = "message";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_CUSTOMER_ID = "customer_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //TextView mDisplay;
    private GoogleCloudMessaging gcm;
    private AtomicInteger msgId;  //= new AtomicInteger();
    private SharedPreferences prefs;
    private String regid;
    private String SENDER_ID;
    private FaqListContainer container;

    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if(checkPlayServices()){
            SENDER_ID = getString(R.string.sender_id);
            msgId = new AtomicInteger();
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = Storage.getRegistrationId(this);
            SENDER_ID = getString(R.string.sender_id);
            if (!regid.isEmpty()) {
                if(!Storage.getAuthorization(this)) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(this, PincodeActivity.class));
                    finish();
                }
            }
        }
        username = (EditText) findViewById(R.id.username_register);
    }

    @Override
    protected void onPause() {
        GcmIntentService.checkRunningMode = false;
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        GcmIntentService.checkRunningMode = true;
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void register(View v){
        if(v.getId() == R.id.register_button){
            if(!username.getText().toString().equals("")){
                if(username.getText().length() >= 10){
                    sendRegistrationIdToBackend();
                //    (new GetCategoriesAsync()).execute("/api/categories");
                }else{
                    showErrorAsToast("Je naam moet langer zijn dan 10 karakters");
                }
            }else{
                showErrorAsToast("Je hebt geen naam ingevuld!");
            }
        }
    }

    private void addFaqsToContainer(JSONArray j) {
        try{
            for(int i = 0; i < j.length(); i++){
                FAQ f = JsonToClass.toFaq(j.getJSONObject(i));
                container.getData().get(f.getCategory()).add(f);
            }
        }catch (JSONException e){
            Log.e("err", e.getMessage());
        }
    }

    private FaqListContainer initializeFaqListContainer(JSONArray j) {
        container = new FaqListContainer();
        CategoryContainer categoryContainer = new CategoryContainer();

        //Initialize all the current categories
        try{
            for(int i = 0; i < j.length(); i++){
                container.getData().put(JsonToClass.toCategory(j.getJSONObject(i)).getName(), new ArrayList<FAQ>());
                categoryContainer.getCategories()




                        .add(JsonToClass.toCategory(j.getJSONObject(i)));
            }
        }catch (JSONException e){
            Log.e("err", e.getMessage());
        }

        try {
            CategoryContainer.serializeSelf(categoryContainer, openFileOutput(CategoryContainer.CATEGORIES_FILE_NAME, Context.MODE_PRIVATE));
        }catch (FileNotFoundException e) {
            Log.e("error", e.getMessage());
        }

        return container;
    }

    private void showErrorAsToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendRegistrationIdToBackend() {
        (new RegisterInBackGround(this.getApplicationContext())).execute("/api/customers");
    }

    private class RegisterInBackGround extends AsyncTask<String, Void, JSONObject> {

        String responseCode = null;
        private Context context;

        public RegisterInBackGround(Context context)
        {
            this.context = context;
        }

        private final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Registreren");
            dialog.show();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            dialog.dismiss();
            try{
                Storage.storeCustomerId(getApplicationContext(), jsonObject.getString("_id"), 1);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }catch (JSONException e){
                Log.e("JSON parsing error", e.getMessage());
            }
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            SnsClient client = new SnsClient(context);
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                }
                regid = gcm.register(SENDER_ID);

                Storage.storeRegistrationId(getApplicationContext(), regid, 1);

                JSONObject response = new JSONObject(client.post(params[0], "name=" + username.getText().toString() + "&registrationId=" + Storage.getRegistrationId(getApplicationContext())));

                responseCode = response.getString("status");
                //serializeContainer(initializeFaqListContainer(new JSONObject(client.get("/api/categories"))));
                (new GetCategoriesAsync()).execute("/api/categories");

                System.out.println(Storage.getCustomerId(getApplicationContext()));

                ChatsContainer chatsContainer = new ChatsContainer();
                ChatsContainer.serializeSelf(chatsContainer, openFileOutput(ChatsContainer.CHATS_FILE_NAME, Context.MODE_PRIVATE));


                return new JSONObject(response.getString("data"));

            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
    }

    private class GetCategoriesAsync extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            initializeFaqListContainer(jsonArray);
            (new GetFaqsAsync()).execute("/api/faq");
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            SnsClient client = new SnsClient(getApplicationContext());

            try {
                JSONObject response = new JSONObject(client.get(params[0]));
                return (JSONArray) response.get("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetFaqsAsync extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            try {
                addFaqsToContainer(jsonArray);
                container.serializeSelf(container, openFileOutput(FaqActivity.FAQ_FILE_NAME, Context.MODE_PRIVATE));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            SnsClient client = new SnsClient(getApplicationContext());

            try {
                JSONObject response = new JSONObject(client.get(params[0]));
                return (JSONArray) response.get("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
