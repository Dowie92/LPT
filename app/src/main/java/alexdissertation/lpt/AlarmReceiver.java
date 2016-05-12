package alexdissertation.lpt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Alex on 12/05/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Times Up")
                .setContentText("The notification you have set is happening!")
                .setSmallIcon(R.drawable.ic_date_range_white_18dp)
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Detail.class), 0))
                .build();

        notificationManager.notify(0, notification);
    }
}