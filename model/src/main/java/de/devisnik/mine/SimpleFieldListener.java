package de.devisnik.mine;

/**
 * @since 1.0
 */
public abstract class SimpleFieldListener implements IFieldListener {

	public void onFieldExplodedChange(IField field, boolean value) {
		onChange(field);
	}

	public void onFieldFlagChange(IField field, boolean value) {
		onChange(field);
	}

	public void onFieldOpenChange(IField field, boolean value) {
		onChange(field);
	}

	public void onFieldTouchedChange(IField field, boolean value) {
		onChange(field);
	}

	protected abstract void onChange(IField field);

}
