package de.devisnik.web.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;
//import de.devisnik.mine.robot.AutoPlayer;

public class GamePanel extends VerticalPanel {
	private BoardCanvas gameCanvas;
	private final IGame game;
	private IMinesGameListener gameListener;
	private boolean isGameFinished;
//	private Timer itsAutoPlayTimer;

	public GamePanel(final IGame game) {
		this.game = game;
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		DockPanel clickPanel = new DockPanel();
		add(clickPanel);
		clickPanel.setWidth(Integer.toString(15 * game.getBoard()
				.getDimension().x)
				+ "px");

		final CheckBox clickOpenButton = new CheckBox(
				"open");
		clickOpenButton.setChecked(true);
		final CheckBox clickFlagButton = new CheckBox(
				"flag");
		clickFlagButton.setChecked(false);
		clickOpenButton.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				clickFlagButton.setChecked(!clickOpenButton.isChecked());
			}
		});
		clickFlagButton.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				clickOpenButton.setChecked(!clickFlagButton.isChecked());
			}
		});
		clickPanel.setHorizontalAlignment(ALIGN_RIGHT);
		clickPanel.add(clickFlagButton, DockPanel.WEST);
		clickPanel.setHorizontalAlignment(ALIGN_LEFT);
		clickPanel.add(clickOpenButton, DockPanel.EAST);
		DockPanel counterPanel = new DockPanel();
		add(counterPanel);
		final Counter minesCounter = new Counter(3);
		minesCounter.setValue(game.getBombCount());
		counterPanel.add(minesCounter, DockPanel.WEST);
		final Counter timeCounter = new Counter(3);
		counterPanel.setHorizontalAlignment(ALIGN_RIGHT);
		counterPanel.add(timeCounter, DockPanel.EAST);
		counterPanel.setWidth(Integer.toString(15 * game.getBoard()
				.getDimension().x)
				+ "px");
		final StopWatch stopWatch = new StopWatch() {
			@Override
			public void setTime(int value) {
				timeCounter.setValue(value);
			}
		};
		final ClickListener clickListener = new ClickListener() {

			public void onClick(Widget sender) {
				Event currentEvent = DOM.eventGetCurrentEvent();
				boolean shiftKey = DOM.eventGetShiftKey(currentEvent);
                FieldCanvas canvas = (FieldCanvas) sender;
                if (shiftKey) {
                    if (clickOpenButton.isChecked()) {
                        game.onRequestFlag(canvas.getField());
                    } else {
                        game.onRequestOpen(canvas.getField());
                    }
                } else {
                    if (clickOpenButton.isChecked()) {
                        game.onRequestOpen(canvas.getField());
                    } else {
                        game.onRequestFlag(canvas.getField());
                    }
                }
            }
        };

		gameCanvas = new BoardCanvas(game.getBoard(), clickListener);
		add(gameCanvas);
		final TextBox textBox = new TextBox();
		textBox.setReadOnly(true);
		textBox.setWidth(Integer
				.toString(15 * game.getBoard().getDimension().x)
				+ "px");
		textBox.setText("Click field to start game.");
		add(textBox);

//		final AutoPlayer autoPlayer = new AutoPlayer(game, false);
//		itsAutoPlayTimer = new Timer() {
//			public void run() {
//				autoPlayer.doNextMove();
//			}
//		};
		gameListener = new IMinesGameListener() {

			public void onBusted() {
				stopWatch.stop();
				textBox.setText("Exploded!");
//				itsAutoPlayTimer.cancel();
				setGameFinished(true);
			}

			public void onChange(int flags, int mines) {
				int bombsToFind = mines - flags;
				textBox.setText(Integer.toString(bombsToFind)
						+ " mines left to find.");
				minesCounter.setValue(bombsToFind);
			}

			public void onDisarmed() {
				stopWatch.stop();
				textBox.setText("Mines cleared!");
//				itsAutoPlayTimer.cancel();
				setGameFinished(true);
			}

			public void onStart() {
				stopWatch.start();
				textBox.setText(Integer.toString(game.getBombCount())
						+ " mines left to find.");
			}
		};
		game.addListener(gameListener);
		final CheckBox autoPlay = new CheckBox("Let the browser play.");
//		autoPlay.addClickListener(new ClickListener() {
//
//			public void onClick(Widget sender) {
//				if (autoPlay.isChecked() && !isGameFinished()) {
//					itsAutoPlayTimer.scheduleRepeating(1000);
//				} else {
//					itsAutoPlayTimer.cancel();
//				}
//			}
//		});
		add(autoPlay);
	}

    public static native void alert(String msg) /*-{
      $wnd.alert(msg);
    }-*/;

    private boolean isGameFinished() {
		return isGameFinished;
	}

	private void setGameFinished(boolean value) {
		isGameFinished = value;
	}

	public void dispose() {
//		itsAutoPlayTimer.cancel();
		game.removeListener(gameListener);
		gameCanvas.dispose();

	}
}
