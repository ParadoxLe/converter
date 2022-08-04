package com.example.translation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kotlin.jvm.internal.Lambda;

public class privaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("隐私政策");
        setContentView(R.layout.activity_privary);
    }
}