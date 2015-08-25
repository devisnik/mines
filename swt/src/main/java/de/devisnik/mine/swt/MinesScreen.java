package de.devisnik.mine.swt;

import de.devisnik.mine.GameFactory;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;
import de.devisnik.mine.robot.AutoPlayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MinesScreen {

    private Composite gameComposite;
    private AutoPlayer autoPlayer;
    private Counter flagCountDown;
    private Counter timeCounter;
    private Timer secondsTimer;

    private final IMinesGameListener itsMinesGameListener = new IMinesGameListener() {

        public void onBusted() {
            stopTimer();
            fireDoneDelayed();
        }

        private void fireDoneDelayed() {
            gameComposite.getDisplay().timerExec((int) SECONDS.toMillis(5), new Runnable() {

                public void run() {
                    System.exit(0);
                }
            });
        }

        public void onChange(final int flags, final int mines) {
            if (!flagCountDown.isDisposed()) {
                flagCountDown.reset(mines - flags);
            }
        }

        public void onDisarmed() {
            stopTimer();
            fireDoneDelayed();
        }

        public void onStart() {
            startTimer();
        }
    };

    private static GridLayout createLayout(int numColumns) {
        final GridLayout layout = new GridLayout(numColumns, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        return layout;
    }

    public void createControl(final Composite parent) {
        IGame game = GameFactory.create(40, 20, 150);
        game.addListener(itsMinesGameListener);
        GameCanvas gameCanvas = setupGameUI(parent, game);
        autoPlayer = new AutoPlayer(game, true);
        gameCanvas.getDisplay().timerExec((int) SECONDS.toMillis(1), new Runnable() {

            public void run() {
                autoPlayer.doNextMove();
            }
        });
    }

    private GameCanvas setupGameUI(final Composite parent, final IGame game) {
        final Color black = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        Composite rootComposite = new Composite(parent, SWT.NONE);
        final GridLayout rootLayout = createLayout(1);
        rootComposite.setLayout(rootLayout);
        gameComposite = new Composite(rootComposite, SWT.NONE);
        gameComposite.setLayout(createLayout(2));
        gameComposite.setLayoutData(createFillGridData());
        gameComposite.setBackground(black);
        MinesImages images = new MinesImages(parent.getDisplay());
        flagCountDown = new Counter(gameComposite, SWT.NONE, 3, game
                .getBombCount(), images);
        timeCounter = new Counter(gameComposite, SWT.NONE, 3, 0, images);
        final GridData timerGridData = new GridData();
        timerGridData.horizontalAlignment = SWT.END;
        timeCounter.setLayoutData(timerGridData);
        GameCanvas canvas = new GameCanvas(gameComposite, SWT.NONE, game);
        canvas.setLayoutData(createGameCanvasGridData());
        return canvas;
    }

    private GridData createGameCanvasGridData() {
        final GridData gameGridData = new GridData();
        gameGridData.horizontalSpan = 2;
        return gameGridData;
    }

    private GridData createFillGridData() {
        final GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.verticalAlignment = SWT.FILL;
        return gridData;
    }

    private void startTimer() {
        secondsTimer = new Timer();
        final Display display = Display.getCurrent();
        secondsTimer.schedule(new TimerTask() {

            public void run() {
                display.asyncExec(new Runnable() {

                    public void run() {
                        if (!timeCounter.isDisposed()) {
                            timeCounter.increase();
                            autoPlayer.doNextMove();
                        }
                    }
                });
            }
        }, SECONDS.toMillis(1), SECONDS.toMillis(1));
    }

    private void stopTimer() {
        if (secondsTimer != null) {
            secondsTimer.cancel();
        }
    }

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout(new GridLayout(1, false));
        new MinesScreen().createControl(shell);
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}
