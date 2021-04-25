package com.example.foodcalculator.httpHandler;


import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpHandler extends AsyncTask<String, Void, String> {

    //Callback interface
    public AsyncResponse delegate;
    public HttpHandler(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }

    String result;

    @Override
    protected String doInBackground(String... params) {

//        String query = "3lb carrots and a chicken sandwich";

        String query = params[0];
        URL url = null;

        try {
            url = new URL("https://api.calorieninjas.com/v1/nutrition?query="+query);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {

            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("X-Api-Key", "uUc45GZTz4+oXqqtfVyyTw==ODGjuTjXcqJ9ddaQ");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            String stringBuffer = null;
            String string = "";
            while (true) {
                if ((stringBuffer = bufferedReader.readLine()) == null) break;
                string = String.format("%s%s", string, stringBuffer);
            }
            bufferedReader.close();
            result = string;
        } catch (IOException e) {
            e.printStackTrace();
            result = e.toString();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String response) {
//        System.out.println(result);
        delegate.processFinish(result);
    }
}
