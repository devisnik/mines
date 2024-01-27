package de.devisnik.android.mine;

import android.app.Activity;
import android.os.Bundle;

public class Preview extends Activity {

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = new Settings(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme(settings.getTheme());
        setContentView(R.layout.main);
        BoardView boardView = (BoardView) findViewById(R.id.board);
        boardView.setSize(4, 4);
        boardView.setFieldSizeAndTouchFocus(-1);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                boardView.getField(i, j).setImageId(4 * i + j);
    }
}
