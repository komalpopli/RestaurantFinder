package com.example.komal.restaurantfinder;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;

class JSONParser {
     HttpURLConnection httpURLConnection;
    URL url1;
    StringBuilder sbParams;
    StringBuilder stringBuilder;
    JSONObject jsonObject = null;


    public JSONObject makeHttpRequest(String url, String method, HashMap<String,String> params) throws IOException, JSONException {
         sbParams=new StringBuilder();
        int i=0;
        for(String key: params.keySet()) {
            try {
                if (i != 0) {
                    sbParams.append("&");
                }
                sbParams.append("key").append("=").append(URLEncoder.encode(params.get(key), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        if(method.equals("POST")) {
            try {
                URL url1 = new URL(url);
                httpURLConnection = ( HttpURLConnection ) url1.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();
                String paramString = sbParams.toString();
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(paramString);
                dataOutputStream.flush();
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(method.equals("GET")){
            if(sbParams.length()!=0){
                url += "?" + sbParams.toString();
            }

            try{
                url1 = new URL(url);
                httpURLConnection = (HttpURLConnection) url1.openConnection();
                httpURLConnection.setDoOutput(false);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try{
            InputStream in=new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
            stringBuilder=new StringBuilder();
            String line;
            while ((line =bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        httpURLConnection.disconnect();
        try{
           jsonObject=new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }
}
