package de.devisnik.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;
import de.devisnik.mine.IStopWatchListener;
import de.devisnik.mine.robot.AutoPlayer;

public class GamePanel extends VerticalPanel {
    private static final int ONE_SECOND_IN_MILLIS = 1000;

    private final IGame game;
    private IMinesGameListener gameListener;
    private boolean isGameFinished;

    private final Timer timeTicker;
    private BoardCanvas gameCanvas;
    private Counter minesCounter;
    private CheckBox clickOpenButton;
    private TextBox messageBox;
    private Timer autoPlayTimer;
    private CheckBox clickFlagButton;

    public GamePanel(final IGame game) {
        this.game = game;

        initClickModeSelectionUI(game);
        initCountersUI();
        initBoardUI();
        initMessageUI();
        initAutoPlay();

        initGameListener();
        timeTicker = new Timer() {
            @Override
            public void run() {
                GamePanel.this.game.tickWatch();
            }
        };
        if (game.isRunning())
            startWatch();
    }

    private void startWatch() {
        timeTicker.scheduleRepeating(ONE_SECOND_IN_MILLIS);
    }

    private void initGameListener() {
        gameListener = new IMinesGameListener() {
            @Override
            public void onBusted() {
                messageBox.setText("Exploded!");
                autoPlayTimer.cancel();
                setGameFinished(true);
            }

            @Override
            public void onChange(int flags, int mines) {
                int bombsToFind = mines - flags;
                messageBox.setText(Integer.toString(bombsToFind)
                        + " mines left to find.");
                minesCounter.setValue(bombsToFind);
            }

            @Override
            public void onDisarmed() {
                messageBox.setText("Mines cleared!");
                autoPlayTimer.cancel();
                setGameFinished(true);
            }

            @Override
            public void onStart() {
                startWatch();
                messageBox.setText(game.getBombCount() + " mines left to find.");
                if (clickOpenButton.getValue()) //switch to Flag Mode
                    clickFlagButton.setValue(true, true);
            }
        };
        game.addListener(gameListener);
    }

    private void initAutoPlay() {
        final AutoPlayer autoPlayer = new AutoPlayer(game, false);
        autoPlayTimer = new Timer() {
            public void run() {
                autoPlayer.doNextMove();
            }
        };
        final CheckBox autoPlay = new CheckBox("Let the browser play.");
        autoPlay.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() && !isGameFinished())
                    autoPlayTimer.scheduleRepeating(1000);
                else
                    autoPlayTimer.cancel();
            }
        });
        add(autoPlay);
    }

    private void initBoardUI() {
        final ClickHandler clickListener = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean shiftKey = event.isShiftKeyDown();
                FieldCanvas canvas = (FieldCanvas) event.getSource();
                IField field = canvas.getField();
                if (clickOpenButton.getValue() != shiftKey) //logical XOR
                    game.onRequestOpen(field);
                else
                    game.onRequestFlag(field);
            }
        };
        gameCanvas = new BoardCanvas(game.getBoard(), clickListener);
        add(gameCanvas);
    }

    private void initMessageUI() {
        messageBox = new TextBox();
        messageBox.setReadOnly(true);
        messageBox.setWidth(FieldCanvas.SIZE * game.getBoard().getDimension().x + "px");
        messageBox.setText("Click field to start game.");
        add(messageBox);
    }

    private void initCountersUI() {
        DockPanel counterPanel = new DockPanel();
        add(counterPanel);
        minesCounter = new Counter(3);
        minesCounter.setValue(game.getBombCount());
        counterPanel.add(minesCounter, DockPanel.WEST);
        final Counter timeCounter = new Counter(3);
        counterPanel.setHorizontalAlignment(DockPanel.ALIGN_RIGHT);
        counterPanel.add(timeCounter, DockPanel.EAST);
        counterPanel.setWidth(Integer.toString(FieldCanvas.SIZE * game.getBoard().getDimension().x) + "px");
        game.getWatch().addListener(new IStopWatchListener() {
            @Override
            public void onTimeChange(int currentTime) {
                timeCounter.setValue(currentTime);
            }
        });

    }

    private void initClickModeSelectionUI(IGame game) {
        DockPanel clickModePanel = new DockPanel();
        add(clickModePanel);
        clickModePanel.setWidth(Integer.toString(FieldCanvas.SIZE * game.getBoard().getDimension().x) + "px");
        clickModePanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        clickOpenButton = new RadioButton("ClickOpens", "click opens");
        clickOpenButton.setValue(true);
        clickFlagButton = new RadioButton("ClickFlags", "click flags");
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
        timeTicker.cancel();
        autoPlayTimer.cancel();
        game.removeListener(gameListener);
        gameCanvas.dispose();
    }
}
