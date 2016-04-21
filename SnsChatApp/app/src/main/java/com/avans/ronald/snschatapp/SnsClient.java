package com.avans.ronald.snschatapp;

import android.content.Context;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Luuk on 16-5-2015.
 */
public class SnsClient
{
    private String username, password, url;

    public SnsClient(Context context)
    {
        this.username = context.getString(R.string.auth_username);
        this.password = context.getString(R.string.auth_password);
        this.url = context.getString(R.string.external_api_url);
    }

    public String get(String path)
    {
        String response = null;
        try {
            URL url = new URL(this.url + path);
            String authString = this.username + ":" + this.password;
            String encString = Base64.encodeToString(authString.getBytes(), 0);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + encString);
            conn.connect();

            InputStream in = conn.getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            response = responseStrBuilder.toString();
        }catch (Throwable t){
            t.printStackTrace();
        }

        return response;
    }

    public String post(String path, String postFields)
    {
        String response = null;
        try {
            URL url = new URL(this.url + path);
            String authString = this.username + ":" + this.password;
            String encString = Base64.encodeToString(authString.getBytes(), 0);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.addRequestProperty("Authorization", "Basic " + encString);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(postFields);

            InputStream in = conn.getInputStream();

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            response = responseStrBuilder.toString();
        }catch (Throwable t){
            t.printStackTrace();
        }

        return response;
    }
}
