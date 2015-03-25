package de.devisnik.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;
import de.devisnik.web.client.robot.AutoPlayer;

public class GamePanel extends VerticalPanel {
    private final IGame game;
    private IMinesGameListener gameListener;
    private boolean isGameFinished;

    private BoardCanvas gameCanvas;
    private Counter minesCounter;
    private StopWatch stopWatch;
    private CheckBox clickOpenButton;
    private TextBox messageBox;
    private Timer itsAutoPlayTimer;

    public GamePanel(final IGame game) {
        this.game = game;

        initClickModeSelectionUI(game);
        initCountersUI(game);
        initBoardUI(game);
        initMessageUI(game);

        final AutoPlayer autoPlayer = new AutoPlayer(game, false);
        itsAutoPlayTimer = new Timer() {
            public void run() {
                autoPlayer.doNextMove();
            }
        };
        gameListener = new IMinesGameListener() {
            public void onBusted() {
                stopWatch.stop();
                messageBox.setText("Exploded!");
				itsAutoPlayTimer.cancel();
                setGameFinished(true);
            }

            public void onChange(int flags, int mines) {
                int bombsToFind = mines - flags;
                messageBox.setText(Integer.toString(bombsToFind)
                        + " mines left to find.");
                minesCounter.setValue(bombsToFind);
            }

            public void onDisarmed() {
                stopWatch.stop();
                messageBox.setText("Mines cleared!");
				itsAutoPlayTimer.cancel();
                setGameFinished(true);
            }

            public void onStart() {
                stopWatch.start();
                messageBox.setText(Integer.toString(game.getBombCount())
                        + " mines left to find.");
            }
        };
        game.addListener(gameListener);
        final CheckBox autoPlay = new CheckBox("Let the browser play.");
        autoPlay.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() && !isGameFinished()) {
                    itsAutoPlayTimer.scheduleRepeating(1000);
                } else {
                    itsAutoPlayTimer.cancel();
                }
            }
        });
        add(autoPlay);
    }

    private void initBoardUI(final IGame game) {
        final ClickHandler clickListener = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean shiftKey = event.isShiftKeyDown();
                FieldCanvas canvas = (FieldCanvas) event.getSource();
                if (shiftKey) {
                    if (clickOpenButton.getValue()) {
                        game.onRequestFlag(canvas.getField());
                    } else {
                        game.onRequestOpen(canvas.getField());
                    }
                } else {
                    if (clickOpenButton.getValue()) {
                        game.onRequestOpen(canvas.getField());
                    } else {
                        game.onRequestFlag(canvas.getField());
                    }
                }
            }
        };
        gameCanvas = new BoardCanvas(game.getBoard(), clickListener);
        add(gameCanvas);
    }

    private void initMessageUI(IGame game) {
        messageBox = new TextBox();
        messageBox.setReadOnly(true);
        messageBox.setWidth(Integer
                .toString(FieldCanvas.SIZE * game.getBoard().getDimension().x)
                + "px");
        messageBox.setText("Click field to start game.");
        add(messageBox);
    }

    private void initCountersUI(IGame game) {
        DockPanel counterPanel = new DockPanel();
        add(counterPanel);
        minesCounter = new Counter(3);
        minesCounter.setValue(game.getBombCount());
        counterPanel.add(minesCounter, DockPanel.WEST);
        final Counter timeCounter = new Counter(3);
        counterPanel.setHorizontalAlignment(DockPanel.ALIGN_RIGHT);
        counterPanel.add(timeCounter, DockPanel.EAST);
        counterPanel.setWidth(Integer.toString(FieldCanvas.SIZE * game.getBoard().getDimension().x) + "px");
        stopWatch = new StopWatch() {
            @Override
            public void setTime(int value) {
                timeCounter.setValue(value);
            }
        };
    }

    private void initClickModeSelectionUI(IGame game) {
        DockPanel clickModePanel = new DockPanel();
        add(clickModePanel);
        clickModePanel.setWidth(Integer.toString(FieldCanvas.SIZE * game.getBoard().getDimension().x) + "px");
        clickModePanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        clickOpenButton = new RadioButton("ClickOpens", "click opens");
        clickOpenButton.setValue(true);
        final CheckBox clickFlagButton = new RadioButton("ClickFlags", "click flags");
        clickFlagButton.setValue(false);
        ValueChangeHandler<Boolean> checkBoxToggler = new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                CheckBox checkBox = (CheckBox) event.getSource();
                if (checkBox == clickOpenButton)
                    clickFlagButton.setValue(!event.getValue());
                else
                    clickOpenButton.setValue(!event.getValue());
            }
        };
        clickOpenButton.addValueChangeHandler(checkBoxToggler);
        clickFlagButton.addValueChangeHandler(checkBoxToggler);
        clickModePanel.add(clickFlagButton, DockPanel.WEST);
        clickModePanel.add(clickOpenButton, DockPanel.EAST);
        clickModePanel.setCellHorizontalAlignment(clickOpenButton, DockPanel.ALIGN_LEFT);
        clickModePanel.setCellHorizontalAlignment(clickFlagButton, DockPanel.ALIGN_RIGHT);
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
		itsAutoPlayTimer.cancel();
        game.removeListener(gameListener);
        gameCanvas.dispose();

    }
}
