package com.avans.ronald.snschatapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.avans.ronald.snschatapp.JsonToClass;
import com.avans.ronald.snschatapp.R;
import com.avans.ronald.snschatapp.SnsClient;
import com.avans.ronald.snschatapp.Storage;
import com.avans.ronald.snschatapp.adapters.ChatAdapter;
import com.avans.ronald.snschatapp.models.Customer;
import com.avans.ronald.snschatapp.models.Employee;
import com.avans.ronald.snschatapp.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatMessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChatMessagesFragment extends Fragment {

    private final static String TAG = ChatMessagesFragment.class.getSimpleName();

    private final AtomicBoolean running = new AtomicBoolean(true);

    private OnFragmentInteractionListener mListener;
    private ChatAdapter chatAdapter;
    private ListView listView;
    private String chatId;

    public ChatMessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_messages, container, false);
        listView = (ListView) v.findViewById(android.R.id.list);
//        if(chatAdapter == null){
//            chatAdapter = new ChatAdapter(getActivity().getApplicationContext(), (ArrayList<Message>) getActivity().getParentActivityIntent().getSerializableExtra("MESSAGES"), new HashMap<String, String>());
//        }
        listView.setAdapter(chatAdapter);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public String getChatId(){
        return this.chatId;
    }
    public ArrayAdapter<Message> getAdapter(){
        return this.chatAdapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            chatId = activity.getIntent().getStringExtra("CHAT_ID");
            chatAdapter = new ChatAdapter(getActivity().getApplicationContext(), (ArrayList<Message>) activity.getIntent().getSerializableExtra("MESSAGES"), new HashMap<String, String>());
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        stopExecuteAsyncLoop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopExecuteAsyncLoop();
    }

    @Override
    public void onResume(){
        super.onResume();
        running.set(true);
    }

    public void addMessageToList(final Message sentMessage){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatAdapter.getMessages().add(sentMessage);
                chatAdapter.notifyDataSetChanged();
            }
        });
    }

    private void stopExecuteAsyncLoop(){
        running.set(false);
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
        public void onFragmentInteraction(Uri uri);
    }

}
