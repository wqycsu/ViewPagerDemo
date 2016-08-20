package com.wqycsu.viewpagerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button button;
    private ArrayList<Integer> images = new ArrayList<Integer>();
    private CardFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.show_cards_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardsDialog();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        initData();
    }

    private void initData() {
        //设置默认数据
        for(int i = 0;i < 5; i++) {
            images.add(0);
        }
    }

    private void showCardsDialog() {
        dialogFragment = (CardFragment) getSupportFragmentManager().findFragmentByTag("CardFragment");
        if(dialogFragment == null) {
            dialogFragment = CardFragment.newInstance(images);
        }
        dialogFragment.show(getSupportFragmentManager(), "CardFragment");
        Handler handler = new Handler(Looper.getMainLooper());
        //模拟延迟加载数据
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                images.set(0, R.mipmap.meizi);
                dialogFragment.updateDataSet(0);
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                images.set(1, R.mipmap.meizi);
                dialogFragment.updateDataSet(1);
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                images.set(2, R.mipmap.meizi);
                dialogFragment.updateDataSet(2);
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                images.set(3, R.mipmap.meizi);
                dialogFragment.updateDataSet(3);
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                images.set(4, R.mipmap.meizi);
                dialogFragment.updateDataSet(4);
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
