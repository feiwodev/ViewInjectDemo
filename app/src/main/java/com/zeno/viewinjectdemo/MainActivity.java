package com.zeno.viewinjectdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zeno.viewinject.ViewInject;
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
        ViewInject.inject(this);

        tvShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "注入成功。。。", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }
}
