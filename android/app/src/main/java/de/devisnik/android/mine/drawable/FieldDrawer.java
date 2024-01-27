package de.devisnik.android.mine.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;

import de.devisnik.android.mine.drawable.StyleColorProvider.ColorId;

public class FieldDrawer extends CachingDrawer {

    private static Drawable createLayer(final Drawable... drawables) {
        return new LayerDrawable(drawables);
    }

    private final StyleColorProvider colorProvider;

    public FieldDrawer(final StyleColorProvider colorProvider, final Typeface face) {
        super(10, 10);
        this.colorProvider = colorProvider;
        initDrawables(face);
    }

    private int getColor(final ColorId id) {
        return colorProvider.getValue(id);
    }

    @Override
    public void drawFocus(final Canvas canvas) {
        draw(14, canvas);
    }

    private void initDrawables(final Typeface face) {
        GradientDrawable button_background = new GradientDrawable(Orientation.TL_BR, new int[]{
                getColor(ColorId.BUTTON_UP_LEFT), getColor(ColorId.BUTTON_LOW_RIGHT)});
        BombDrawable bomb = new BombDrawable(getColor(ColorId.BOMB_BODY), getColor(ColorId.BOMB_SPIKES));
        Drawable button = createLayer(button_background, new ShadowDrawable());
        FlagDrawable flag = new FlagDrawable(getColor(ColorId.FLAG_LIGHT), getColor(ColorId.FLAG_DARK),
                getColor(ColorId.FLAGPOLE));
        Drawable focus = new ColorDrawable(getColor(ColorId.FOCUS));
        Drawable touched = new TouchDrawable(getColor(ColorId.TOUCH));

        register(0, new ColorDrawable(Color.TRANSPARENT));
        register(1, createNumber(1, face, ColorId.NO_1));
        register(2, createNumber(2, face, ColorId.NO_2));
        register(3, createNumber(3, face, ColorId.NO_3));
        register(4, createNumber(4, face, ColorId.NO_4));
        register(5, createNumber(5, face, ColorId.NO_5));
        register(6, createNumber(6, face, ColorId.NO_6));
        register(7, createNumber(7, face, ColorId.NO_7));
        register(8, createNumber(8, face, ColorId.NO_8));
        register(9, createLayer(button, bomb));
        register(10, button);
        register(11, createLayer(button, flag));
        register(12, createLayer(button, flag, new CrossDrawable(getColor(ColorId.ERROR))));
        register(13, createLayer(new ColorDrawable(getColor(ColorId.ERROR)), bomb));
        register(14, focus);
        register(15, touched);
    }

    private NumberDrawable createNumber(final int number, final Typeface face, final ColorId colorId) {
        return new NumberDrawable(number, getColor(colorId), face);
    }

    @Override
    public void drawTouched(final Canvas canvas) {
        draw(15, canvas);
    }
}
