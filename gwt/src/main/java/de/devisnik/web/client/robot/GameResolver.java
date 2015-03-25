package de.devisnik.web.client.robot;

import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.Point;
import de.devisnik.web.client.robot.impl.OpenMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class GameResolver {
	
	private final IGame itsGame;
	private final Stack<IMove> itsCommandStack = new Stack<IMove>();
	private FieldResolver itsMinimalResolver = null;

	public GameResolver(IGame game) {
		this.itsGame = game;
	}
	/**
	 * Searches for a uniquely resolvable field, updating minResolver to be the resolver
	 * with minimal number of possible resolutions.
	 * @return <code>true</code> iff found and applied unique resolution
	 */
	public boolean findUniqueResolution() {
		final List<IField> openFields = getOpenUnfinishedFields();
		for (IField field : openFields) {
			FieldResolver fieldResolver = new FieldResolver(field, itsGame);
			applyUniqueResolution(fieldResolver);
			if (!isEmptyCommandStack())
				return true;
			if (itsMinimalResolver == null
					|| itsMinimalResolver.getPossibleResolutions().size() > fieldResolver
							.getPossibleResolutions().size())
				itsMinimalResolver = fieldResolver;
		}
		return false;
	}

	public Stack<IMove> getCommandStack() {
		return itsCommandStack;
	}

	private void applyUniqueResolution(FieldResolver resolver) {
		final IMove[] moves = resolver.applyUniqueResolution();
		itsCommandStack.addAll(Arrays.asList(moves));
	}

	public boolean isEmptyCommandStack() {
		return itsCommandStack.isEmpty();
	}

	public boolean applyFirstResolution() {
		if (itsMinimalResolver != null) {
			applyFirstPossibleResolution(itsMinimalResolver);
			return !isEmptyCommandStack();
		}
		return false;
	}

	public void openFirstClosedUnflaggedField() {
		final IField closedUnflaggedField = getFirstClosedUnflaggedField();
		if (closedUnflaggedField != null)
			itsCommandStack.push(new OpenMove(itsGame, closedUnflaggedField));
	}


	private void applyFirstPossibleResolution(FieldResolver resolver) {
		final IMove[] moves = resolver.applyFirstPossibleResolution();
		if (moves.length > 0)
			itsCommandStack.add(moves[0]);
	}


	private IField getFirstClosedUnflaggedField() {
		final Point gameDimension = itsGame.getBoard().getDimension();
		for (int i = 0; i < gameDimension.y; i++)
			for (int j = 0; j < gameDimension.x; j++) {
				final IField field = itsGame.getBoard().getField(j, i);
				if (!field.isOpen() && !field.isFlagged())
					return field;
			}
		return null;
	}



	private List<IField> getOpenUnfinishedFields() {
		final List<IField> openFields = new ArrayList<IField>();
		final Point gameDimension = itsGame.getBoard().getDimension();
		for (int i = 0; i < gameDimension.y; i++)
			for (int j = 0; j < gameDimension.x; j++) {
				final IField field = itsGame.getBoard().getField(j, i);
				if (field.isOpen() && field.getNeighborBombs() > 0
						&& countClosedUnflaggedNeighbors(field) > 0)
					openFields.add(field);
			}
		return openFields;
	}
	
	private int countClosedUnflaggedNeighbors(final IField field) {
		final List<IField> neighbors = FieldUtil.getNeighbors(field, itsGame.getBoard());
		int count = 0;
		for (IField neighbor : neighbors)
			if (!neighbor.isOpen() && !neighbor.isFlagged())
				count++;
		return count;
	}

}
