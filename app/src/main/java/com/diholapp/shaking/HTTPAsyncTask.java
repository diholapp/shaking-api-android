package com.diholapp.shaking;

import android.os.AsyncTask;

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

public class HTTPAsyncTask extends AsyncTask<String, Void, String> {

    private Shaking delegate = null;

    public HTTPAsyncTask(Shaking delegate){
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            try {
                return HttpPost("https://diholapplication.com/bump-php/connect");
            } catch (JSONException e) {
                e.printStackTrace();
                return "Error!";
            }
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    private String HttpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        InputStream in = new BufferedInputStream(conn.getInputStream());

        // 5. return response message
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

    private JSONObject buidJsonObject() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.accumulate("id", delegate.getUser());
        jsonObject.accumulate("api_key",  delegate.getApiKey());

        jsonObject.accumulate("lat",  delegate.getLat());
        jsonObject.accumulate("lng",  delegate.getLng());

        jsonObject.accumulate("timingFilter",  delegate.getTimingFilter());
        jsonObject.accumulate("maxTimeSearch",  delegate.getMaxTimeSearch());
        jsonObject.accumulate("refreshInterval",  delegate.getRefreshInterval());
        jsonObject.accumulate("distanceFilter",  delegate.getDistanceFilter());
        jsonObject.accumulate("keepSearching",  delegate.getKeepSearching());

        return jsonObject;
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
