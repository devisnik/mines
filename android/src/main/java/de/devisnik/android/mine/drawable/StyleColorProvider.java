package de.devisnik.android.mine.drawable;

import java.util.HashMap;
import java.util.Map;

import android.content.res.TypedArray;
import de.devisnik.android.mine.R;

public class StyleColorProvider {

	enum ColorId {
		NO_1,
		NO_2,
		NO_3,
		NO_4,
		NO_5,
		NO_6,
		NO_7,
		NO_8,
		ERROR,
		FLAG_LIGHT,
		FLAG_DARK,
		FLAGPOLE,
		BOMB_BODY,
		BOMB_SPIKES,
		BUTTON_UP_LEFT,
		BUTTON_LOW_RIGHT,
		FOCUS,
		TOUCH,
	}

	Map<ColorId, Integer> itsColorMap = new HashMap<ColorId, Integer>();
	private final TypedArray itsArray;

	public StyleColorProvider(TypedArray array) {
		itsArray = array;
		map(ColorId.BOMB_BODY, R.styleable.BoardPanel_BOMB_COLOR_BODY, R.color.BOMB_COLOR_BODY);
		map(ColorId.BOMB_SPIKES, R.styleable.BoardPanel_BOMB_COLOR_SPIKES, R.color.BOMB_COLOR_SPIKES);
		map(ColorId.BUTTON_UP_LEFT, R.styleable.BoardPanel_BUTTON_COLOR_UP_LEFT,
				R.color.BUTTON_COLOR_UP_LEFT);
		map(ColorId.BUTTON_LOW_RIGHT, R.styleable.BoardPanel_BUTTON_COLOR_LOW_RIGHT,
				R.color.BUTTON_COLOR_LOW_RIGHT);
		map(ColorId.ERROR, R.styleable.BoardPanel_ERROR_COLOR, R.color.ERROR_COLOR);
		map(ColorId.FLAG_LIGHT, R.styleable.BoardPanel_FLAG_COLOR_LIGHT, R.color.FLAG_COLOR);
		map(ColorId.FLAG_DARK, R.styleable.BoardPanel_FLAG_COLOR_DARK, R.color.FLAG_COLOR);
		map(ColorId.FLAGPOLE, R.styleable.BoardPanel_FLAGPOLE_COLOR, R.color.FLAGPOLE_COLOR);
		map(ColorId.FOCUS, R.styleable.BoardPanel_FOCUS_COLOR, R.color.FOCUS_COLOR);
		map(ColorId.TOUCH, R.styleable.BoardPanel_TOUCH_COLOR, R.color.TOUCH_COLOR);
		map(ColorId.NO_1, R.styleable.BoardPanel_NO_1_COLOR, R.color.NO_1_COLOR);
		map(ColorId.NO_2, R.styleable.BoardPanel_NO_2_COLOR, R.color.NO_2_COLOR);
		map(ColorId.NO_3, R.styleable.BoardPanel_NO_3_COLOR, R.color.NO_3_COLOR);
		map(ColorId.NO_4, R.styleable.BoardPanel_NO_4_COLOR, R.color.NO_4_COLOR);
		map(ColorId.NO_5, R.styleable.BoardPanel_NO_5_COLOR, R.color.NO_5_COLOR);
		map(ColorId.NO_6, R.styleable.BoardPanel_NO_6_COLOR, R.color.NO_6_COLOR);
		map(ColorId.NO_7, R.styleable.BoardPanel_NO_7_COLOR, R.color.NO_7_COLOR);
		map(ColorId.NO_8, R.styleable.BoardPanel_NO_8_COLOR, R.color.NO_8_COLOR);
	}

	@SuppressWarnings("boxing")
	public int getValue(ColorId id) {
		return itsColorMap.get(id);
	}

	@SuppressWarnings("boxing")
	private void map(ColorId id, int styleableId, int defaultColor) {
		itsColorMap.put(id, itsArray.getColor(styleableId, itsArray.getResources().getColor(defaultColor)));
	}
}
