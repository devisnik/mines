package de.devisnik.mine.robot;

import java.util.ArrayList;
import java.util.List;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class FieldUtil {

	private FieldUtil() {
	}

	public static List<IField> getNeighbors(IField field, IBoard board) {
		ArrayList<IField> neighbors = new ArrayList<IField>();
		Point dimension = board.getDimension();
		Point position = board.getPosition(field);
		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
				if ((x != 0 || y != 0) && position.x + x >= 0 && position.x + x < dimension.x && position.y + y >= 0
						&& position.y + y < dimension.y)
					neighbors.add(board.getField(position.x + x, position.y + y));
		return neighbors;
	}

}
