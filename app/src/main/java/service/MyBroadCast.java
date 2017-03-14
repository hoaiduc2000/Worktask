package service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import vn.softfront.worktask.MainActivity;
import vn.softfront.worktask.R;

/**
 * Created by admin on 12/9/16.
 */

public class MyBroadCast extends BroadcastReceiver {
    public MyBroadCast() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String title=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");
        int id=intent.getIntExtra("id", -1);
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true);


        Intent resIntent = new Intent(context,MainActivity.class);
//        resIntent.putExtra("id",id);
        PendingIntent resultPendingIntent=PendingIntent.getActivity(context,id,resIntent,0);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotification=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification.notify(id,mBuilder.build());
    }
}
