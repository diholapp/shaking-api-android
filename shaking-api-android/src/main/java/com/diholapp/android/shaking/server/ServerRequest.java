package com.diholapp.android.shaking.server;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ServerRequest extends AsyncTask<String, Void, String> {

    protected int TIMEOUT;
    protected String URL_REQUEST;

    protected abstract JSONObject buildBodyRequest() throws JSONException;

    @Override
    protected String doInBackground(String... urls) {

        try {
            return HttpPost();
        }
        catch (Exception e) {
            return "";
        }

    }

    private String HttpPost() throws IOException, JSONException {

        URL url = new URL(URL_REQUEST);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setConnectTimeout(TIMEOUT);

        // 2. build JSON object
        JSONObject jsonObject = buildBodyRequest();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        InputStream in = new BufferedInputStream(conn.getInputStream());

        return readStream(in);
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        writer.write(jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }
}
