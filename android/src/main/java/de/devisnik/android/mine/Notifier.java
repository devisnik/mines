package de.devisnik.android.mine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import de.devisnik.mine.IGame;

public class Notifier {
	private static final int GAME_INFO_ID = 1;
	private final Context context;
	private final NotificationManager notificationManager;
	private final Notification notification;
	private boolean isDisabled;
	private GameInfo gameInfo;

	public Notifier(MineSweeper context, GameInfo gameInfo) {
		this.context = context.getApplicationContext();
		this.gameInfo = gameInfo;
		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.bomb_notification, null, System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
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
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notificationManager.notify(GAME_INFO_ID, notification);
	}
	
	public void clearRunningGame() {
		notificationManager.cancel(GAME_INFO_ID);
	}
	
	public void disable() {
		isDisabled = true;
	}
}
