package com.example.leeyh.abroadapp.background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.leeyh.abroadapp.view.activity.TabBarMainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE;
import static com.example.leeyh.abroadapp.constants.StaticString.MESSAGE_FROM_SERVICE;
import static com.example.leeyh.abroadapp.constants.StaticString.RECEIVED_DATA;
import static com.example.leeyh.abroadapp.constants.StaticString.ROOM_NAME;
import static com.example.leeyh.abroadapp.constants.StaticString.SEND_MESSAGE_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_ID;
import static com.example.leeyh.abroadapp.constants.StaticString.USER_INFO;

public class BackgroundChattingService extends Service {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private PendingIntent pendingIntent;
    private Intent notificationIntent;

    public BackgroundChattingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("서비스 999", "onCreate: ");
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("channel", "name", importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        builder = new NotificationCompat.Builder(getApplicationContext(), "channel");
        notificationIntent = new Intent(getApplicationContext()
                , TabBarMainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestID = (int) System.currentTimeMillis();
        pendingIntent = PendingIntent.getActivity(getApplicationContext()
                , requestID
                , notificationIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getStringExtra(RECEIVED_DATA) != null) {
                Log.d("서비스2", "onStartCommand: 메세지 도착");
                PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "new Message");
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
                    JSONObject jsonObject = new JSONObject(intent.getStringExtra(RECEIVED_DATA));
                    String sendMessageId = jsonObject.getString(SEND_MESSAGE_ID);
                    String roomName = jsonObject.getString(ROOM_NAME);
                    String message = jsonObject.getString(MESSAGE);
                    if(!sharedPreferences.getString(USER_ID, null).equals(sendMessageId)) {
                        notificationIntent.putExtra(MESSAGE_FROM_SERVICE, MESSAGE_FROM_SERVICE);
                        notificationIntent.putExtra(ROOM_NAME, roomName);
                        wakeLock.acquire(DateUtils.SECOND_IN_MILLIS * 5);
                        builder.setContentTitle(sendMessageId) // required
                                .setContentText(message)  // required
                                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                                .setSound(RingtoneManager
                                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setSmallIcon(android.R.drawable.btn_star)
//                            .setLargeIcon(BitmapFactory.decodeResource(getResources()
//                                    , R.drawable.msg_icon))
//                            .setBadgeIconType(R.drawable.msg_icon)
                                .setContentIntent(pendingIntent);
                        notificationManager.notify(0, builder.build());
                        wakeLock.release();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return START_STICKY;
    }
}
