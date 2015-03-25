
package de.devisnik.web.client.robot.impl;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.impl.Field;
import de.devisnik.web.client.robot.FieldUtil;
import de.devisnik.web.client.robot.IConfiguration;
import de.devisnik.web.client.robot.IMove;

import java.util.ArrayList;
import java.util.List;

public class Configuration implements IConfiguration {


	private static int[] createDefaultMapping(final int length) {
		final int[] mapping = new int[length];
		for (int i = 0; i < mapping.length; i++)
			mapping[i] = i;
		return mapping;
	}

	private static List<Element> createElementList(final boolean[] vector,
			final int[] indices) {
		final ArrayList<Element> list = new ArrayList<Element>();
		for (int i = 0; i < vector.length; i++)
			list.add(new Element(vector[i], indices[i]));
		return list;
	}

	public static void main(final String[] args) {
		final IConfiguration configuration = new Configuration(new boolean[] {
				true, true, true, false, false });
		IConfiguration loopConfiguration = configuration;
		while (loopConfiguration != null) {
			System.out.println(loopConfiguration);
			loopConfiguration = loopConfiguration.next();
		}

		final Configuration anotherConfiguration = new Configuration(
				new boolean[] { false, false, true });
		final IConfiguration intersection = configuration
				.getIntersection(anotherConfiguration);
//		System.out.println("Intersection of " + configuration + " and "
//				+ anotherConfiguration);
//		System.out.println(intersection);

		final Configuration third = new Configuration(new boolean[] { false,
				true, false });
//		System.out.println(intersection.getIntersection(third));
	}

	private final List<Element> elements;

	public Configuration(final boolean[] vector) {
		this(vector, createDefaultMapping(vector.length));
	}

	public Configuration(final boolean[] vector, final int[] fieldIndices) {
		this(createElementList(vector, fieldIndices));

		// this.vector = vector;
		// this.fieldIndices = fieldIndices;
		// Assert.isNotNull(vector);
	}

	private Configuration(final List<Element> elementList) {
		elements = elementList;
	}

	@Override
	public IMove[] apply(final IGame game, final IField field) {
		final List<IField> unflaggedNeighbors = getClosedUnflaggedNeighbors(field, game.getBoard());
		final IMove[] moves = new IMove[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			final Element element = elements.get(i);
			final IField neighbor = unflaggedNeighbors.get(element
					.getPosition());
			if (element.isFlag())
				moves[i] = new FlagMove(game, neighbor);
			else
				moves[i] = new OpenMove(game, neighbor);
		}

		return moves;
	}

	private int countEndingTrueEntries(final boolean[] copy) {
		int count = 0;
		for (int i = copy.length - 1; i >= 0; i--) {
			if (!copy[i])
				return count;
			count++;
		}
		return 0;
	}

	private List<IField> getClosedUnflaggedNeighbors(final IField field, IBoard board) {
		final List<IField> neighbors = FieldUtil.getNeighbors(field, board);
		final List<IField> closedNeighbors = new ArrayList<IField>();
		for (IField neighbor : neighbors)
			if (!neighbor.isOpen() && !neighbor.isFlagged())
				closedNeighbors.add(neighbor);
		return closedNeighbors;
	}

	private boolean[] getFlagVector() {
		final boolean[] vector = new boolean[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			final Element element = elements.get(i);
			vector[i] = element.isFlag();
		}
		return vector;
	}

	@Override
	public IConfiguration getIntersection(final IConfiguration configuration) {
		if (!(configuration instanceof Configuration))
			return null;
		final Configuration confImpl = (Configuration) configuration;
		final List<Element> intersection = new ArrayList<Element>();
		for (int i = 0; i < elements.size(); i++) {
			final Element element = elements.get(i);
			if (confImpl.elements.contains(element))
				intersection.add(element);
		}
		final int size = intersection.size();
		if (size == 0)
			return null;
		return new Configuration(intersection);
	}

	private int[] getPositionVector() {
		final int[] vector = new int[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			final Element element = elements.get(i);
			vector[i] = element.getPosition();
		}
		return vector;
	}

	@Override
	public IConfiguration next() {
		final boolean[] vector = getFlagVector();
		final int[] positions = getPositionVector();
		final int length = vector.length;
		final boolean[] copy = new boolean[length];
		System.arraycopy(vector, 0, copy, 0, length);
		final int toCut = countEndingTrueEntries(copy);
		if (shiftArray(copy, length - toCut, toCut))
			return new Configuration(copy, positions);
		return null;
	}

	private void setFlagState(final List<IField> unflaggedNeighbors, final boolean state) {
		for (int i = 0; i < elements.size(); i++) {
			final Element element = elements.get(i);
			if (element.isFlag()) {
				final Field neighbor = (Field) unflaggedNeighbors.get(element
						.getPosition());
				neighbor.setFlagged(state);
			}
		}
	}

	private boolean shiftArray(final boolean[] array, final int length,
			final int fills) {
		int index = 0;
		for (index = length - 1; index >= 0; index--)
			if (array[index])
				break;
		if (index == length - 1 || index == -1)
			return false;
		array[index + 1] = true;
		array[index] = false;
		for (int i = 0; i < fills; i++)
			array[index + 2 + i] = true;
		for (int i = index + 2 + fills; i < array.length; i++)
			array[i] = false;
		return true;
	}

	@Override
	public boolean test(final IGame game, final IField field) {
		final List<IField> unflaggedNeighbors = getClosedUnflaggedNeighbors(field, game.getBoard());
		setFlagState(unflaggedNeighbors, true);
		final boolean overflagged = game.getBoard().isOverflagged();
		setFlagState(unflaggedNeighbors, false);
		return !overflagged;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < elements.size(); i++) {
			final Element element = elements.get(i);
			buffer.append(element.isFlag() ? "1" : "0");
			buffer.append("[");
			buffer.append(element.getPosition());
			buffer.append("]");
		}
		return buffer.toString();
	}

	@Override
	public Element[] getElements() {
		return elements.toArray(new Element[elements.size()]);
	}

	@Override
	public int getElementsCount() {
		return elements.size();
	}

}
