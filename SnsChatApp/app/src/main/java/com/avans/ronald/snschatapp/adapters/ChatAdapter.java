package com.avans.ronald.snschatapp.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avans.ronald.snschatapp.Format;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.models.Message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.logging.SimpleFormatter;

/**
 * Created by Ronald on 7-3-2015.
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private Context context;
    private List<Message> messages;
    private HashMap<String, String> employeeNames;
    private SimpleDateFormat parseFormat;

    public ChatAdapter(Context context, ArrayList<Message> messages, HashMap<String, String> employeeNames) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
        this.employeeNames = employeeNames;
        parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.message_sender_name);
        TextView content = (TextView) convertView.findViewById(R.id.message_content);
        TextView timeStamp = (TextView) convertView.findViewById(R.id.message_timestamp);
        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.message_item);
        LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.message_main);
        content.setText(message.getText());
        timeStamp.setText(Format.fancyDate(message.getTimeStamp()));

        if(message.getIsEmployee()){
            if (message.getEmployee() != null) {
                name.setText(message.getEmployee().getName());
            }
            else{
                name.setText(context.getString(R.string.company_name));
            }
            name.setTextColor(Color.WHITE);
            layout.setBackgroundResource(R.drawable.speech_bubble_blue_system);
            mainLayout.setGravity(Gravity.LEFT);


        }else if(message.getCustomer() != null){
            name.setText(context.getString(R.string.own_message_identifier));
            mainLayout.setGravity(Gravity.RIGHT);
            layout.setBackgroundResource(R.drawable.speech_bubble_purple);
            layout.setGravity(Gravity.LEFT);

        } else {
            name.setText("SNS bank");
            name.setTextColor(Color.WHITE);
            layout.setBackgroundResource(R.drawable.speech_bubble_blue_system);
            mainLayout.setGravity(Gravity.LEFT);

        }

        return convertView;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public HashMap<String, String> getEmployeeNames() {
        return employeeNames;
    }

    public void setEmployeeNames(HashMap<String, String> employeeNames) {
        this.employeeNames = employeeNames;
    }
}
