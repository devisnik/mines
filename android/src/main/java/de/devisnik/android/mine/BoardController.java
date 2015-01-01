package de.devisnik.android.mine;

import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.Point;
import de.devisnik.mine.SimpleFieldListener;

public class BoardController {

	class FieldListener extends SimpleFieldListener {
		@Override
		protected void onChange(IField field) {
			getFieldController(field).updateView();
		}
	}

	class FieldViewListener implements OnClickListener, OnLongClickListener {
		@Override
		public void onClick(final View v) {
			FieldController fieldController = getFieldController(v);
			itsGame.onRequestFlag(fieldController.getField());
			fieldController.showClickFeedback();
		}

		@Override
		public boolean onLongClick(final View v) {
			final Vibrator vibrator = itsSettings.getVibrator();
			if (vibrator != null)
				vibrator.vibrate(new long[] { 1, 50 }, -1);
			FieldController fieldController = getFieldController(v);
			itsGame.onRequestOpen(fieldController.getField());
			return true;
		}
	}

	private final BoardView itsBoardView;
	private final IBoard itsBoard;
	private final IGame itsGame;
	private final Settings itsSettings;

	public BoardController(final IGame game, final BoardView boardView, final Settings settings) {
		itsGame = game;
		itsBoardView = boardView;
		itsSettings = settings;
		itsBoard = game.getBoard();
		initBoardView();
		initFieldControllers();
	}

	private void initBoardView() {
		final Point dimension = itsBoard.getDimension();
		itsBoardView.setSize(dimension.x, dimension.y);
		itsBoardView.setFieldSizeAndTouchFocus(itsSettings.getFieldSize(), itsSettings.isTouchFocus());
	}

	public final void onZoomChange() {
		itsBoardView.setFieldSizeAndTouchFocus(itsSettings.getFieldSize(), itsSettings.isTouchFocus());
	}

	public void dispose() {
		Point dimension = itsBoard.getDimension();
		for (int x = 0; x < dimension.x; x++)
			for (int y = 0; y < dimension.y; y++)
				getFieldController(itsBoardView.getField(x, y)).dispose();
	}

	private FieldController getFieldController(final IField field) {
		final Point fieldPosition = itsBoard.getPosition(field);
		return getFieldController(itsBoardView.getField(fieldPosition.x, fieldPosition.y));
	}

	private FieldController getFieldController(final View view) {
		return (FieldController) view.getTag();
	}

	private void initFieldControllers() {
		final Point dimension = itsBoard.getDimension();
		FieldViewListener clickListener = new FieldViewListener();
		FieldListener fieldListener = new FieldListener();
		for (int x = 0; x < dimension.x; x++)
			for (int y = 0; y < dimension.y; y++)
				createFieldController(x, y, clickListener, fieldListener);
	}

	private void createFieldController(int x, int y, FieldViewListener clickListener, FieldListener fieldListener) {
		final FieldView fieldView = itsBoardView.getField(x, y);
		final FieldController fieldController = new FieldController(fieldView, itsBoard.getField(x, y), itsSettings,
				clickListener, fieldListener);
		fieldView.setTag(fieldController);
	}

	public boolean isBoardFullyVisibleForFieldSize(int fieldSize) {
		int width = itsBoard.getDimension().x * fieldSize;
		int height = itsBoard.getDimension().y * fieldSize;
		return itsBoardView.fitsIntoArea(width, height);
	}
}
