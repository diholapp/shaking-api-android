package com.diholapp.android.shaking.server;

import com.diholapp.android.shaking.ShakingAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServerRequestConnect extends ServerRequest {

    private ShakingAPI delegate;

    public ServerRequestConnect(ShakingAPI delegate){

        this.delegate = delegate;
        TIMEOUT = delegate.getConfig().getTimingFilter() + 5000;
        URL_REQUEST = "https://api.diholapplication.com/shaking/connect";
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processServerResponse(new ServerResponse(result));
    }

    protected JSONObject buildBodyRequest() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.accumulate("id", delegate.getUserId());
        jsonObject.accumulate("api_key",  delegate.getConfig().getApiKey());
        jsonObject.accumulate("id_request",  delegate.getIdRequest());

        jsonObject.accumulate("lat",  delegate.getLocation().getLatitude());
        jsonObject.accumulate("lng",  delegate.getLocation().getLongitude());

        jsonObject.accumulate("sensibility",  delegate.getConfig().getSensibility());
        jsonObject.accumulate("timingFilter",  delegate.getConfig().getTimingFilter());
        jsonObject.accumulate("distanceFilter",  delegate.getConfig().getDistanceFilter());
        jsonObject.accumulate("keepSearching",  delegate.getConfig().getKeepSearching());

        jsonObject.accumulate("connectOnlyWith",  new JSONArray(delegate.getConfig().getConnectOnlyWith()));

        return jsonObject;
    }

}
