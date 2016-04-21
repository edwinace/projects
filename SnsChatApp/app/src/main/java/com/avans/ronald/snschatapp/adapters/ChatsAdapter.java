package com.avans.ronald.snschatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avans.ronald.snschatapp.Format;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.models.Chat;
import com.avans.ronald.snschatapp.models.Message;

import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Luuk on 5-3-2015.
 */
public class ChatsAdapter extends ArrayAdapter<Chat>
{
    private List<Chat> chats;
    private Context context;


    public ChatsAdapter(Context context, List<Chat> chats) {
        super(context, 0, chats);

        this.context = context;
        this.chats = chats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat chat = getItem(position);

        View v = convertView;
        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.chat_item, null);
        }

        if (chat != null) {
            String name = "Geen medewerker toegewezen";
            String formattedDate = "";

            if (chat.getEmployees().size() > 0) {
                name = chat.getEmployees().get(chat.getEmployees().size() - 1).getName();
            }

            TextView chatMessage = (TextView) v.findViewById(R.id.message);
            TextView employeeName = (TextView) v.findViewById(R.id.employee);
            TextView timeStamp = (TextView) v.findViewById(R.id.message_timestamp);

            if (chat.getMessages().size() > 0) {
                formattedDate = Format.fancyDate(chat.getMessages().get(chat.getMessages().size() - 1).getTimeStamp());
                chatMessage.setText(chat.getMessages().get(1).getText());
            }

            employeeName.setText(name);
            timeStamp.setText(formattedDate);
        }

        return v;
    }

    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    public List<Chat> getChats(){
        return this.chats;
    }

    public void setChats(List<Chat> chats){
        this.chats = chats;
    }
}
