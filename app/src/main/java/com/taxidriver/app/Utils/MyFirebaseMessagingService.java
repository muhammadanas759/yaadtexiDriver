package com.taxidriver.app.Utils;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taxidriver.app.R;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    private static final String TAG = ;

    public MyFirebaseMessagingService() {
        super();

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> payload=remoteMessage.getData();
//        Log.e("MyFirebaseMessagingService", "From: " + remoteMessage.getFrom());
        Log.e("MyFirebaseMessagingService", "Notification Message Body: " + payload.get("user_fullname"));
   String message;
       message=payload.get("name").concat(" is requesting for Ride");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle("Ride Request Notification")
                        .setContentText(message);

//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);
        // Add as notification

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);

    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

}
