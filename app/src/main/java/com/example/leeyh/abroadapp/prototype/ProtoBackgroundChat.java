//package com.example.leeyh.abroadapp.prototype;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.media.RingtoneManager;
//import android.os.IBinder;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//
//import com.example.leeyh.abroadapp.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.net.URISyntaxException;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
//
//import static com.example.leeyh.abroadapp.prototype.Constants.CHAT_ROUTE;
//import static com.example.leeyh.abroadapp.prototype.Constants.CHECK_USER;
//import static com.example.leeyh.abroadapp.prototype.Constants.GET_CHAT_LIST;
//import static com.example.leeyh.abroadapp.prototype.Constants.GET_STORED_CHAT;
//import static com.example.leeyh.abroadapp.prototype.Constants.INVITED_JOIN;
//import static com.example.leeyh.abroadapp.prototype.Constants.JOIN_SUCCESS;
//import static com.example.leeyh.abroadapp.prototype.Constants.LOGIN_ROUTE;
//import static com.example.leeyh.abroadapp.prototype.Constants.RECEIVE_CHAT;
//import static com.example.leeyh.abroadapp.prototype.Constants.REQUEST_JOIN;
//import static com.example.leeyh.abroadapp.prototype.Constants.REQUEST_STORED_CHAT;
//import static com.example.leeyh.abroadapp.prototype.Constants.REQUEST_URL;
//import static com.example.leeyh.abroadapp.prototype.Constants.RESPONSE_CHECK_USER;
//import static com.example.leeyh.abroadapp.prototype.Constants.SEND_CHAT;
//import static com.example.leeyh.abroadapp.prototype.Constants.SET_CHAT_LIST;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_IN_FAILED;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_IN_SUCCESS;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_UP;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_UP_FAILED;
//import static com.example.leeyh.abroadapp.prototype.Constants.SIGN_UP_SUCCESS;
//import static com.example.leeyh.abroadapp.prototype.ProtoChatListActivity.CHATS;
//import static com.example.leeyh.abroadapp.prototype.ProtoChatListActivity.JOIN_MEMBERS;
//import static com.example.leeyh.abroadapp.prototype.ProtoChatListActivity.ROOM_NAME;
//import static com.example.leeyh.abroadapp.prototype.ProtoRoomChatActivity.MESSAGE;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.EVENT;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.USER_EMAIL;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.PASSWORD;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.PROFILE;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.ROUTING;
//import static com.example.leeyh.abroadapp.prototype.ProtoSignUpActivity.USER_ID;
//
//
//public class ProtoBackgroundChat extends Service {
//
//    public Socket mSocket;
//    public String TAG = "서비스";
//    private NotificationManager notificationManager;
//    private NotificationCompat.Builder builder;
//    private PendingIntent pendingIntent;
//
//    public ProtoBackgroundChat() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        throw null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d(TAG, "onCreate: 시작됨");
//
//        try {
//            IO.Options options = new IO.Options();
//            options.reconnection = true;
//            options.forceNew = true;
//            mSocket = IO.socket(REQUEST_URL, options);
//            mSocket.io().reconnection(true);
//            mSocket.connect();
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//
//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel("channel", "name", importance);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//        builder =
//                new NotificationCompat.Builder(getApplicationContext(), "channel");
//
//        Intent notificationIntent = new Intent(getApplicationContext()
//                , ProtoSignUpActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        int requestID = (int) System.currentTimeMillis();
//        pendingIntent = PendingIntent.getActivity(getApplicationContext()
//                , requestID
//                , notificationIntent
//                , PendingIntent.FLAG_UPDATE_CURRENT);
//
//        defaultNameSpaceSocketOnEvent();
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null) {
//                if (intent.getStringExtra(EVENT) != null) {
//                    if (intent.getStringExtra(EVENT).equals(REQUEST_JOIN)) {
//                        String event = intent.getStringExtra(EVENT);
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put(USER_NAME, intent.getStringExtra(USER_NAME));
//                            data.put("invite", intent.getStringExtra("invite"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        mSocket.emit(event, data);
//
//                    } else if (intent.getStringExtra(EVENT).equals(GET_CHAT_LIST)) {
//                        mSocket.emit(GET_CHAT_LIST, intent.getStringExtra(USER_ID));
//                    } else if (intent.getStringExtra(EVENT).equals(SEND_CHAT)) {
//                        String event = intent.getStringExtra(EVENT);
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put(USER_NAME, intent.getStringExtra(USER_NAME));
//                            data.put(ROOM_NAME, intent.getStringExtra(ROOM_NAME));
//                            data.put(MESSAGE, intent.getStringExtra(MESSAGE));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        mSocket.emit(event, data);
//
//                    } else if (intent.getStringExtra(EVENT).equals(REQUEST_STORED_CHAT)) {
//                        String event = intent.getStringExtra(EVENT);
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put(USER_NAME, intent.getStringExtra(USER_NAME));
//                            data.put(ROOM_NAME, intent.getStringExtra(ROOM_NAME));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        mSocket.emit(event, data);
//                    } else if(intent.getStringExtra(EVENT).equals(SIGN_UP)){
//                        Drawable drawable = getResources().getDrawable(R.drawable.test);
//                        Bitmap profile = ((BitmapDrawable)drawable).getBitmap();
//                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                        profile.compress(Bitmap.CompressFormat.JPEG, 5, outputStream);
//                        byte[] byteArray = outputStream.toByteArray();
//                        String event = intent.getStringExtra(EVENT);
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put(USER_NAME, intent.getStringExtra(USER_NAME));
//                            data.put(PASSWORD, intent.getStringExtra(PASSWORD));
//                            data.put(E_MAIL, intent.getStringExtra(E_MAIL));
//                            data.put(PROFILE, byteArray);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        mSocket.emit(event, data);
//                    } else {
//
//                        String event = intent.getStringExtra(EVENT);
//                        JSONObject data = new JSONObject();
//                        try {
//                            data.put(USER_NAME, intent.getStringExtra(USER_NAME));
//                            data.put(PASSWORD, intent.getStringExtra(PASSWORD));
//                            data.put(E_MAIL, intent.getStringExtra(E_MAIL));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        mSocket.emit(event, data);
//                    }
//                } else if (intent.getStringExtra(ROUTING) != null) {
//                    String route = intent.getStringExtra(ROUTING);
//                    mSocket = mSocket.io().socket(route);
//                    mSocket.connect();
//
//                    if (route.equals(LOGIN_ROUTE)) {
//                        singInNameSpaceSocketOnEvent();
//                    } else if (route.equals(CHAT_ROUTE)) {
//                        chatNameSpaceSocketOnEvent();
//                    }
//                }
//            mSocket.on("disconnect", new Emitter.Listener() {
//                @Override
//                public void call(Object... args) {
//                    mSocket.open();
//                }
//            });
//        }
//
//        return START_STICKY;
//    }
//
//    private void checkUserSignIned() {
//        if (getSharedPreferences("userInfo", MODE_PRIVATE) != null) {
//            SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
//            String userId = preferences.getString(USER_ID, null);
//            String password = preferences.getString(PASSWORD, null);
//            String nickName = preferences.getString(USER_EMAIL, null);
//            if (userId != null && password != null && nickName != null) {
//                JSONObject data = new JSONObject();
//                try {
//                    data.put(USER_ID, userId);
//                    data.put(PASSWORD, password);
//                    data.put(USER_EMAIL, nickName);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                mSocket.emit(RESPONSE_CHECK_USER, data);
//            }
//        }
//    }
//
//    private void defaultNameSpaceSocketOnEvent() {
//        mSocket.on(SIGN_UP_SUCCESS, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Intent broadCast = new Intent(SIGN_UP_SUCCESS);
//                sendBroadcast(broadCast);
//            }
//        });
//
//        mSocket.on(SIGN_UP_FAILED, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Intent broadCast = new Intent(SIGN_UP_FAILED);
//                sendBroadcast(broadCast);
//            }
//        });
//
//        mSocket.on(RESPONSE_CHECK_USER, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d(TAG, "call: 유저 확인");
//
//            }
//        });
//
//        mSocket.on(CHECK_USER, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d(TAG, "call: CHECK USER 처리해야함.");
//                checkUserSignIned();
//            }
//        });
//
//        mSocket.on("goToChat", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d(TAG, "call: goToChat 응답 받음.");
//                SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
//                String userId = preferences.getString(USER_ID, null);
//                JSONObject data = new JSONObject();
//                try {
//                    data.put(USER_ID, userId);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Socket newSocket = mSocket.io().socket(CHAT_ROUTE);
//                newSocket.connect();
//                newSocket.emit("storedUser", data);
//                Log.d(TAG, "call: storedUser 요청함");
//                Intent broadCast = new Intent("goToChat");
//                sendBroadcast(broadCast);
//
//                newSocket.on(RECEIVE_CHAT, new Emitter.Listener() {
//                    @Override
//                    public void call(Object... args) {
//                        Log.d(TAG, "call: receive chat 채팅 받아라");
//                        JSONObject receivedData = (JSONObject) args[0];
//                        String userId = receivedData.optString(USER_ID);
//                        String message = receivedData.optString(MESSAGE);
//                        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
//                        String savedId = preferences.getString(USER_ID, null);
//                        if (!userId.equals(savedId)) {
//                            builder.setContentTitle(userId) // required
//                                    .setContentText(message)  // required
//                                    .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
//                                    .setAutoCancel(true) // 알림 터치시 반응 후 삭제
//
//                                    .setSound(RingtoneManager
//                                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                                    .setSmallIcon(android.R.drawable.btn_star)
////                            .setLargeIcon(BitmapFactory.decodeResource(getResources()
////                                    , R.drawable.msg_icon))
////                            .setBadgeIconType(R.drawable.msg_icon)
//                                    .setContentIntent(pendingIntent);
//                            notificationManager.notify(0, builder.build());
//
//                            Intent broadCast = new Intent(RECEIVE_CHAT);
//                            broadCast.putExtra(USER_ID, userId);
//                            broadCast.putExtra(MESSAGE, message);
//                            sendBroadcast(broadCast);
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    private void singInNameSpaceSocketOnEvent() {
//
//        mSocket.on(SIGN_IN_FAILED, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Intent broadCast = new Intent(SIGN_IN_FAILED);
//                sendBroadcast(broadCast);
//            }
//        });
//
//        mSocket.on(SIGN_IN_SUCCESS, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                JSONObject receivedData = (JSONObject) args[0];
//                SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString(USER_NAME, receivedData.optString(USER_NAME));
//                editor.putString(PASSWORD, receivedData.optString(PASSWORD));
//                editor.putString(E_MAIL, receivedData.optString(E_MAIL));
//                editor.commit();
//                Intent broadCast = new Intent(SIGN_IN_SUCCESS);
//                sendBroadcast(broadCast);
//            }
//        });
//
////        mSocket.on("test", new Emitter.Listener() {
////            @Override
////            public void call(Object... args) {
////
////            }
////        });
//    }
//
//    private void chatNameSpaceSocketOnEvent() {
//
//        mSocket.on(SET_CHAT_LIST, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                JSONArray receivedData = (JSONArray) args[0];
//                Intent broadCast = new Intent(SET_CHAT_LIST);
//                broadCast.putExtra(CHATS, receivedData.toString());
//                sendBroadcast(broadCast);
//            }
//        });
//
//        mSocket.on(INVITED_JOIN, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                JSONObject receivedData = (JSONObject) args[0];
//                Intent broadCast = new Intent(INVITED_JOIN);
//                broadCast.putExtra(ROOM_NAME, receivedData.optString(ROOM_NAME));
//                broadCast.putExtra(JOIN_MEMBERS, receivedData.optString(JOIN_MEMBERS));
//                sendBroadcast(broadCast);
//            }
//        });
//
//        mSocket.on(JOIN_SUCCESS, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                JSONObject receivedData = (JSONObject) args[0];
//                Intent broadCast = new Intent(JOIN_SUCCESS);
//                broadCast.putExtra(USER_NAME, receivedData.optString(USER_NAME));
//                broadCast.putExtra(ROOM_NAME, receivedData.optString(ROOM_NAME));
//                sendBroadcast(broadCast);
//            }
//        });
//
//        mSocket.on(GET_STORED_CHAT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                JSONArray receivedData = (JSONArray) args[0];
//                Intent broadCast = new Intent(GET_STORED_CHAT);
//                broadCast.putExtra(CHATS, receivedData.toString());
//                sendBroadcast(broadCast);
//            }
//        });
//
//        mSocket.on(RECEIVE_CHAT, new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                Log.d(TAG, "call: receive chat 채팅 받아라");
//                JSONObject receivedData = (JSONObject) args[0];
//                String userId = receivedData.optString(USER_ID);
//                String message = receivedData.optString(MESSAGE);
//                SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
//                String savedId = preferences.getString(USER_ID, null);
//                if (!userId.equals(savedId)) {
//                    builder.setContentTitle(userId) // required
//                            .setContentText(message)  // required
//                            .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
//                            .setAutoCancel(true) // 알림 터치시 반응 후 삭제
//
//                            .setSound(RingtoneManager
//                                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                            .setSmallIcon(android.R.drawable.btn_star)
////                            .setLargeIcon(BitmapFactory.decodeResource(getResources()
////                                    , R.drawable.msg_icon))
////                            .setBadgeIconType(R.drawable.msg_icon)
//                            .setContentIntent(pendingIntent);
//                    notificationManager.notify(0, builder.build());
//
//                    Intent broadCast = new Intent(RECEIVE_CHAT);
//                    broadCast.putExtra(USER_ID, userId);
//                    broadCast.putExtra(MESSAGE, message);
//                    sendBroadcast(broadCast);
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, "onDestroy: ");
//    }
//}
