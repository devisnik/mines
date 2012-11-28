package de.devisnik.mine;

/**
 * @since 1.0
 */
public interface IFieldListener {
	void onFieldOpenChange(IField field, boolean value);
	void onFieldFlagChange(IField field, boolean value);
	void onFieldExplodedChange(IField field, boolean value);
	void onFieldTouchedChange(IField field, boolean value);
}
