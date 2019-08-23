package com.diholapp.android.shaking.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ServerResponse {

    private String jsonString;

    public ServerResponse(String jsonString){
        this.jsonString = jsonString;
    }

    public ArrayList<String> getArrayFromResponse() throws Exception {

        JSONObject jsonObject;

        try {
            Object obj = new JSONParser().parse(jsonString);
            jsonObject = (JSONObject) obj;

        } catch (ParseException e){
            // Server Error. Wrong JSON.
            throw new Exception("500");
        }

        Map status = ((Map)jsonObject.get("status"));

        Iterator<Map.Entry> itr1 = status.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();

            String key = pair.getKey().toString();

            if(key.equals("code")) {
                String value = pair.getValue().toString();
                if (!value.equals("200")){
                    throw new Exception(value);
                }
            }
        }

        JSONArray jArray = (JSONArray) jsonObject.get("response");
        ArrayList<String> result = new ArrayList<>();
        if (jArray != null) {
            for (int i = 0; i < jArray.size(); i++){
                result.add(jArray.get(i).toString());
            }
        }

        return result;
    }

}
