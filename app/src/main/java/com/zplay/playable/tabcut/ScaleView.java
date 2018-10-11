package com.zplay.playable.tabcut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/10/10.
 */

public class ScaleView extends View {
    private final int mScaleWidth;
    private final int mScaleHeight;
    private Paint mFramePaint;
    private Paint mScaleTextPaint;
    private Rect mTextRect = new Rect();

    public ScaleView(Context context, int width, int height) {
        super(context);
        mScaleWidth = width;
        mScaleHeight = height;

        mFramePaint = new Paint();
        mFramePaint.setColor(0xff222222);
        mFramePaint.setStrokeWidth(getResources().getDisplayMetrics().density);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setAntiAlias(true);

        mScaleTextPaint = new Paint(mFramePaint);
        mScaleTextPaint.setTextSize(40);
        mScaleTextPaint.setColor(0xffff0000);
        mScaleTextPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mFramePaint);
        canvas.save();
        final String widthStr = String.valueOf(mScaleWidth);
        mScaleTextPaint.getTextBounds(widthStr, 0, widthStr.length(), mTextRect);
        canvas.translate((getWidth() - mTextRect.width()) / 2, 0);
        canvas.drawText(widthStr, -mTextRect.left, -mTextRect.top, mScaleTextPaint);
        canvas.restore();

        canvas.save();
        final String heightStr = String.valueOf(mScaleHeight);
        mScaleTextPaint.getTextBounds(heightStr, 0, heightStr.length(), mTextRect);
        canvas.rotate(90);
        canvas.translate((getHeight() - mTextRect.width()) / 2, -getWidth() + mTextRect.height());
        canvas.drawText(heightStr, 0, 0, mScaleTextPaint);
        canvas.restore();
    }
}
