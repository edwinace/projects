package com.avans.ronald.snschatapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.avans.ronald.snschatapp.JsonToClass;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.SnsClient;
import com.avans.ronald.snschatapp.Storage;
import com.avans.ronald.snschatapp.models.Customer;
import com.avans.ronald.snschatapp.models.Employee;
import com.avans.ronald.snschatapp.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendChatMessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SendChatMessageFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button sendTextButton;
    private EditText textField;
    private String chatId;
    private Message message;

    public SendChatMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_send_chat_message, container, false);

        sendTextButton = (Button) v.findViewById(R.id.send_message_button);
        setOnClickListener(sendTextButton);

        textField = (EditText) v.findViewById(R.id.send_message_text_field);

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            chatId = activity.getIntent().getStringExtra("CHAT_ID");
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setOnClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textField.getText().toString().equals("") && textField.getText() != null) {
                    (new SendMessageAsync(getActivity().getApplicationContext())).execute("/api/customers/"+ Storage.getCustomerId(getActivity().getApplicationContext()) +"/chats/" + chatId + "/messages");
                }
            }
        });
    }

    private class SendMessageAsync extends AsyncTask<String, Void, Void> {

        String responseCode = null;
        private Context context;

        public SendMessageAsync(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mListener.onSendMessage(message);
            textField.setText("");
        }

        @Override
        protected Void doInBackground(String... params) {
            SnsClient client = new SnsClient(context);
            try{
                JSONObject response = new JSONObject(client.post(params[0], "message=" + textField.getText().toString()));
                JSONObject data = new JSONObject(response.getString("data"));
                responseCode = response.getString("status");

                message = convertJSONObjectToMessage(data);
            }catch (Throwable t){
                t.printStackTrace();
            }
            return null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onSendMessage(Message message);
    }
}
