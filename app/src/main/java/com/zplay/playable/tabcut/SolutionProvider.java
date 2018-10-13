package com.zplay.playable.tabcut;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/10/10.
 */

public class SolutionProvider {
    private static List<Solution> mSrcBlocks = new ArrayList<>();
    private static TreeList<Block> mTargetBlocks = new TreeList<>();

    public void initBlocks() {
        // mSrcBlocks.add(new Solution(new Block(7, 5)));
        // mTargetBlocks.add(new Block(4, 3));
        // mTargetBlocks.add(new Block(4, 3));
        // mTargetBlocks.add(new Block(3, 2));
        // mTargetBlocks.add(new Block(3, 1));
        // mTargetBlocks.add(new Block(2, 1));
        // mTargetBlocks.add(new Block(2, 1));

        // mSrcBlocks.add(new Solution(new Block(4, 4)));
        // mTargetBlocks.add(new Block(3, 2));
        // mTargetBlocks.add(new Block(2, 2));
        // mTargetBlocks.add(new Block(3, 1));
        // mTargetBlocks.add(new Block(2, 1));

        mSrcBlocks.add(new Solution(new Block(2440, 1900)));
        mTargetBlocks.add(new Block(1070, 995));
        mTargetBlocks.add(new Block(1090, 850));
        mTargetBlocks.add(new Block(1070, 855));
        mTargetBlocks.add(new Block(850, 805));
        mTargetBlocks.add(new Block(930, 510));
        mTargetBlocks.add(new Block(930, 510));

        // mSrcBlocks.add(new Solution(new Block(2440, 1900)));
        // mTargetBlocks.add(new Block(1070, 995));
        // mTargetBlocks.add(new Block(1090, 850));
        // mTargetBlocks.add(new Block(1070, 855));
        // mTargetBlocks.add(new Block(850, 805));
        // mTargetBlocks.add(new Block(930, 510));
        // mTargetBlocks.add(new Block(930, 510));

        // System.out.println(2440 * 1900 - 1070 * 855 - 1070 * 995 - 1090 * 850 - 850 *
        // 805 - 930 * 510 - 930 * 510);

        // mSrcBlocks.add(new Solution(new Block(2440, 1900)));
        // mTargetBlocks.add(new Block(1085, 915));
        // mTargetBlocks.add(new Block(1085, 915));
        // mTargetBlocks.add(new Block(920, 925));
        // mTargetBlocks.add(new Block(925, 920));
        // mTargetBlocks.add(new Block(940, 435));
        // mTargetBlocks.add(new Block(960, 435));

        // mSrcBlocks.add(new Solution(new Block(2440, 1900)));
        // mTargetBlocks.add(new Block(1090, 915));
        // mTargetBlocks.add(new Block(1090, 915));
        // mTargetBlocks.add(new Block(925, 915));
        // mTargetBlocks.add(new Block(925, 915));
        // mTargetBlocks.add(new Block(945, 435));
        // mTargetBlocks.add(new Block(945, 435));
    }

    MainActivity.OnSolutionListener mOnSolutionListener;

    Solution provide(MainActivity.OnSolutionListener listener) {
        mOnSolutionListener = listener;
        initBlocks();
        Collections.sort(mSrcBlocks);

        int i = 0;
        for (Block b : mTargetBlocks) {
            b.setTag(i + 1);
            i++;
        }

        return cutSrcBlock(mSrcBlocks.get(0).getBlock(), mTargetBlocks);
    }

