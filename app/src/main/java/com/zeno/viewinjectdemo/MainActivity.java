package com.zeno.viewinjectdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zeno.viewinject.annotation.VInjector;


public class MainActivity extends AppCompatActivity {

    @VInjector(R.id.tv_show)
    TextView tvShow;
    @VInjector(R.id.tv_show1)
    TextView tvshow1;
    @VInjector(R.id.tv_show2)
    TextView tvShow2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
