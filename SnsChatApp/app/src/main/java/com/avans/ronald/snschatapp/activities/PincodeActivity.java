package com.avans.ronald.snschatapp.activities;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.ronald.snschatapp.GcmIntentService;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.Storage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PincodeActivity extends ActionBarActivity {

    private EditText output;
    private int counter;
    private int amountOfInputFields;
    private  SpannableStringBuilder ssb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);
        output = (EditText) findViewById(R.id.pincode_input_1);
        output.setGravity(Gravity.CENTER_HORIZONTAL);
        amountOfInputFields = 5;
        counter = 0;
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

    private void checkInput(){

        final String pincode = output.getText().toString();

        if(Storage.getPincode(this).equals(pincode)){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            counter = 0;
            Toast.makeText(this, "Verkeerde pincode", Toast.LENGTH_SHORT).show();
            output.setText("");
        }
    }

    private void setNumber(String number){
        output.setText(output.getText().append(number));
        if(counter <= amountOfInputFields){
            counter++;
        }

        if(counter == amountOfInputFields){
            checkInput();
        }
    }

    private void removeNumber(){
        StringBuilder sb = new StringBuilder(output.getText());
        if(counter != 0) {
            sb.setLength(sb.length() - 1);
            counter--;
        }
        output.setText(sb.toString());
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.pincode_button_0 :
                setNumber("0");
                break;
            case R.id.pincode_button_1 :
                setNumber("1");
                break;
            case R.id.pincode_button_2 :
                setNumber("2");
                break;
            case R.id.pincode_button_3 :
                setNumber("3");
                break;
            case R.id.pincode_button_4 :
                setNumber("4");
                break;
            case R.id.pincode_button_5 :
                setNumber("5");
                break;
            case R.id.pincode_button_6 :
                setNumber("6");
                break;
            case R.id.pincode_button_7 :
                setNumber("7");
                break;
            case R.id.pincode_button_8 :
                setNumber("8");
                break;
            case R.id.pincode_button_9 :
                setNumber("9");
                break;
            case R.id.pincode_input_back:
                removeNumber();
        }
    }
}
