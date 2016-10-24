package com.zeno.viewinjectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zeno.viewinject.ViewInject;
import com.zeno.viewinject.annotation.VInjector;

public class LoginActivity extends AppCompatActivity {

    @VInjector(R.id.et_user_name)
    EditText etUserName;
    @VInjector(R.id.et_password)
    EditText etPassword;
    @VInjector(R.id.btn_login)
    Button btnLogion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewInject.inject(this);



        btnLogion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                Toast.makeText(LoginActivity.this, " userName : "+userName+"\n password : "+password, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
