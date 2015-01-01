package de.devisnik.android.mine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Spinner;
import de.devisnik.android.mine.data.ReadGameCommand;
import de.devisnik.android.mine.data.SaveGameCommand;
import de.devisnik.android.mine.device.IDevice;
import de.devisnik.mine.IGame;
import de.devisnik.mine.MinesGameAdapter;

public class MineSweeper extends Activity {

	private static final int HIGHSCORES_REQUEST = 42;
	private static final String SKIP_CACHE = "skip_cache";
	private static final int DIALOG_NEW_GAME = 1;
	private static final int DIALOG_INTRO = 4;
	private static final String GAME_CACHE_FILE = "game.cache";
	private static final Logger LOGGER = new Logger(MineSweeper.class);

	private class NewGameDialogBuilder extends Builder {

		public NewGameDialogBuilder() {
			super(MineSweeper.this);
			// setTheme(android.R.style.Theme_Dialog);
			ViewGroup layout = (ViewGroup) getLayoutInflater().inflate(R.layout.new_game, null);
			final PreferenceSpinnerController boardSpinnerController = createSpinnerController(
					R.string.prefkey_board_size, R.array.sizes_values, R.id.BoardSpinner, layout);
			final PreferenceSpinnerController levelSpinnerController = createSpinnerController(
					R.string.prefkey_game_level, R.array.levels_values, R.id.LevelSpinner, layout);

			setView(layout);
			setTitle(R.string.another_game);
			setPositiveButton(R.string.another_game_yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, final int id) {
					boardSpinnerController.updatePreference();
					levelSpinnerController.updatePreference();
					restartWithNewGame();
				}
			});
			setNegativeButton(R.string.another_game_no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface dialog, final int id) {
					boardSpinnerController.reset();
					levelSpinnerController.reset();
				}
			});
			setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(final DialogInterface dialog) {
					boardSpinnerController.reset();
					levelSpinnerController.reset();
				}
			});
		}

		private PreferenceSpinnerController createSpinnerController(final int prefKeyId, final int valueArrayId,
				final int viewId, final ViewGroup layout) {
			Spinner spinner = (Spinner) layout.findViewById(viewId);
			return new PreferenceSpinnerController(prefKeyId, valueArrayId, spinner);
		}
	}

	private BoardController itsBoardController;
	private BombsController itsBombsController;
	private MessagesController itsMessagesController;
	private TimerController itsTimerController;
	private Settings itsSettings;
	private IGame itsGame;
	private GameTimer itsGameTimer;
	private GameListener itsGameListener;
	private boolean itsSkipCache;
	private Notifier itsNotifier;
	private IDevice mDevice;
	private MenuItem mZoomMenu;

	private class GameListener extends MinesGameAdapter {
		@Override
		public void onBusted() {
			onGameLost();
		}

		@Override
		public void onDisarmed() {
			onGameWon();
		}
	}

	private int getBuildNumber() {
		try {
			PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
			return pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			LOGGER.e("Package name not found", e);
			return 0;
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		debugLog("onCreate");
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
		mDevice = ((MinesApplication) getApplication()).getDevice();
		setFullScreenMode();
		handleIntentExtras();
		itsSettings = new Settings(this);
		GameInfo gameInfo = new GameInfo(itsSettings);
		mDevice.setGameTitle(this, getTitle(), gameInfo.createTitle());
		itsNotifier = new Notifier(this, gameInfo);
	}

	private void setFullScreenMode() {
		mDevice.setFullScreen(this);
	}

	private void handleIntentExtras() {
		Intent intent = getIntent();
		itsSkipCache = intent.getBooleanExtra(SKIP_CACHE, false);
		intent.removeExtra(SKIP_CACHE);
	}

	private void debugLog(final String msg) {
		LOGGER.d(msg);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		debugLog("onRetainNonConfigurationInstance");
		return itsGame;
	}

	@Override
	protected void onStart() {
		debugLog("onStart");
		itsNotifier.clearRunningGame();
		super.onStart();
		setTheme(itsSettings.getTheme());
		setContentView(R.layout.main);
		GameInfoView levelView = (GameInfoView) findViewById(R.id.level);
		if (levelView != null)
			levelView.setText(new GameInfo(itsSettings).createTitle());
		CounterView timerView = (CounterView) findViewById(R.id.time);
		CounterView bombsView = (CounterView) findViewById(R.id.count);
		BoardView boardView = (BoardView) findViewById(R.id.board);
		itsGame = restoreOrCreateGame((IGame) getLastNonConfigurationInstance());
		itsGameListener = new GameListener();
		itsGame.addListener(itsGameListener);
		itsGameTimer = new GameTimer(itsGame);
		itsTimerController = new TimerController(itsGame, timerView);
		itsBombsController = new BombsController(itsGame, bombsView);
		itsBoardController = new BoardController(itsGame, boardView, itsSettings);
		itsMessagesController = new MessagesController(itsGame, this, itsSettings);
		showIntroOnAppStart();
	}

	private IGame restoreOrCreateGame(final IGame lastKnownGame) {
		if (lastKnownGame != null)
			return lastKnownGame;
		return readCachedGameOrCreateNew();
	}

	private IGame readCachedGameOrCreateNew() {
		IGame game = null;
		if (!itsSkipCache)
			game = new ReadGameCommand(this, GAME_CACHE_FILE).execute();
		if (game == null)
			game = new GameCreator(itsSettings).create();
		return game;
	}

	@Override
	protected void onResume() {
		debugLog("onResume");
		super.onResume();
		itsGameTimer.resume();
	}

	private void showIntroOnAppStart() {
		int buildNumber = getBuildNumber();
		if (buildNumber != itsSettings.getLastUsedBuild()) {
			if (!itsGame.isStarted())
				showDialog(DIALOG_INTRO);
			itsSettings.setLastUsedBuild(buildNumber);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		new MenuInflater(this).inflate(R.menu.menu, menu);
		mZoomMenu = menu.findItem(R.id.zoom);
		adjustZoomIcon();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		adjustZoomMenuVisibility(menu);
		return super.onPrepareOptionsMenu(menu);
	}

	private void adjustZoomMenuVisibility(final Menu menu) {
		final MenuItem zoom = menu.findItem(R.id.zoom);
		zoom.setVisible(!shouldHideZoomAction());
	}

	private boolean shouldHideZoomAction() {
		// hide zoom/fit items if board is too small for zooming
		return !itsBoardController.isBoardFullyVisibleForFieldSize(itsSettings.getZoomFieldSize()) || mDevice.isGoogleTv();
	}

	@Override
	protected void onPause() {
		debugLog("onPause");
		itsGameTimer.pause();
		new SaveGameCommand(this, GAME_CACHE_FILE, itsGame).execute();
		super.onPause();
	}

	@Override
	protected void onStop() {
		debugLog("onStop");
		itsGameTimer.dispose();
		itsBoardController.dispose();
		itsBombsController.dispose();
		itsTimerController.dispose();
		itsMessagesController.dispose();
		itsGame.removeListener(itsGameListener);
		if (itsSettings.isNotify())
			itsNotifier.notifyRunningGame(itsGame);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		debugLog("onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		debugLog("onRestart");
		// make sure that cache is read on a restart
		itsSkipCache = false;
		super.onRestart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onSearchRequested()
	 * 
	 * Prohibit searching when hitting search button. We handle this button
	 * ourselves.
	 */
	@Override
	public boolean onSearchRequested() {
		return false;
	}

	@Override
	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			if (itsBoardController.isBoardFullyVisibleForFieldSize(itsSettings.getZoomFieldSize())) {
				itsSettings.toogleZoom();
				itsBoardController.onZoomChange();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, MinesPreferences.class));
			break;
		case R.id.scores:
			startActivity(new Intent(this, HighScores.class));
			break;
		case R.id.new_game:
			showDialog(DIALOG_NEW_GAME);
			break;
		case R.id.zoom:
			itsSettings.toogleZoom();
			adjustZoomIcon();
			itsBoardController.onZoomChange();
			break;
		case R.id.help:
			showDialog(DIALOG_INTRO);
			break;
		}
		return true;
	}

	private void adjustZoomIcon() {
		if (mZoomMenu == null) // menu not created yet
			return;
		if (!itsSettings.isZoom())
			setZoomMenuIconAndText(R.drawable.ic_action_zoom_in, R.string.menu_zoom);
		else
			setZoomMenuIconAndText(R.drawable.ic_action_zoom_out, R.string.menu_fit);
	}

	private void setZoomMenuIconAndText(final int iconId, final int textId) {
		mZoomMenu.setIcon(iconId);
		mZoomMenu.setTitle(textId);
	}

	@Override
	public void onWindowFocusChanged(final boolean hasFocus) {
		if (hasFocus)
			itsGameTimer.resume();
		else
			itsGameTimer.pause();
		super.onWindowFocusChanged(hasFocus);
	}

	private void restartWithNewGame() {
		final Intent intent = new Intent(this, MineSweeper.class);
		intent.putExtra(SKIP_CACHE, true);
		itsNotifier.disable();
		finish();
		startActivity(intent);
	}

	@Override
	protected Dialog onCreateDialog(final int id) {
		switch (id) {
		case DIALOG_NEW_GAME:
			return new NewGameDialogBuilder().create();
		case DIALOG_INTRO:
			AlertDialog dialog = new IntroDialogBuilder(this).create();

			/*
			 * we need to manually adjust the dialog width.
			 * 
			 * see:
			 * http://stackoverflow.com/questions/2306503/how-to-make-an-alert
			 * -dialog-fill-90-of-screen-size
			 */
			dialog.setOnShowListener(new OnShowListener() {

				@Override
				public void onShow(final DialogInterface dialogInterface) {
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					Dialog dialogImpl = (Dialog) dialogInterface;
					lp.copyFrom(dialogImpl.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.MATCH_PARENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					dialogImpl.getWindow().setAttributes(lp);
				}
			});
			return dialog;
		default:
			return super.onCreateDialog(id);
		}
	}

	private void onGameWon() {
        Intent intent = HighScores.withTime(this, itsGame.getWatch().getTime());
        startActivityForResult(intent, HIGHSCORES_REQUEST);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		debugLog("onActivityResult");
		if (requestCode == HIGHSCORES_REQUEST)
			showDialog(DIALOG_NEW_GAME);
	}

	private void onGameLost() {
		showDialog(DIALOG_NEW_GAME);
	}

}
