package de.devisnik.mine;

/**
 * @since 1.0
 */
public final class Point {
	public int x;
	public int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int hashCode() {
		return x + y;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point other = (Point) obj;
			return other.x == x && other.y == y;
		}
		return false;
	}
}
