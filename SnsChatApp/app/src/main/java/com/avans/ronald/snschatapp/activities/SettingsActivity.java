package com.avans.ronald.snschatapp.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.Storage;

public class SettingsActivity extends ActionBarActivity {

    private Switch pincodeAuthorization;
    private EditText pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        pincode = (EditText) findViewById(R.id.pincode);
        pincodeAuthorization = (Switch) findViewById(R.id.pincode_authorization_switch);
        pincodeAuthorization.setChecked(Storage.getAuthorization(this));
        setListenerOnPincodeField(pincode);
        setAuthorizationListener(pincodeAuthorization);
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

    private void setListenerOnPincodeField(final EditText pincode){
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(start + "  " + before + "  " + count);
                if(start == 4){
                    final StringBuilder sb = new StringBuilder(s.length());
                    sb.append(s);
                    pincode.setText("");
                    System.out.println(sb);
                    setPincode(sb.toString());
                    Toast.makeText(getApplicationContext(), "Pincode opgeslagen: " + sb.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setAuthorizationListener(Switch s){
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Storage result: " + Storage.getPincode(getApplicationContext()));
                if(!Storage.getPincode(getApplicationContext()).equals("")) {
                    Storage.storeAuthorization(getApplicationContext(), isChecked, 1);
                }else{
                    pincodeAuthorization.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Voer eerst een pincode in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setPincode(String pincode){
        Storage.storePincode(this, pincode, 1);
    }
}
