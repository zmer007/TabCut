package com.zplay.playable.tabcut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/10/11.
 */

public class ColorFrameLayout extends FrameLayout {
    private static final int COLORS[] = new int[]{0x88ff0000, 0x8800ff00, 0x880000ff};
    private static int colorIndex = 0;

    private Paint mPaint;

    public ColorFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ColorFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(COLORS[colorIndex++ % COLORS.length]);
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), mPaint);
    }
}
