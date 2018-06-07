package de.devisnik.android.mine.drawable;

import android.content.res.TypedArray;

import java.util.HashMap;
import java.util.Map;

import de.devisnik.android.mine.base.R;

public final class StyleColorProvider {

    public enum ColorId {
        NO_1(R.styleable.BoardPanel_NO_1_COLOR, R.color.NO_1_COLOR),
        NO_2(R.styleable.BoardPanel_NO_2_COLOR, R.color.NO_2_COLOR),
        NO_3(R.styleable.BoardPanel_NO_3_COLOR, R.color.NO_3_COLOR),
        NO_4(R.styleable.BoardPanel_NO_4_COLOR, R.color.NO_4_COLOR),
        NO_5(R.styleable.BoardPanel_NO_5_COLOR, R.color.NO_5_COLOR),
        NO_6(R.styleable.BoardPanel_NO_6_COLOR, R.color.NO_6_COLOR),
        NO_7(R.styleable.BoardPanel_NO_7_COLOR, R.color.NO_7_COLOR),
        NO_8(R.styleable.BoardPanel_NO_8_COLOR, R.color.NO_8_COLOR),
        ERROR(R.styleable.BoardPanel_ERROR_COLOR, R.color.ERROR_COLOR),
        FLAG_LIGHT(R.styleable.BoardPanel_FLAG_COLOR_LIGHT, R.color.FLAG_COLOR),
        FLAG_DARK(R.styleable.BoardPanel_FLAG_COLOR_DARK, R.color.FLAG_COLOR),
        FLAGPOLE(R.styleable.BoardPanel_FLAGPOLE_COLOR, R.color.FLAGPOLE_COLOR),
        BOMB_BODY(R.styleable.BoardPanel_BOMB_COLOR_BODY, R.color.BOMB_COLOR_BODY),
        BOMB_SPIKES(R.styleable.BoardPanel_BOMB_COLOR_SPIKES, R.color.BOMB_COLOR_SPIKES),
        BUTTON_UP_LEFT(R.styleable.BoardPanel_BUTTON_COLOR_UP_LEFT, R.color.BUTTON_COLOR_UP_LEFT),
        BUTTON_LOW_RIGHT(R.styleable.BoardPanel_BUTTON_COLOR_LOW_RIGHT,
                R.color.BUTTON_COLOR_LOW_RIGHT),
        FOCUS(R.styleable.BoardPanel_FOCUS_COLOR, R.color.FOCUS_COLOR),
        TOUCH(R.styleable.BoardPanel_TOUCH_COLOR, R.color.TOUCH_COLOR);

        private final int styleColor;
        private final int defaultColor;

        ColorId(int styleColor, int defaultColor) {
            this.styleColor = styleColor;
            this.defaultColor = defaultColor;
        }
    }

    private final Map<ColorId, Integer> colorMap = new HashMap<>();

    public StyleColorProvider(TypedArray array) {
        for (ColorId colorId : ColorId.values()) {
            map(colorId, array, colorMap);
        }
    }

    @SuppressWarnings("boxing")
    public int getValue(ColorId id) {
        return colorMap.get(id);
    }

    @SuppressWarnings("boxing")
    private static void map(ColorId id, TypedArray typedArray, Map<ColorId, Integer> colorMap) {
        colorMap.put(id, typedArray.getColor(id.styleColor, typedArray.getResources().getColor(id.defaultColor)));
    }
}
