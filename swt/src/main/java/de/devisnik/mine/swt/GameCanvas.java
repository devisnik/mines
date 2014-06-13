package de.devisnik.mine.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.devisnik.mine.GameFactory;
import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IMinesGameListener;
import de.devisnik.mine.robot.AutoPlayer;

public class GameCanvas extends Composite {

    private final IGame itsGame;

    public GameCanvas(final Composite parent, final int style,
                      final IGame minesGame) {
        super(parent, SWT.NONE);
        itsGame = minesGame;
        setLayout(createLayout(itsGame.getBoard().getDimension().x));
        initFields();
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

    private void initFields() {
        final de.devisnik.mine.Point gameDimension = itsGame.getBoard()
                .getDimension();
        final MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                FieldCanvas fieldCanvas = (FieldCanvas) event.getSource();
                IField field = (IField) fieldCanvas.getData();
                if (event.button == 1) {
                    itsGame.onRequestOpen(field);
                } else if (event.button == 3) {
                    itsGame.onRequestFlag(field);
                }
            }
        };
        for (int i = 0; i < gameDimension.y; i++) {
            for (int j = 0; j < gameDimension.x; j++) {
                IField field = itsGame.getBoard().getField(j, i);
                final FieldCanvas fieldCanvas = new FieldCanvas(this, field);
                fieldCanvas.setData(field);
                fieldCanvas.addMouseListener(mouseAdapter);
            }
        }
    }

    public IGame getGame() {
        return itsGame;
    }

    public static void main(final String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        final IGame game = GameFactory.create(10, 10, 20);
        final AutoPlayer autoPlayer = new AutoPlayer(game, false);
        final boolean[] finished = new boolean[1];
        final GameCanvas gameCanvas = new GameCanvas(shell, SWT.NONE, game);
        Display.getDefault().timerExec(1000, new Runnable() {
            public void run() {
                autoPlayer.doNextMove();
                if (!finished[0]) {
                    Display.getDefault().timerExec(1000, this);
                }
            }
        });

        game.addListener(new IMinesGameListener() {

            public void onBusted() {
                exit();
            }

            public void onChange(final int flags, final int mines) {
            }

            public void onDisarmed() {
                exit();
            }

            private void exit() {
                finished[0] = true;
                shell.getDisplay().timerExec(2000, new Runnable() {
                    public void run() {
                        System.exit(0);
                    }
                });
            }

            public void onStart() {
            }
        });
        Button cloneButton = new Button(shell, SWT.PUSH);
        cloneButton.setText("Clone");
        cloneButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell subShell = new Shell(display);
                subShell.setLayout(new FillLayout());
                new GameCanvas(subShell, SWT.NONE, game);
                subShell.pack();
                subShell.open();
            }
        });
        Button snapshotButton = new Button(shell, SWT.PUSH);
        snapshotButton.setText("Snapshot");
        snapshotButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell subShell = new Shell(display);
                subShell.setLayout(new FillLayout());
                new GameCanvas(subShell, SWT.NONE, GameFactory.clone(game));
                subShell.pack();
                subShell.open();
            }
        });
        shell.setLayout(new RowLayout());
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
