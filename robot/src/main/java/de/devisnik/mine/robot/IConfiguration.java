package de.devisnik.mine.robot;

import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;

public interface IConfiguration {

	 final class Element {
		private final boolean flag;
		private final int position;

		public Element(final boolean flag, final int position) {
			this.flag = flag;
			this.position = position;
		}

		public boolean equals(final Object obj) {
			if (!(obj instanceof Element)) {
				return false;
			}
			final Element other = (Element) obj;
			return flag == other.flag && position == other.position;
		}

		public int getPosition() {
			return position;
		}

		public int hashCode() {
			return position;
		}

		public boolean isFlag() {
			return flag;
		}
	}

	public abstract IConfiguration next();

	public boolean test(final IGame game, final IField field);

	public IMove[] apply(final IGame game, final IField field);

	public IConfiguration getIntersection(IConfiguration configuration);
	
	Element[] getElements();

	int getElementsCount();
}