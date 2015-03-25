package de.devisnik.web.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.Point;

public class BoardCanvas extends Composite {

	private final IBoard itsBoard;
	private AbsolutePanel itsPanel;
	private final ClickHandler itsClickListener;


	public BoardCanvas(final IBoard board, ClickHandler clickListener) {
		super();
		itsBoard = board;
		itsClickListener = clickListener;
		itsPanel = new AbsolutePanel();
		initWidget(itsPanel);
		setSize(Integer.toString(15*itsBoard.getDimension().x), Integer.toString(15*itsBoard.getDimension().y));
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
				final FieldCanvas fieldCanvas = new FieldCanvas(itsBoard
						.getField(j,i), j, i);
				itsPanel.add(fieldCanvas, 15 * j, 15 * i);
				fieldCanvas.addClickHandler(itsClickListener);
			}
		}
	}

	public void dispose() {
		for (Widget child : itsPanel) {
			FieldCanvas fieldCanvas = (FieldCanvas)child;
			fieldCanvas.dispose();
		}
	}
}
