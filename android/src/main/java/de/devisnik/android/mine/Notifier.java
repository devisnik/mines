package de.devisnik.android.mine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import de.devisnik.mine.IGame;

public class Notifier {
	private static final int GAME_INFO_ID = 1;
	private final Context itsContext;
	private final NotificationManager mNotificationManager;
	private final Notification itsNotification;
	private boolean itsIsDisabled;
	private GameInfo itsGameInfo;

	public Notifier(MineSweeper context, GameInfo gameInfo) {
		itsContext = context.getApplicationContext();
		itsGameInfo = gameInfo;
		mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		itsNotification = new Notification(R.drawable.bomb_notification, null, System.currentTimeMillis());
		itsNotification.flags |= Notification.FLAG_AUTO_CANCEL;
	}

	public void notifyRunningGame(IGame game) {
		if (itsIsDisabled || !game.isRunning())
			return;
		CharSequence contentTitle = itsGameInfo.createTitle();
		CharSequence contentText = itsGameInfo.createStatus(game);
		Intent notificationIntent = new Intent(itsContext, MineSweeper.class);
		//make sure we open game activity if currently settings or highscores is active
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(itsContext, 0, notificationIntent,
				0);
		itsNotification.setLatestEventInfo(itsContext, contentTitle, contentText, contentIntent);
		mNotificationManager.notify(GAME_INFO_ID, itsNotification);
	}
	
	public void clearRunningGame() {
		mNotificationManager.cancel(GAME_INFO_ID);
	}
	
	public void disable() {
		itsIsDisabled = true;
	}
}
