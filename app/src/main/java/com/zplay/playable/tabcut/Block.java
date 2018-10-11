package com.zplay.playable.tabcut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 如果有两个Block(B1与B2)，如果B1最小边等于B2最小边，且B1最大边等于B2最大边，则认为B1等于B2。
 * mLeft/mTop/mRight/mBottom是Block在其父Block中的相对位置。
 * child代表已使用的块，childA/B代表下脚料，其中childA < childB
 */
public class Block implements Comparable<Block> {
    public static final int cut_type_hh = 0;
    public static final int cut_type_hv = 1;
    public static final int cut_type_vh = 2;
    public static final int cut_type_vv = 3;

    int mTag;
    int m0Width;
    int m1Height;

    int mCutType = 0;

    Block mParent;

    Block mChild;
    Block mChildA;
    Block mChildB;

    public static Block copyInstance(Block block) {
        if (block == null) {
            return null;
        }
        Block b = new Block(block);

        b.mChild = copyInstance(block.mChild);
        b.setChild(copyInstance(block.mChild));
        b.setChildA(copyInstance(block.mChildA));
        b.setChildB(copyInstance(block.mChildB));

        return b;
    }

    public Block(int width, int height) {
        m0Width = width;
        m1Height = height;
    }

    public Block(Block block) {
        this(block, false);
    }

    public Block(Block block, boolean reverse) {
        m0Width = reverse ? block.m1Height : block.m0Width;
        m1Height = reverse ? block.m0Width : block.m1Height;
        mTag = block.mTag;
    }

    public void setCutType(int cutType) {
        mCutType = cutType;
    }

    public int getCutType() {
        return mCutType;
    }

    public boolean isValiable() {
        return m0Width >= 0 && m1Height >= 0;
    }

    public Block getParent() {
        return mParent;
    }

    public List<Block> getAllOffcut() {
        ArrayList<Block> allOffcuts = new ArrayList<>();
        getRoot().getOffcutBlock(allOffcuts);
        return allOffcuts;
    }

    private void getOffcutBlock(List<Block> offcuts) {
        if (mChild == null) {
            offcuts.add(this);
        }
        mChildA.getOffcutBlock(offcuts);
        mChildB.getOffcutBlock(offcuts);
    }

    public Block getRoot() {
        Block root = this;
        for (; ; ) {
            if (root.getParent() == null) {
                return root;
            }
            root = root.getParent();
        }
    }

    private void setParent(Block parent) {
        mParent = parent;
    }

    public Block getChild() {
        return mChild;
    }

    // public List<Block> getAllDirectChild() {
    // List<Block> allChild = new LinkedList<>();
    // if(mChild != null){
    // allChild.add(mChild);
    // }
    // return allChild;
    // }

    public void setChild(Block child) {
        if (child == null) {
            return;
        }
        mChild = child;
        mChild.setParent(this);
    }

    public void removeAllChilden() {
        mChild = null;
        mChildA = null;
        mChildB = null;
    }

    public Block getChildA() {
        return mChildA;
    }

    public void setChildA(Block child) {
        if (child == null) {
            return;
        }
        mChildA = child;
        mChildA.setParent(this);
    }

    public Block getChildB() {
        return mChildB;
    }

    public void setChildB(Block child) {
        if (child == null) {
            return;
        }
        mChildB = child;
        mChildB.setParent(this);
    }

    public float getUtilizationRate() {
        return 1 - getOffcut() * 1.0f / getArea();
    }

    public int getOffcut() {
        if (mChild == null) {
            return getArea();
        }
        return mChildA.getOffcut() + mChildB.getOffcut();
    }

    public int getArea() {
        return m0Width * m1Height;
    }

    public void setWidth(int w) {
        m0Width = w;
    }

    public int getWidth() {
        return m0Width;
    }

    public int getHeight() {
        return m1Height;
    }

    public void setTag(int tag) {
        mTag = tag;
    }

    public int getTag() {
        return mTag;
    }

    @Override
    public String toString() {
        return "{" + m0Width + "," + m1Height + "}";
    }

    @Override
    public int compareTo(Block o) {
        if (o == null) {
            return -1;
        }
        int area = getArea() - o.getArea();
        if (area != 0) {
            return area;
        }
        return Math.min(m0Width, m1Height) - Math.min(o.m0Width, o.m1Height);
    }

    public boolean canCut(Block target) {
        return m0Width >= target.m0Width && m1Height >= target.m1Height;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Block)) {
            return false;
        }

        Block o = (Block) obj;
        return (Math.min(m0Width, m1Height) == Math.min(o.m0Width, o.m1Height))
                && (Math.max(m0Width, m1Height) == Math.max(o.m0Width, o.m1Height));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{Math.min(m0Width, m1Height), Math.max(m0Width, m1Height)});
    }

    static void setChildAB(Block parent, Block a, Block b) {
        if (a.compareTo(b) < 0) {
            parent.setChildA(a);
            parent.setChildB(b);
        } else {
            parent.setChildA(b);
            parent.setChildB(a);
        }
    }
}