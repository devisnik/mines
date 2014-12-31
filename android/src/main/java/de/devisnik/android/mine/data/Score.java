/**
 * 
 */
package de.devisnik.android.mine.data;

import java.io.Serializable;

public final class Score implements Serializable {
	private static final long serialVersionUID = -4570229603597268521L;

	public long id;
	public String name;
	public String level;
	public String board;
	public int time;
	public long date;
	public int rank;
}
