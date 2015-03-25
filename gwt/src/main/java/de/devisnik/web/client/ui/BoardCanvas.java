package de.devisnik.web.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.*;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.Point;

public class BoardCanvas extends Composite {

    private static final int SIZE = 15;
    private final IBoard itsBoard;
    private LayoutPanel itsPanel;
    private final ClickHandler itsClickListener;


    public BoardCanvas(final IBoard board, ClickHandler clickListener) {
        super();
        itsBoard = board;
        itsClickListener = clickListener;
        itsPanel = new LayoutPanel();
        Point boardSize = itsBoard.getDimension();
        initWidget(itsPanel);
        setSize(Integer.toString(SIZE * boardSize.x), Integer.toString(SIZE * boardSize.y));
    }

    protected void onLoad() {
        super.onLoad();
        Scheduler.get().scheduleDeferred(new Command() {
            public void execute() {
                initFields();
            }
        });
    }

    private void initFields() {
        final Point gameDimension = itsBoard.getDimension();
        for (int i = 0; i < gameDimension.y; i++) {
            for (int j = 0; j < gameDimension.x; j++) {
                final FieldCanvas fieldCanvas = new FieldCanvas(itsBoard.getField(j, i), j, i);
                itsPanel.add(fieldCanvas);
                itsPanel.setWidgetLeftWidth(fieldCanvas, SIZE * j, Style.Unit.PX, SIZE, Style.Unit.PX);
                itsPanel.setWidgetTopHeight(fieldCanvas, SIZE * i, Style.Unit.PX, SIZE, Style.Unit.PX);
                fieldCanvas.addClickHandler(itsClickListener);
            }
        }
    }

    public void dispose() {
        for (Widget child : itsPanel) {
            FieldCanvas fieldCanvas = (FieldCanvas) child;
            fieldCanvas.dispose();
        }
    }
}
