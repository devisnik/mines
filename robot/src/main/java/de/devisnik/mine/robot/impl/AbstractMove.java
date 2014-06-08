package de.devisnik.mine.robot.impl;

import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;
import de.devisnik.mine.robot.IMove;

public abstract class AbstractMove implements IMove {

	protected final IGame game;
	protected final IField field;

	public AbstractMove(IGame game, IField field) {
		this.game = game;
		this.field = field;
	}	
}
