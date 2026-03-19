package com.dukenightshade.abowatch.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import com.dukenightshade.abowatch.R;

/**
 * Hilfsklasse zum Erstellen und Anzeigen von Benachrichtigungen.
 * @author Nico Hoffmann
 * @version 1.0
 */
public class NotificationHelper {

    // ====================================
    // Constants
    // ====================================

    public static final String CHANNEL_ID = "abowatch_channel";

    // ====================================
    // Constructor
    // ====================================

    private NotificationHelper() {
        // Utility class – no instantiation
    }

    // ====================================
    // Business Logic Methods
    // ====================================

    public static void createNotificationChannel(Context context) {
        String channelName = context.getString(R.string.notification_channel_name);
        String channelDesc = context.getString(R.string.notification_channel_desc);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channel.setDescription(channelDesc);

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    public static void sendNotification(Context context, int notificationId,
                                        String subscriptionName, String cancellationDate) {
        String title = context.getString(R.string.notification_title);
        String contentText = subscriptionName + " – "
                + context.getString(R.string.notification_content, cancellationDate);
        String bigText = context.getString(
                R.string.notification_big_text, subscriptionName, cancellationDate);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId, builder.build());
    }
}
