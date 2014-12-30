package de.devisnik.mine.robot;

import java.util.ArrayList;
import java.util.List;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.Point;

public class FieldResolver {
	private final IField itsField;
	private final IGame itsGame;
	private final List<IField> itsNeighbors;
	private List<IConfiguration> itsPossibleResolutions;
	private IConfiguration itsSaveSolution;

	public FieldResolver(IField field, IGame game) {
		this.itsField = field;
		this.itsGame = game;
		itsNeighbors = getFieldNeighbors();
	}

	public IField getField() {
		return itsField;
	}

	public List<IConfiguration> getPossibleResolutions() {
		if (itsPossibleResolutions != null)
			return itsPossibleResolutions;
		itsPossibleResolutions = new ArrayList<IConfiguration>();
		final int countClosedUnflaggedNeighbors = countClosedUnflaggedNeighbors();
		final int countNeighborsToFlag = countNeighborsToFlag();
		IConfiguration configuration = ConfigurationFactory.create(
				countClosedUnflaggedNeighbors, countNeighborsToFlag);
		while (configuration != null) {
			if (configuration.test(itsGame, itsField))
				itsPossibleResolutions.add(configuration);
			configuration = configuration.next();
		}
		return itsPossibleResolutions;
	}

	public IConfiguration getUniqueResolution() {
		if (itsSaveSolution == null)
			itsSaveSolution = getIntersection(getPossibleResolutions());
		return itsSaveSolution;
	}

	public IMove[] applyUniqueResolution() {
		if (getUniqueResolution() == null)
			return new IMove[0];
		return getUniqueResolution().apply(itsGame, itsField);
	}
	
	public IMove[] applyFirstPossibleResolution() {
		if (getPossibleResolutions().isEmpty())
			return new IMove[0];
		IConfiguration configuration = getPossibleResolutions().get(0);
		return configuration.apply(itsGame, itsField);
	}

	private IConfiguration getIntersection(final List<IConfiguration> resolvers) {
		if (resolvers.size() == 0)
			return null;
		IConfiguration intersection = resolvers.get(0);
		for (int i = 1; i < resolvers.size(); i++) {
			final IConfiguration configuration = resolvers
					.get(i);
			intersection = intersection.getIntersection(configuration);
			if (intersection == null)
				return null;
		}
		// MineActivator.getDefault().log(
		// "intersection of resolvers: " + resolvers.size());
		return intersection;
	}

	private int countClosedUnflaggedNeighbors() {
		int count = 0;
		for (IField neighbor : itsNeighbors)
			if (!neighbor.isOpen() && !neighbor.isFlagged())
				count++;
		return count;
	}

	private int countNeighborsToFlag() {
		return itsField.getNeighborBombs() - countFlaggedNeighbors();
	}

	private int countFlaggedNeighbors() {
		int count = 0;
		for (IField neighbor : itsNeighbors)
			if (neighbor.isFlagged())
				count++;
		return count;
	}

	private List<IField> getFieldNeighbors() {
		ArrayList<IField> neighbors = new ArrayList<IField>();
		IBoard board = itsGame.getBoard();
		Point dimension = board.getDimension();
		Point position = board.getPosition(itsField);
		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
				if ((x != 0 || y != 0) && position.x + x >= 0 && position.x + x < dimension.x && position.y + y >= 0
						&& position.y + y < dimension.y)
					neighbors.add(board.getField(position.x + x, position.y + y));
		return neighbors;
	}
}
