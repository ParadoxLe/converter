package com.example.translation;

import static com.example.translation.util.XunfeiUtil.initXunFei;
import static com.example.translation.util.XunfeiUtil.parseIatResult;
import static com.example.translation.util.XunfeiUtil.startVoice;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.example.translation.dao.RecordDao;
import com.example.translation.db.AppDatabase;
import com.example.translation.entity.Record;
import com.example.translation.util.XunFeiCallbackListener;
import com.iflytek.cloud.RecognizerResult;

/*录音转文字*/
public class vToTActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "test";

    Button clickButton;
    Button saveButton;
    EditText resultText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vto_tactivity);
        initXunFei(this);
        init();
        requestPermissions();//申请权限麦克风
    }

    public void init() {
        clickButton = (Button) findViewById(R.id.clickButton5);
        saveButton = (Button) findViewById(R.id.saveButton5);
        resultText = (EditText) findViewById(R.id.result1);
        clickButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "this=" + this);
        switch (v.getId()) {
            case R.id.clickButton5:
                startVoice(this, new XunFeiCallbackListener() {
                    @Override
                    public void onFinish(RecognizerResult results) {
                        Log.d(TAG, this + "=");
                        String text = parseIatResult(results.getResultString());
                        // 自动填写到当前内容后面
                        resultText.append(text);
                    }
                });
                break;
            case R.id.saveButton5:
                if (resultText.getText().toString().equals("")) {
                    Toast.makeText(this, "内容为空,保存失败", Toast.LENGTH_SHORT).show();
                } else {
                    Record record = new Record("语音听写", resultText.getText().toString());
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mydb")
                            .allowMainThreadQueries()
                            .build();
                    RecordDao recordDao = db.recordDao();
                    recordDao.insert(record);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }


    /**
     * 请求权限
     */
    private void requestPermissions() {
        try {
            //Android6.0及以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.RECORD_AUDIO},
                            0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

