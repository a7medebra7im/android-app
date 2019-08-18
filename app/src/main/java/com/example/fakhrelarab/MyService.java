package com.example.fakhrelarab;



import android.app.Notification;
import android.app.NotificationManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyService extends FirebaseMessagingService {
    public MyService() {
    }

    private String not_Tit;




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if(remoteMessage.getNotification()!=null)
        {
            not_Tit = remoteMessage.getNotification().getTag();
            start_Not(not_Tit);
        }

    }




    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


    }

    public void start_Not(String T)
    {
        Notification.Builder builder = new Notification.Builder(getBaseContext());

        builder.setContentTitle(T)
                .setSmallIcon(R.drawable.sssss)
                 .setAutoCancel(true);

        Notification not = builder.build();
        NotificationManager n = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        n.notify(1,not);

    }






















}
