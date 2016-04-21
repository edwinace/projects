package com.avans.ronald.snschatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.avans.ronald.snschatapp.activities.MainActivity;

/**
 * Created by Ronald on 2-4-2015.
 */
public class Storage {

    private static final String TAG = Storage.class.getSimpleName();

    private static final String PROPERTY_CUSTOMER_ID = "customer_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_CODE = "pincode";
    private static final String PROPERTY_AUTHORIZATION = "authorization";

    public static void storeRegistrationId(Context context, String regId, int appVersion) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static void storePincode(Context context, String pincode, int appVersion){
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_CODE, pincode);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static void storeAuthorization(Context context, boolean authorizationOn, int appVersion){
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PROPERTY_AUTHORIZATION, authorizationOn);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static void storeCustomerId(Context context, String customerId, int appVersion){
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_CUSTOMER_ID, customerId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public static String getCustomerId(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String customerId = prefs.getString(PROPERTY_CUSTOMER_ID, "");
        if (customerId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }

//        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion) {
//            Log.i(TAG, "App version changed.");
//            return "";
//        }
        return customerId;
    }

    public static String getPincode(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String pincode = prefs.getString(PROPERTY_CODE, "");
        if (pincode.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return pincode;
    }

    public static boolean getAuthorization(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        boolean pincode = prefs.getBoolean(PROPERTY_AUTHORIZATION, false);
        if (!pincode) {
            Log.i(TAG, "Authorization not set.");
            return false;
        }
        return pincode;
    }

    public static String getRegistrationId(Context context){
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
//        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion) {
//            Log.i(TAG, "App version changed.");
//            return "";
//        }
        return registrationId;
    }

    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

}
