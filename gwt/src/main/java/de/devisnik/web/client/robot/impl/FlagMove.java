package de.devisnik.web.client.robot.impl;

import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;

public class FlagMove extends AbstractMove {

	public FlagMove(IGame game, IField field) {
		super(game, field);
	}
	
	public boolean execute() {
		if (field.isFlagged() || field.isOpen()) {
			return false;
		}
		game.onRequestFlag(field);
		return true;
	}

}
