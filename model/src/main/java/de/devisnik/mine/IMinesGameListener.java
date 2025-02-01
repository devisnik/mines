package de.devisnik.mine;

/**
 * @since 1.0
 */
public interface IMinesGameListener {

    public void onDisarmed();

    public void onBusted();

    public void onChange(int flags, int mines);

    public void onStart();

    public void onClickAfterFinished();
}
