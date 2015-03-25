package de.devisnik.web.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import de.devisnik.mine.IBoard;
import de.devisnik.mine.Point;

public class BoardCanvas extends Composite {

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
        setSize(boardSize.x*FieldCanvas.SIZE + "px", boardSize.y*FieldCanvas.SIZE + "px");
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
                itsPanel.setWidgetLeftWidth(fieldCanvas, FieldCanvas.SIZE * j, Style.Unit.PX, FieldCanvas.SIZE, Style.Unit.PX);
                itsPanel.setWidgetTopHeight(fieldCanvas, FieldCanvas.SIZE * i, Style.Unit.PX, FieldCanvas.SIZE, Style.Unit.PX);
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
