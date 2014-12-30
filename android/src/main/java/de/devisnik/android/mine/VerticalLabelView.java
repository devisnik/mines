/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.devisnik.android.mine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Simple class that draws a string vertically. It is much simpler than
 * TextView as the user can only set the text, text size and text color.
 */
public class VerticalLabelView extends View {
    private String mText;

    private final Paint mPaint = new Paint();
    private final Rect mBounds = new Rect();

    public VerticalLabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalLabelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerticalLabelView);
        try {
            mPaint.setTextSize(a.getDimensionPixelSize(R.styleable.VerticalLabelView_textSize, 15));
            mPaint.setColor(a.getColor(R.styleable.VerticalLabelView_textColor, 0xff000000));
            mText = a.getString(R.styleable.VerticalLabelView_text);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPaint.getTextBounds(mText, 0, mText.length(), mBounds);
        // Expand the bounds by 1 pixel to avoid rounding errors with anti-aliasing
        mBounds.inset(-1, -1);
        setMeasuredDimension(mBounds.height(), mBounds.width());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mBounds.height(), mBounds.width());
        canvas.rotate(-90);
        canvas.drawText(mText, 0, 0, mPaint);
        canvas.restore();
    }
}
