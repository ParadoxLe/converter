package com.example.translation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class loginActivity extends AppCompatActivity {
    private static final String TAG = "test";

    EditText usernameEdit, passwordEdit;//用户名，密码

    RadioButton male, female;

    Button wxlogin;//登录按钮


    SharedPreferences sp;//数据访问

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor;
            switch (v.getId()) {
                case R.id.male:
                    sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    editor = sp.edit();
                    editor.putString("sex", "male");
                    editor.apply();
                    Toast.makeText(loginActivity.this, "选中男", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.female:
                    sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    editor = sp.edit();
                    editor.putString("sex", "female");
                    editor.apply();
                    Toast.makeText(loginActivity.this, "选中女", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.wxlogin:
                    login();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        wxlogin = (Button) findViewById(R.id.wxlogin);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        wxlogin.setOnClickListener(listener);
        male.setOnClickListener(listener);
        female.setOnClickListener(listener);
    }

    //登录将数据存入
    public void login() {
        if (usernameEdit.getText().toString().isEmpty() || passwordEdit.getText().toString().isEmpty()) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            String username = usernameEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            Boolean flag = true;

            sp = getSharedPreferences("mydata", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putBoolean("flag", flag);
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();
            Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


}