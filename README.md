# DiHola Shaking API for Android

DiHola Shaking API makes it easy to build fast and reliable ways to communicate between devices, just by shaking them.
We provide such a secure and flexible protocol that this technology can be applied in any form of data exchange: Payment processing, file sharing, social networking, verification processes, etc.

Installation
-------

Add the following dependency in your app's build.gradle:

```gradle
implementation 'com.github.diholapp:shaking-api-android:0.1.0'
```

Note: We recommend that you don't use ```implementation 'com.github.diholapp:shaking-api-android:+'```, as future versions may not maintain full backwards compatibility.

In your project’s build.gradle:
```gradle
repositories {

    maven {
        url 'https://jitpack.io'
        credentials { username authToken }
    }
}
```


Import
-------

```java
import com.diholapp.android.shaking.ShakingAPI;
import com.diholapp.android.shaking.ShakingIntents;
```

Usage
-------

```java
ShakingAPI api = new ShakingAPI(USER_ID, API_KEY, this).start();
```

**Parameters:**

| Name       | Type       | Description                  |
| -----------| -----------| -----------------------------|
| USER_ID    | String     | User identifier              |
| API_KEY    | String     | Get one at www.diholapp.com  |
| context    | Context    | Activity context             |

Set up your BroadcastReceiver
------------

```java
private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            switch (action){
                case ShakingIntents.SHAKING:
                    break;
                case ShakingIntents.MATCHED:
                    ArrayList<String> result = intent.getStringArrayListExtra("result");
                    break;
                case ShakingIntents.NOT_MATCHED:
                    break;
                case ShakingIntents.LOCATION_PERMISSION_ERROR:
                    // Ask ACCESS_FINE_LOCATION permission
                    break;
                case ShakingIntents.AUTHENTICATION_ERROR:
                    break;
                case ShakingIntents.API_KEY_EXPIRED:
                    break;
                case ShakingIntents.TIMEOUT:
                    break;
                case ShakingIntents.SERVER_ERROR:
                    break;
                case ShakingIntents.SENSOR_ERROR:
                    break;
            }
        }
};

IntentFilter filter = new IntentFilter();
filter.addAction(ShakingIntents.SHAKING);
filter.addAction(ShakingIntents.MATCHED);
filter.addAction(ShakingIntents.NOT_MATCHED);
filter.addAction(ShakingIntents.LOCATION_PERMISSION_ERROR);
filter.addAction(ShakingIntents.AUTHENTICATION_ERROR);
filter.addAction(ShakingIntents.API_KEY_EXPIRED);
filter.addAction(ShakingIntents.TIMEOUT);
filter.addAction(ShakingIntents.SERVER_ERROR);
filter.addAction(ShakingIntents.SENSOR_ERROR);
registerReceiver(receiver, filter);
```



Methods
-------

### Summary

* [`start`](README.md#start)
* [`stop`](README.md#stop)
* [`setSensibility`](README.md#setSensibility)
* [`setDistanceFilter`](README.md#setDistanceFilter)
* [`setTimingFilter`](README.md#setTimingFilter)
* [`setKeepSearching`](README.md#setKeepSearching)
* [`setMaxTimeSearch`](README.md#setMaxTimeSearch)
* [`setRefreshInterval`](README.md#setRefreshInterval)
* [`setLocation`](README.md#setLocation)



### Details

#### `start()`

```java
api.start();
```

Starts listening to shaking events.


---

#### `stop()`

```java
api.stop();
```

Stops listening to shaking events.


---

#### `setSensibility()`

```java
api.setSensibility(sensibility);
```

Sets the sensibility for the shaking event to be triggered.

**Parameters:**

| Name        | Type     | Default|
| ----------- | -------- | -------- |
| sensibility| double     | 25      |

---


#### `setDistanceFilter()`

```java
api.setDistanceFilter(distanceFilter);
```

Sets the maximum distance (in meters) between two devices to be eligible for pairing.

**Parameters:**

| Name        | Type     | Default| Note|
| ----------- | -------- | -------- | ----------------------------------------- |
| distanceFilter| int     | 100  | GPS margin error must be taken into account        |

---


#### `setTimingFilter()`

```java
api.setTimingFilter(timingFilter);
```

Sets the maximum time difference (in milliseconds) between two shaking events to be eligible for pairing.

**Parameters:**

| Name        | Type     | Default|
| ----------- | -------- | -------- |
| timingFilter| int   | 2000 |

---

#### `setKeepSearching()`

```java
api.setKeepSearching(keepSearching);
```

A positive value would allow to keep searching even though if a user has been found. This could allow to pair with multiple devices. The response time will be affected by the maxTimeSearch value.

**Parameters:**

| Name        | Type     | Default|
| ----------- | -------- | -------- |
| keepSearching| boolean| false|

---


#### `setMaxTimeSearch()`

```java
api.setMaxTimeSearch(maxTimeSearch);
```

Sets the maximum time the server will be searching (in milliseconds). This value can affect the response time.

**Parameters:**

| Name        | Type     | Default| Description                               |
| ----------- | -------- | -------- | ----------------------------------------- |
| maxTimeSearch| int     | 2000      | Value between 100 and 10000        |

---

#### `setRefreshInterval()`

```java
api.setRefreshInterval(refreshInterval);
```

Sets the refresh interval for the pairing algorithm (in milliseconds). Greater values would allow to pair with multiple devices when keepSearching is disabled.

**Parameters:**

| Name        | Type     | Default| Description                               |
| ----------- | -------- | -------- | ----------------------------------------- |
| refreshInterval| int     | 250      | Value between 100 and maxTimeSearch        |

---



#### `setLocation()`

```java
api.setLocation(location);
```
or
```java
api.setLocation(latitude, longitude);
```

Setting the location manually will disable using the device location.

**Parameters:**

| Name        | Type     | Default  |
| ----------- | -------- | -------- |
| location    | Location | Device current value |
| latitude    | double   | Device current value|
| longitude   | double   | Device current value|

---




Intents
------------

| Action                   | Extras                         | Description|
| ---------------------    | ------------------------------ | -------- |
| SHAKING                  | -                              | Shaking event triggered |
| MATCHED                  | ```ArrayList<String> result``` | Match with 1 or more users|
| NOT_MATCHED              | -                              | Not matched|
| LOCATION_PERMISSION_ERROR| -                              | Location permission has not been accepted|
| SENSOR_ERROR             | -                              | The sensor devices are not available |
| AUTHENTICATION_ERROR     | -                              | API key invalid|
| API_KEY_EXPIRED          | -                              | API key expired|
| TIMEOUT                  | -                              | The server connection has timed out|
| SERVER_ERROR             | -                              | Server is not available|

The only necessary action in the purpose of this library is MATCHED. The others are optional but we recommend to handle them.



## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
