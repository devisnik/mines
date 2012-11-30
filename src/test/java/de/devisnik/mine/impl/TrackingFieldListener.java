package de.devisnik.mine.impl;

import de.devisnik.mine.IField;
import de.devisnik.mine.IFieldListener;

public class TrackingFieldListener implements IFieldListener {

	private int openChange;
	private int flagChange;
	private int explodedChange;
	private int touchChange;

	public int getOpenChange() {
		return openChange;
	}

	public int getFlagChange() {
		return flagChange;
	}

	public int getExplodedChange() {
		return explodedChange;
	}

	public int getTouchedChange() {
		return touchChange;
	}
	
	public void onFieldExplodedChange(IField field, boolean value) {
		explodedChange++;
	}

	public void onFieldFlagChange(IField field, boolean value) {
		flagChange++;
	}

	public void onFieldOpenChange(IField field, boolean value) {
		openChange++;
	}

	public void onFieldTouchedChange(IField field, boolean value) {
		touchChange++;
	}

}
