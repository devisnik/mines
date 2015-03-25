package de.devisnik.web.client.robot.impl;

import de.devisnik.mine.IField;
import de.devisnik.mine.IGame;

public class OpenMove extends AbstractMove {

	public OpenMove(IGame game, IField field) {
		super(game, field);
	}
	
	public boolean execute() {
		if (field.isOpen()) {
			return false;
		}
		game.onRequestOpen(field);
		return true;
	}

}
