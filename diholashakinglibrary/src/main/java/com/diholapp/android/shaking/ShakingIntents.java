package com.diholapp.android.shaking;

public class ShakingIntents {

    /* Broadcast when a shaking event is triggered.
     */
    public static final String SHAKING = "com.diholapp.shaking.shaking";

    /* Broadcast when the API fails to match someone.
     */
    public static final String NOT_MATCHED = "com.diholapp.shaking.notMatched";

    /* Broadcast when the API has matched someone.
     */
    public static final String MATCHED = "com.diholapp.shaking.matched";

    /* Broadcast when the location permission has not been accepted.
     */
    public static final String LOCATION_PERMISSION_ERROR = "com.diholapp.shaking.locationPermissionError";

    /* Broadcast when the server fails to validate the API key.
     */
    public static final String AUTHENTICATION_ERROR = "com.diholapp.shaking.authenticationError";

    /* Broadcast when the API key has expired.
     */
    public static final String API_KEY_EXPIRED = "com.diholapp.shaking.apiKeyExpired";

    /* Broadcast when the server connection has timed out.
     */
    public static final String TIMEOUT = "com.diholapp.shaking.timeout";

    /* Broadcast when the server is not available.
     */
    public static final String SERVER_ERROR = "com.diholapp.shaking.serverError";

    /* Broadcast when the device sensors are not available.
     */
    public static final String SENSOR_ERROR = "com.diholapp.shaking.sensorError";
}
