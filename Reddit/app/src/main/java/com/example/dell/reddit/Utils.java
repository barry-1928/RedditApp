package com.example.dell.reddit;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import Databases.MyHelper;

/**
 * Created by dell on 15-07-2017.
 */

public class Utils {

    private static String api = "https://api.reddit.com/top/.json";

    public static ArrayList<PostDetails> postDetailsArrayList;
    public static int n;
    public static String after;

    private Utils() {
    }

    public static void parseJsonCode(String JsonCode) {
        Log.d("xyz",JsonCode);
        String kind;
        String title;
        Double created_utc_timestamp;
        int number_of_comments;
        String thumbnail;
        postDetailsArrayList = new ArrayList<>();
        try {

            JSONObject root = new JSONObject(JsonCode);
            JSONObject child_data;
            JSONObject root_data = root.getJSONObject("data");
            JSONObject child_object;
            JSONArray children = root_data.getJSONArray("children");
            int length = children.length();
            Log.d("xyz",""+length);
            for(int i=0;i<length;i++) {
                child_object = children.getJSONObject(i);
                kind = child_object.getString("kind");
                child_data = child_object.getJSONObject("data");
                title = child_data.getString("title");
                created_utc_timestamp = child_data.getDouble("created_utc");
                number_of_comments = child_data.getInt("num_comments");
                thumbnail = child_data.getString("thumbnail");
                postDetailsArrayList.add(new PostDetails(kind,title,created_utc_timestamp,number_of_comments,thumbnail));
                Log.d("xyz",kind+" "+title+" "+ created_utc_timestamp + " " + number_of_comments);
            }

            MainActivity.postDetailsArrayList.addAll(postDetailsArrayList);

            after = root_data.getString("after");
            Log.d("xyz",after);

        } catch (JSONException e) {
            Log.e("xyz", e.getMessage());
        }

    }

    public static void EstablishUrlConnection(int n) {
            String jsonCode = "";
            String url_name;
            if(n == 0) {
                url_name = api;
                Log.d("xyz", url_name);
            }
            else {
                url_name = api + "?" + after;
                Log.d("xyz",url_name);
            }

            try {

            URL url = new URL(url_name);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            InputStream inputStream = null;

            if(urlConnection.getResponseCode() == 200) {
                Log.d("xyz",""+urlConnection.getResponseCode());
                inputStream = urlConnection.getInputStream();
                if(inputStream != null){
                    jsonCode = getJsonFromInputStream(inputStream);
                    parseJsonCode(jsonCode);
                }
            }
        } catch (MalformedURLException e) {
            Log.e("xyz","Error forming URL");
        } catch (IOException e) {
            Log.e("xyz","Error opening connection");
        }

    }

    public static String getJsonFromInputStream(InputStream inputStream) {
        String jsonCode = "";
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            Log.e("xyz","Error reading data");
        }

        while(line != null) {
            stringBuilder.append(line);
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                Log.e("xyz","Error reading data");
            }
        }

        jsonCode = stringBuilder.toString();

        return jsonCode;

    }
}