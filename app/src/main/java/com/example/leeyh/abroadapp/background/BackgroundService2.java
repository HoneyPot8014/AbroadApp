//package com.example.leeyh.abroadapp.background;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//
//import com.example.leeyh.abroadapp.constants.SocketEvent;
//import com.example.leeyh.abroadapp.helper.ApplicationManagement;
//import com.example.leeyh.abroadapp.dataconverter.DataConverter;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.net.URISyntaxException;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//
//import static com.example.leeyh.abroadapp.constants.SocketEvent.CHECK_SIGNED;
//import static com.example.leeyh.abroadapp.constants.SocketEvent.DISCONNECT;
//import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTE_CHAT;
//import static com.example.leeyh.abroadapp.constants.SocketEvent.ROUTING;
//import static com.example.leeyh.abroadapp.constants.SocketEvent.SAVE_LOCATION_SUCCESS;
//import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGNED_USER;
//import static com.example.leeyh.abroadapp.constants.SocketEvent.SIGN_UP;
//import static com.example.leeyh.abroadapp.constants.StaticString.BROADCAST;
//import static com.example.leeyh.abroadapp.constants.StaticString.EMIT_EVENT;
//import static com.example.leeyh.abroadapp.constants.StaticString.JSON_DATA;
//import static com.example.leeyh.abroadapp.constants.StaticString.ON_EVENT;
//import static com.example.leeyh.abroadapp.constants.StaticString.PASSWORD;
//import static com.example.leeyh.abroadapp.constants.StaticString.PROFILE;
//import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;
//import static com.example.leeyh.abroadapp.constants.StaticString.USER_NAME;
//import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;
//import static com.example.leeyh.abroadapp.constants.StaticString.USER_UUID;
//
//public class BackgroundService extends Service {
//
//    private Socket mSocket;
//    private ApplicationManagement mAppStatic;
//    private IO.Options socketOptions;
//    private NotificationManager mNotificationManager;
//    private NotificationCompat.Builder mNotificationBuilder;
//    private PendingIntent mPedningIntent;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//
//        socketOptions = new IO.Options();
//        socketOptions.forceNew = false;
//        try {
//            mSocket = IO.socket(SocketEvent.DEFAULT_HOST, socketOptions);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            //not connect handling
//        }
//        mSocket.connect();
////        onSocketConnected(mSocket);
//        mAppStatic = (ApplicationManagement) getApplicationContext();
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("서비스 몇개인지", "onStartCommand: " + intent.getStringExtra(ROUTING) +intent.getStringExtra(EMIT_EVENT) +intent.getStringExtra(ON_EVENT));
//        if (intent != null) {
//            if (intent.getStringExtra(ROUTING) != null) {
//                String routeString = intent.getStringExtra(ROUTING);
//                mSocket.disconnect();
//                mSocket = mSocket.io().socket(routeString);
//                mSocket.connect();
//                mSocket.on(DISCONNECT, new Emitter.Listener() {
//                    @Override
//                    public void call(Object... args) {
//                        Log.d("연결끊김", "call: ");
//                    }
//                });
//            } else if (intent.getStringExtra(EMIT_EVENT) != null) {
//                if (intent.getStringExtra(EMIT_EVENT).equals(SIGN_UP)) {
//                    String event = intent.getStringExtra(EMIT_EVENT);
//                    String data = intent.getStringExtra(JSON_DATA);
//                    try {
//                        JSONObject jsonData = new JSONObject(data);
//                        String id = jsonData.getString(USER_NAME);
//                        jsonData.put(PROFILE, DataConverter.getByteArrayToStringFromBitmap(mAppStatic.getBitmapFromMemoryCache(id)));
//                        mSocket.emit(event, jsonData);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    String event = intent.getStringExtra(EMIT_EVENT);
//                    String StringData = intent.getStringExtra(JSON_DATA);
//                    try {
//                        JSONObject jsonData = new JSONObject(StringData);
//                        mSocket.emit(event, jsonData);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } else if (intent.getStringExtra(ON_EVENT) != null) {
//                String event = intent.getStringExtra(ON_EVENT);
//                setOnEvent(mSocket, event);
//            } else if (intent.getStringExtra(DISCONNECT) != null) {
//                Log.d("몇번 서비스 명령 받음", intent.getStringExtra(DISCONNECT));
//                mSocket.close();
//                mSocket.disconnect();
//            }
//        }
//        return START_STICKY;
//    }
//
//    public void setOnEvent(Socket socket, final String event) {
//        socket.once(event, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Intent broadCast = new Intent(BROADCAST);
//                broadCast.putExtra(ON_EVENT, event);
//                JSONArray receivedArray = (JSONArray) args[0];
//                if (event.equals(SAVE_LOCATION_SUCCESS)) {
//                    for (int i = 0; i < receivedArray.length(); i++) {
//                        try {
//                            JSONObject receivedObject = (JSONObject) receivedArray.get(0);
//                            if (receivedObject.get(PROFILE) != null || !receivedObject.get(PROFILE).equals("")) {
//                                byte[] userProfileByteArray = (byte[]) receivedObject.get(PROFILE);
//                                Bitmap userProfile = BitmapFactory.decodeByteArray(userProfileByteArray, 0, userProfileByteArray.length);
//                                mAppStatic.addBitmapToMemoryCache(receivedObject.getString(USER_NAME), userProfile);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                broadCast.putExtra(RECEIVED_DATA, receivedArray.toString());
//                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadCast);
//            }
//        });
//    }
//
//    public void onSocketConnected(Socket socket) {
//        SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
//        if (sharedPreferences.getString(USER_NAME, null) != null && sharedPreferences.getString(PASSWORD, null) != null
//                && sharedPreferences.getString(USER_UUID, null) != null) {
//            JSONObject checkSignData = new JSONObject();
//            try {
//                checkSignData.put(USER_NAME, sharedPreferences.getString(USER_NAME, null));
//                checkSignData.put(PASSWORD, sharedPreferences.getString(PASSWORD, null));
//                checkSignData.put(USER_UUID, sharedPreferences.getString(USER_UUID, null));
//                socket.emit(CHECK_SIGNED, checkSignData);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        socket.once(SIGNED_USER, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d("여기서부터 보자", "call: ");
//                mSocket = mSocket.io().socket(ROUTE_CHAT);
//                mSocket.connect();
//            }
//        });
//    }
//}