    private Solution cutSrcBlock(Block src, TreeList<Block> targets) {
        Solution result = new Solution(new Block(src));
        Stack<Block> cuttedStack = new Stack<>();
        Queue<Block> offcutQueue = new ArrayDeque<>();
        offcutQueue.add(src);
        while (!offcutQueue.isEmpty() || !cuttedStack.isEmpty()) {
            if (targets.isEmpty()) {
                result.refresh(cuttedStack.firstElement());
                break;
            }
            if (offcutQueue.isEmpty()) {
                result.refresh(cuttedStack.firstElement());
                Block popBlock = cuttedStack.pop();
                Block popParent = popBlock.getParent();
                if(popParent != null){
                    popParent.removeAllChilden();
                }
                targets.add(new Block(popBlock.getChild()));

                if (popBlock.getCutType() == 0) {
                    List<Block> cutted = cutByHv(popBlock, targets);
                    if (cutted != null) {
                        popBlock.setCutType(Block.cut_type_hv);
                        touchData(targets, cuttedStack, offcutQueue, cutted, popBlock);
                    }
                } else if (popBlock.getCutType() == 1) {
                    List<Block> cutted = cutByVh(popBlock, targets);
                    if (cutted != null) {
                        popBlock.setCutType(Block.cut_type_vh);
                        touchData(targets, cuttedStack, offcutQueue, cutted, popBlock);
                    }
                } else if (popBlock.getCutType() == 2) {
                    List<Block> cutted = cutByVv(popBlock, targets);
                    if (cutted != null) {
                        popBlock.setCutType(Block.cut_type_vv);
                        touchData(targets, cuttedStack, offcutQueue, cutted, popBlock);
                    }
                }
            }

            if (!offcutQueue.isEmpty()) {
                Block cutBlock = offcutQueue.poll();
                List<Block> cutted = cutByHh(cutBlock, targets);
                if (cutted == null) {
                    cutted = cutByHv(cutBlock, targets);
                    if (cutted == null) {
                        cutted = cutByVh(cutBlock, targets);
                        if (cutted == null) {
                            cutted = cutByVv(cutBlock, targets);
                            if (cutted != null) {
                                cutBlock.setCutType(Block.cut_type_vv);
                                touchData(targets, cuttedStack, offcutQueue, cutted, cutBlock);
                            }
                        } else {
                            cutBlock.setCutType(Block.cut_type_vh);
                            touchData(targets, cuttedStack, offcutQueue, cutted, cutBlock);
                        }
                    } else {
                        cutBlock.setCutType(Block.cut_type_hv);
                        touchData(targets, cuttedStack, offcutQueue, cutted, cutBlock);
                    }
                } else {
                    cutBlock.setCutType(Block.cut_type_hh);
                    touchData(targets, cuttedStack, offcutQueue, cutted, cutBlock);
                }
            }
        }
        return result;
    }

    private void touchData(TreeList<Block> targets, Stack<Block> cuttedStack, Queue<Block> offcutQueue,
                           @NonNull List<Block> cutted, Block cutBlock) {
        targets.remove(cutted.get(0));
        offcutQueue.add(cutted.get(1));
        offcutQueue.add(cutted.get(2));
        cuttedStack.add(cutBlock);
        mOnSolutionListener.onResult(cuttedStack.firstElement());
        for (; ; ) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (cnt) {
                cnt = !cnt;
                break;
            }
        }
    }

    boolean cnt;

    private List<Block> cutByHh(Block src, TreeList<Block> targets) {
        Block target = getAvailableBlock(src, targets);
        if (target == null) {
            return null;
        }

        Block child = new Block(target);
        Block childA = new Block(src.getWidth() - child.getWidth(), child.getHeight());
        Block childB = new Block(src.getWidth(), src.getHeight() - child.getHeight());

        src.setChild(child);
        Block.setChildAB(src, childA, childB);

        return Arrays.asList(target, src.getChildA(), src.getChildB());
    }

    private List<Block> cutByHv(Block src, TreeList<Block> targets) {
        Block target = getAvailableBlock(src, targets);
        if (target == null) {
            return null;
        }

        Block child = new Block(target);
        Block childA = new Block(src.getWidth() - child.getWidth(), src.getHeight());
        Block childB = new Block(child.getWidth(), src.getHeight() - child.getHeight());

        src.setChild(child);
        Block.setChildAB(src, childA, childB);

        return Arrays.asList(target, src.getChildA(), src.getChildB());
    }

    private List<Block> cutByVh(Block src, TreeList<Block> targets) {
        Block target = getAvailableBlock(new Block(src, true), targets);
        if (target == null) {
            return null;
        }

        Block child = new Block(target, true);
        Block childA = new Block(src.getWidth() - child.getWidth(), child.getHeight());
        Block childB = new Block(src.getWidth(), src.getHeight() - child.getHeight());

        src.setChild(child);
        Block.setChildAB(src, childA, childB);

        return Arrays.asList(target, src.getChildA(), src.getChildB());
    }

    private List<Block> cutByVv(Block src, TreeList<Block> targets) {
        Block target = getAvailableBlock(new Block(src, true), targets);
        if (target == null) {
            return null;
        }

        Block child = new Block(target, true);
        Block childA = new Block(src.getWidth() - child.getWidth(), src.getHeight());
        Block childB = new Block(child.getWidth(), src.getHeight() - child.getHeight());

        src.setChild(child);
        Block.setChildAB(src, childA, childB);

        return Arrays.asList(target, src.getChildA(), src.getChildB());
    }

    private Block getAvailableBlock(Block src, TreeList<Block> targets) {
        Block target = null;
        for (Block b : targets) {
            if (src.canCut(b)) {
                target = b;
                break;
            }
        }
        return target;
    }
}
