package com.example.translation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.translation.dao.RecordDao;
import com.example.translation.db.AppDatabase;
import com.example.translation.entity.Record;

public class ModifyActivity extends AppCompatActivity implements View.OnClickListener {

    int id;//主键id
    TextView topic;//类型
    EditText modifyresult;//修改内容
    Button modifyButton;//修改保存按钮

    RecordDao recordDao;//数据库操作对象
    Record record;//编辑对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        recordDao = db.recordDao();
        init();
        initData();//接收数据
    }

    public void init() {
        topic = (TextView) findViewById(R.id.topic);
        modifyresult = (EditText) findViewById(R.id.modifyresult);
        modifyButton = (Button) findViewById(R.id.modifyButton);
        modifyButton.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");
        record = recordDao.findRecord(id);
        topic.setText(record.getType());
        modifyresult.setText(record.getText());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modifyButton:
                record.setType(topic.getText().toString());
                record.setText(modifyresult.getText().toString());
                recordDao.update(record);
                finish();
                break;
        }
    }
}