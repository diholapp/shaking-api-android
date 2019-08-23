# DiHola Shaking API for Android

DiHola Shaking API makes it easy to build fast and reliable ways to communicate between devices, just by shaking them.
We provide such a secure and flexible protocol that this technology can be applied in any form of data exchange: Payment processing, file sharing, social networking, verification processes, etc.

There is available a version of this library which allows to connect devices without internet connection, please [contact us](https://www.diholapp.com).


## Index
1. [Installation](#installation)
2. [Usage](#usage)
3. [Methods](#methods)
4. [Error codes](#error-codes)


# Installation

Installation
-------

Add the following dependency in your app's ```build.gradle```:

```gradle
implementation 'com.diholapp.shaking:shaking-api-android:0.5.2'
```

***Note:*** We recommend that you don't use ```implementation 'com.diholapp.shaking:shaking-api-android:+'```, as future versions may not maintain full backwards compatibility.

***Note:*** This library requires Java 1.8.

```gradle
android {
   compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

Permissions
-------

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

Usage
-------

```java
import com.diholapp.android.shaking.ShakingAPI;
import com.diholapp.android.shaking.ShakingConfig;
import com.diholapp.android.shaking.ShakingCodes;

ShakingConfig shakingConfig = new ShakingConfig(USER_ID, API_KEY);
ShakingAPI shakingAPI = new ShakingAPI(shakingConfig, this){

    @Override
    public void onInit(ArrayList<ShakingCodes> errors) {

    }

    @Override
    public void onShaking(ArrayList<ShakingCodes> errors) {

    }

    @Override
    public void onResult(ArrayList<String> result, ArrayList<ShakingCodes> errors) {

    }
};
```

[Here](app/src/main/java/com/diholapp/shaking/ShakingExample.java) you can find an example.


Methods
-------

##### ShakingAPI

* [`start`](#start)
* [`stop`](#stop)
* [`simulate`](#simulate)
* [`onInit`](#oninit)
* [`onShaking`](#onshaking)
* [`onResult`](#onresult)
* [`isRunning`](#isrunning)
* [`setConfig`](#setconfig)
* [`getConfig`](#getconfig)


##### ShakingConfig

* [`setSensibility`](#setsensibility)
* [`setDistanceFilter`](#setdistancefilter)
* [`setTimingFilter`](#settimingfilter)
* [`setKeepSearching`](#setkeepsearching)
* [`setUserId`](#setuserid)
* [`setConnectOnlyWith`](#setconnectonlywith)


### Details

#### `start()`

```java
shakingAPI.start();
```

Starts listening to shaking events.


---

#### `stop()`

```java
shakingAPI.stop();
```

Stops listening to shaking events.

---

#### `simulate()`

```java
shakingAPI.simulate();
```

Simulates the shaking event.

---

#### `onInit()`

```java
shakingAPI.onInit(errors);
```
This method is executed when the API starts.

**Possible errors:** ```LOCATION_PERMISSION_ERROR```, ```LOCATION_DISABLED``` and ```SENSOR_ERROR```

---

#### `onShaking()`

```java
shakingAPI.onShaking(errors);
```
This method is executed when a shaking event is detected.

**Possible errors:** ```LOCATION_PERMISSION_ERROR``` and ```LOCATION_DISABLED```

---

#### `onResult()`

```java
shakingAPI.onResult(result, errors);
```
This method is executed when the API returns a result.
***Note:*** The result might be an empty ArrayList.

**Possible errors:** ```SERVER_ERROR```, ```AUTHENTICATION_ERROR```, ```API_KEY_EXPIRED```

---


#### `setLocation()`

```java
shakingAPI.setLocation(location);
```
or
```java
shakingAPI.setLocation(latitude, longitude);
```

Setting the location manually will disable using the device location.

**Parameters:**

| Name        | Type     | Default  |
| ----------- | -------- | -------- |
| location    | Location | Device current value |
| latitude    | double   | Device current value|
| longitude   | double   | Device current value|

---

#### `setSensibility()`

```java
shakingConfig.setSensibility(sensibility);
```

Sets the sensibility for the shaking event to be triggered.

**Parameters:**

| Name        | Type     | Default|
| ----------- | -------- | -------- |
| sensibility| double     | 25      |

---


#### `setDistanceFilter()`

```java
shakingConfig.setDistanceFilter(distanceFilter);
```

Sets the maximum distance (in meters) between two devices to be eligible for pairing.
***Note:*** Only used in the online mode.

**Parameters:**

| Name        | Type     | Default| Note|
| ----------- | -------- | -------- | ----------------------------------------- |
| distanceFilter| int     | 100  | GPS margin error must be taken into account        |

---


#### `setTimingFilter()`

```java
shakingConfig.setTimingFilter(timingFilter);
```

Sets the maximum time difference (in milliseconds) between two shaking events to be eligible for pairing.

**Parameters:**

| Name        | Type     | Default| Note|
| ----------- | -------- | -------- | -------- |
| timingFilter| int   | 2000 | Value between 100 and 10000 |

---

#### `setKeepSearching()`

```java
shakingConfig.setKeepSearching(keepSearching);
```

A positive value would allow to keep searching even though if a user has been found. This could allow to pair with multiple devices. The response time will be affected by the timingFilter value.

**Parameters:**

| Name        | Type     | Default|
| ----------- | -------- | -------- |
| keepSearching| boolean| false|

---

#### `setUserId()`

```java
shakingConfig.setUserId(userId);
```

Sets the user identifier.

**Parameters:**

| Name        | Type     | Description|
| ----------- | -------- | -------- |
| userId| String| Unique user identifier in the context of the app (255 characters max)|

---

#### `setOnline()`

```java
shakingConfig.setOnline(onlineEnabled);
```

Enables or disable the online connection.

**Parameters:**

| Name        | Type     | Default|
| ----------- | -------- | -------- |
| onlineEnabled| boolean| true|

#### `setConnectOnlyWith()`

```java
shakingConfig.setConnectOnlyWith(users);
```

Allows to connect only with a set of users. This can be useful in processes verification.

**Parameters:**

| Name        | Type     | Default|
| ----------- | -------- | -------- |
| users| ArrayList<String>| Empty|

---

Error codes
------------

| Name                   | Description|
| ---------------------    | -------- |
| LOCATION_PERMISSION_ERROR| Location permission has not been accepted|
| LOCATION_DISABLED        |  Location is disabled|
| SENSOR_ERROR             |  The sensor devices are not available |
| AUTHENTICATION_ERROR     | API key invalid|
| API_KEY_EXPIRED          | API key expired|
| SERVER_ERROR             | Server is not available|

