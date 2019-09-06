package com.taxidriver.app.Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taxidriver.app.Map.DashBoard;
import com.taxidriver.app.Model.RideRequest;
import com.taxidriver.app.Model.Riderequest;
import com.taxidriver.app.R;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    //    private static final String TAG = ;
    private static final String MESSAGE_NOTIFICATION_CHANNEL_ID = "message_notification_channel";

    public MyFirebaseMessagingService() {
        super();

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> payload= remoteMessage.getData();
//        Log.e("MyFirebaseMessagingService", "From: " + remoteMessage.getFrom());
//        Log.e("MyFirebaseMessagingService", "Notification Message Body: " + payload.get("user_fullname"));
//        String message="this";
//        String req_id=payload.get("request_id");
//
//        Log.e("req", "onMessageReceived: "+req_id );
//        message=payload.get("name").concat(" Ride Request");

        final NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel
                    (MESSAGE_NOTIFICATION_CHANNEL_ID,"Message Channel",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

//        final PendingIntent pendingIntent=PendingIntent.getActivity(this,12,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        String finalMessage = "You have a new Ride ";

        Intent resultIntent = new Intent(getApplicationContext(), DashBoard.class);
        resultIntent.putExtra("request",true);
        Riderequest request=new Riderequest(payload.get("u_name"),
                payload.get("d_name"),
                payload.get("s_name"),
                payload.get("request_id"),
                payload.get("s_lat"),
                payload.get("s_lng"),
                payload.get("d_lat"),
                payload.get("d_lng"));
        LocalPersistence.witeObjectToFile(getApplicationContext(),request,"map");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //TODO:you have to change from this
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.app_icon);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder=new NotificationCompat.Builder
                (MyFirebaseMessagingService.this,MESSAGE_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(MyFirebaseMessagingService.this,R.color.colorAccent))
                .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon(icon)
                .setSound(soundUri)
                .setContentTitle("YaadtaxiDriver")
                .setContentText(finalMessage)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);


        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        notificationManager.notify(0,builder.build());

//        Glide.with(this).asBitmap().load(R.drawable.app_icon).into(new CustomTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                Intent resultIntent = new Intent(getApplicationContext(), DashBoard.class);
//                resultIntent.putExtra("request",true);
//                Riderequest request=new Riderequest(payload.get("u_name"),
//                        payload.get("d_name"),
//                        payload.get("s_name"),
//                        payload.get("request_id"),
//                        payload.get("s_lat"),
//                        payload.get("s_lng"),
//                        payload.get("d_lat"),
//                        payload.get("d_lng"));
//                LocalPersistence.witeObjectToFile(getApplicationContext(),request,"map");
//                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//                stackBuilder.addNextIntentWithParentStack(resultIntent);
//                PendingIntent resultPendingIntent =
//                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//                NotificationCompat.Builder builder=new NotificationCompat.Builder
//                        (MyFirebaseMessagingService.this,MESSAGE_NOTIFICATION_CHANNEL_ID)
//                        .setColor(ContextCompat.getColor(MyFirebaseMessagingService.this,R.color.colorAccent))
//                        .setSmallIcon(R.drawable.app_icon)
//                        .setLargeIcon(resource)
//                        .setContentTitle("YaadtaxiDriver")
//                        .setContentText(finalMessage)
//                        .setAutoCancel(false)
//                        .setContentIntent(resultPendingIntent);
//
//
//                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT<Build.VERSION_CODES.O){
//                    builder.setPriority(Notification.PRIORITY_HIGH);
//                }
//
//                notificationManager.notify(0,builder.build());
//
//
//            }
//
//            @Override
//            public void onLoadCleared(@Nullable Drawable placeholder) {
//
//            }
//        });


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