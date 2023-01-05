package com.bapoto.bapotodriver.firebase;





import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bapoto.bapotodriver.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class NotificationService extends FirebaseMessagingService {


    private static final String CANAL = "My notif canal";

    @Override
     public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
         super.onMessageReceived(remoteMessage);

         String myMessage = remoteMessage.getNotification().getBody();
         Log.d("FireBaseMessage", "NOUVELLE NOTIFICATION : " + myMessage);

        // Créer notification
         NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CANAL);
         builder.setContentTitle("NOTIFICATION");
         builder.setContentText("Réservation");
         builder.setSmallIcon(R.drawable.ic_notification);


         //envoyer la notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.notification_channel_id);
            String channelTitle = getString(R.string.notification_channel_title);
            String channelDescription = getString(R.string.notification_channel_descrption);
            NotificationChannel channel = new NotificationChannel(channelId,channelTitle,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
         }
        notificationManager.notify(1, builder.build());




     }
}