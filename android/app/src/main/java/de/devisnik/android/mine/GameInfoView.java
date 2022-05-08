package de.devisnik.android.mine;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class GameInfoView extends TextView {

	public GameInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
        setSelected(true);
	}
}
