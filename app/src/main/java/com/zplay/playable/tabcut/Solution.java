package com.zplay.playable.tabcut;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/10/10.
 */

public class Solution implements Comparable<Solution> {
    Block mBlock;

    public Solution(Block block) {
        mBlock = new Block(block);
    }

    public Block getBlock() {
        return mBlock;
    }

    public void refresh(Block block) {
        Block rootBlock = block.getRoot();
        if (rootBlock.getOffcut() < mBlock.getOffcut()) {
            mBlock = Block.copyInstance(rootBlock);
            System.out.println("offcut: " + mBlock.getOffcut());
            System.out.println("utilization: " + mBlock.getUtilizationRate());
            System.out.println();
        }
    }

    @Override
    public int compareTo(Solution o) {
        if (o == null) {
            return -1;
        }
        return mBlock.compareTo(o.mBlock);
    }
}