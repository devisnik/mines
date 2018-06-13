package de.devisnik.android.mine;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import de.devisnik.android.mine.base.R;
import de.devisnik.mine.IGame;

public class Notifier {
    private static final int GAME_INFO_ID = 1;
    private static final String MINES_CHANNEL = "mines-channel";
    private final Context context;
    private final NotificationManager notificationManager;
    private boolean isDisabled;
    private GameInfo gameInfo;

    public Notifier(MineSweeper context, GameInfo gameInfo) {
        this.context = context.getApplicationContext();
        this.gameInfo = gameInfo;
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.notification_channel_name);
            String description = context.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(MINES_CHANNEL, name, importance);
            channel.setDescription(description);
            channel.setImportance(NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notifyRunningGame(IGame game) {
        if (isDisabled || !game.isRunning())
            return;
        CharSequence contentTitle = gameInfo.createTitle();
        CharSequence contentText = gameInfo.createStatus(game);
        Intent notificationIntent = new Intent(context, MineSweeper.class);
        //make sure we open game activity if currently settings or highscores is active
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                0);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setSmallIcon(R.drawable.bomb_notification)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(MINES_CHANNEL);
        }

        notificationManager.notify(GAME_INFO_ID, builder.getNotification());
    }

    public void clearRunningGame() {
        notificationManager.cancel(GAME_INFO_ID);
    }

    public void disable() {
        isDisabled = true;
    }
}
