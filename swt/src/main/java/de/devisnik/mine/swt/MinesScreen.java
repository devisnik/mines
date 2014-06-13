package de.devisnik.mine.swt;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.devisnik.mine.GameFactory;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;
import de.devisnik.mine.robot.AutoPlayer;

public class MinesScreen {

	private GameCanvas itsGameCanvas;
	private Composite itsComposite;
	private AutoPlayer itsAutoPlayer;
	private Counter itsCounter;
	private Counter itsTimerCounter;
	private Timer itsTimer;

	private IGame itsGame;

	private final IMinesGameListener itsMinesGameListener = new IMinesGameListener() {

		public void onBusted() {
			stopTimer();
			fireDoneDelayed();
		}

		private void fireDoneDelayed() {
//			itsComposite.getDisplay().timerExec(5000, new Runnable() {
//
//				public void run() {
//					fireScreenDone();
//				}
//			});
		}

		public void onChange(final int flags, final int mines) {
			if (!itsCounter.isDisposed()) {
				itsCounter.reset(mines - flags);
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
	private Composite itsRootComposite;

	public MinesScreen() {
//		super("Minesweeper");
	}

    private static GridLayout createLayout(int numColumns) {
        GridLayout result = new GridLayout(numColumns, true);
        result.horizontalSpacing = 0;
        result.marginBottom = 0;
        result.marginHeight = 0;
        result.marginLeft = 0;
        result.marginRight = 0;
        result.marginTop = 0;
        result.marginWidth = 0;
        result.verticalSpacing = 0;
        return result;
    }

    public void createControl(final Composite parent) {
		itsGame = GameFactory.create(10,
                10, 10);
		itsGame.addListener(itsMinesGameListener);
		final Color black = parent.getDisplay().getSystemColor(SWT.COLOR_BLACK);
		itsRootComposite = new Composite(parent, SWT.NONE);
		itsRootComposite.setBackground(black);
		itsRootComposite.setLayout(createLayout(1));
		itsComposite = new Composite(itsRootComposite, SWT.NONE);
		itsComposite.setLayout(createLayout(2));
//		itsComposite.setLayoutData(GridDataFactory.fillDefaults().align(
//				SWT.CENTER, SWT.CENTER).grab(true, true).create());
		itsComposite.setBackground(black);
		itsCounter = new Counter(itsComposite, SWT.NONE, 3, itsGame
				.getBombCount(), null);
		itsCounter.setLayoutData(GridDataFactory.fillDefaults().align(
				SWT.BEGINNING, SWT.CENTER).grab(true, false).create());
		itsTimerCounter = new Counter(itsComposite, SWT.NONE, 3, 0, null);
		itsTimerCounter.setLayoutData(GridDataFactory.fillDefaults().align(
				SWT.CENTER, SWT.END).create());
		itsGameCanvas = new GameCanvas(itsComposite, SWT.NONE, itsGame);
		itsGameCanvas.setLayoutData(GridDataFactory.fillDefaults().span(2, 1)
				.create());
		itsAutoPlayer = new AutoPlayer(itsGameCanvas.getGame(), true);
		itsGameCanvas.getDisplay().timerExec(1000, new Runnable() {

			public void run() {
				itsAutoPlayer.doNextMove();
			}
		});
	}

	public void dispose() {
	}

	public void disposeControl() {
		stopTimer();
		itsGame.removeListener(itsMinesGameListener);
		itsRootComposite.dispose();
	}

	public void setFocus() {
		itsRootComposite.setFocus();
	}

	private void startTimer() {
		itsTimer = new Timer();
		final Display display = Display.getCurrent();
		itsTimer.schedule(new TimerTask() {

			public void run() {
				display.asyncExec(new Runnable() {

					public void run() {
						if (!itsTimerCounter.isDisposed()) {
							itsTimerCounter.increase();
							itsAutoPlayer.doNextMove();
						}
					}
				});
			}
		}, 1000, 1000);
	}

	private void stopTimer() {
		if (itsTimer != null) {
			itsTimer.cancel();
		}
	}

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
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
