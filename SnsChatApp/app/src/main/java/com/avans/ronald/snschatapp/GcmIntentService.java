package com.avans.ronald.snschatapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.avans.ronald.snschatapp.activities.FaqActivity;
import com.avans.ronald.snschatapp.activities.ViewAllChatsActivity;
import com.avans.ronald.snschatapp.models.Chat;
import com.avans.ronald.snschatapp.models.ChatsContainer;
import com.avans.ronald.snschatapp.models.Customer;
import com.avans.ronald.snschatapp.models.Employee;
import com.avans.ronald.snschatapp.models.FAQ;
import com.avans.ronald.snschatapp.models.FaqListContainer;
import com.avans.ronald.snschatapp.models.Message;
import com.avans.ronald.snschatapp.receivers.GcmBroadcastReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by Ronald on 16-3-2015.
 */
public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName();

    //Message types
    private static final String REGULAR_MESSAGE = "message";
    private static final String SYNC_FAQ_MESSAGE = "sync_faq";

    public static boolean checkRunningMode = false;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private Vibrator mVibrator;

    public GcmIntentService(){
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.

                System.out.println(extras.toString());
                //Regular messages
                if (extras.get(REGULAR_MESSAGE) != null) {
                    JSONObject data = (getDataFromJSONObject(extras.getString(REGULAR_MESSAGE)));

                    if (checkRunningMode) {

                        Intent addMessageToViewAllChats = new Intent("android.intent.action.MESSAGE_ALL_CHATS");
                        Intent addMessage = new Intent("android.intent.action.MESSAGE");

                        try {
                            addMessageToViewAllChats.putExtra("MESSAGE", data.getString("completeMessage"));
                            addMessageToViewAllChats.putExtra("CHAT_ID", data.getString("chatId"));

                            if (!data.isNull("employee")) {
                                addMessage.putExtra("EMPLOYEE", data.getString("employee"));
                                addMessageToViewAllChats.putExtra("EMPLOYEE", data.getString("employee"));
                            }

                            addMessage.putExtra("MESSAGE", data.getString("completeMessage"));
                            addMessage.putExtra("CHAT_ID", data.getString("chatId"));
                        }catch(JSONException e){
                            Log.e(TAG, e.getMessage());
                        }

                        this.sendBroadcast(addMessageToViewAllChats);
                        this.sendBroadcast(addMessage);
                    } else {
                        try{
                            sendNotification("Nieuw bericht: " + data.getString("notificationMessage"));
                            ChatsContainer cc = ChatsContainer.deSerializeSelf(getApplication().openFileInput(ChatsContainer.CHATS_FILE_NAME));
                            for(Chat chat : cc.getChats()){
                                if(chat.get_id().equals(data.getString("chatId"))){
                                    chat.getMessages().add(convertJSONObjectToMessage(new JSONObject(data.getString("completeMessage"))));
                                    break;
                                }
                            }
                            ChatsContainer.serializeSelf(cc, getApplication().openFileOutput(ChatsContainer.CHATS_FILE_NAME, Context.MODE_PRIVATE));
                        }catch (FileNotFoundException e){
                            Log.e(TAG, e.getMessage());
                        }catch (JSONException e){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                //Messages to sync the FAQ
                }else if(extras.get(SYNC_FAQ_MESSAGE) != null){
                    JSONObject data = getDataFromJSONObject(extras.getString(SYNC_FAQ_MESSAGE));

                    FaqListContainer container = null;

                    try{
                        container = FaqListContainer.deSerializeSelf(openFileInput(FaqActivity.FAQ_FILE_NAME));

                        if(container != null) {
                            if (data.getString("type").equals("post")) {
                                FAQ faq = JsonToClass.toFaq(data);
                                container.addFaqToList(faq);
                                container.serializeSelf(container, openFileOutput(FaqActivity.FAQ_FILE_NAME, Context.MODE_PRIVATE));
                            } else if (data.getString("type").equals("put")) {
                                FAQ faq = JsonToClass.toFaq(data);
                                container.updateFaqById(faq.getCategory(), faq);
                                container.serializeSelf(container, openFileOutput(FaqActivity.FAQ_FILE_NAME, Context.MODE_PRIVATE));
                            } else if (data.getString("type").equals("delete")) {
                                container.deleteFaqById(data.getString("id"));
                                container.serializeSelf(container, openFileOutput(FaqActivity.FAQ_FILE_NAME, Context.MODE_PRIVATE));
                            }
                        }
                    }catch (JSONException e){
                        Log.e(TAG, e.getMessage());
                    } catch (FileNotFoundException e2) {
                        Log.e(TAG, e2.getMessage());
                    }
                    this.sendBroadcast(new Intent("android.intent.action.SYNC_FAQ").putExtra("faq", extras.getString("sync_faq")));
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private JSONObject getDataFromJSONObject(String jsonObject){
        try{
            JSONObject temp = new JSONObject(jsonObject);
            return new JSONObject(temp.getString("data"));
        }catch(JSONException e){
            Log.e(TAG, e.getMessage());
        }
        return null;
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

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String notificationMessage) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ViewAllChatsActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("New SNS message")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(notificationMessage))
                        .setContentText(notificationMessage)
                        .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}

