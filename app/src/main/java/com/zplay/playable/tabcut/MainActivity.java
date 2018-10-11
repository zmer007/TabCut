package com.zplay.playable.tabcut;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    BlockView mBlockView;
    Thread mThread;
    SolutionProvider sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlockView = findViewById(R.id.blockView);
        sp = new SolutionProvider();
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                sp.provide(new OnSolutionListener() {
                    @Override
                    public void onResult(final Block block) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mBlockView.setSolution(block);
                                mBlockView.drawSolution();
                            }
                        });
                    }
                });
            }
        });
        mThread.start();
    }

    public void drawBlock(View view) {
        sp.cnt = true;
    }

    interface OnSolutionListener {
        void onResult(Block block);
    }
}
