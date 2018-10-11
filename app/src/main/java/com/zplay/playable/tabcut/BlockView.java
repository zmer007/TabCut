package com.zplay.playable.tabcut;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/10/10.
 */

public class BlockView extends ColorFrameLayout {

    private Block mBlock;

    public BlockView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (width != 0) {
            int height = (int) (width * 1f * mBlock.getHeight() / mBlock.getWidth());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setSolution(Block block) {
        mBlock = block;
    }

    public void drawSolution() {
        if (getWidth() != 0 && mBlock != null) {
            removeAllViews();
            draw(this, mBlock);
        }
        if (getWidth() == 0) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawSolution();
                }
            }, 200);
        }
    }

    private void draw(ColorFrameLayout parentFrame, Block block) {
        Block child = block.getChild();
        if (child == null) {
            return;
        }

        ScaleView childView = new ScaleView(getContext(), child.getWidth(), child.getHeight());
        childView.setLayoutParams(new LayoutParams(rePix(child.getWidth()), rePix(child.getHeight())));
        parentFrame.addView(childView);

        drawChildAB(parentFrame, block.getChildA());
        drawChildAB(parentFrame, block.getChildB());
    }

    private void drawChildAB(ColorFrameLayout parentFrame, Block block) {
        if (block.getArea() > 0) {
            Block parent = block.getParent();
            ColorFrameLayout bFrame = new ColorFrameLayout(getContext());
            LayoutParams bLayoutParams = new LayoutParams(rePix(block.getWidth()), rePix(block.getHeight()));
            if (block.getWidth() == parent.getWidth()) {
                bLayoutParams.topMargin = rePix(parent.getChild().getHeight());
            } else {
                bLayoutParams.leftMargin = rePix(parent.getChild().getWidth());
            }
            bFrame.setLayoutParams(bLayoutParams);
            parentFrame.addView(bFrame);
            draw(bFrame, block);
        }
    }

//    private void drawChildB(FrameLayout parentFrame, Block block) {
//        if (block.getArea() > 0) {
//            Block parent = block.getParent();
//            ColorFrameLayout bFrame = new ColorFrameLayout(getContext());
//            LayoutParams bLayoutParams = new LayoutParams(rePix(block.getWidth()), rePix(block.getHeight()));
//            if (block.getWidth() == parent.getWidth()) {
//                bLayoutParams.topMargin = rePix(parent.getChild().getHeight());
//            } else {
//                bLayoutParams.leftMargin = rePix(parent.getChild().getWidth());
//            }
//            bFrame.setLayoutParams(bLayoutParams);
//            parentFrame.addView(bFrame);
//            draw(bFrame, block);
//        }
//    }

    private int rePix(float origin) {
        return (int) (origin * getWidth() / mBlock.getWidth());
    }
}
