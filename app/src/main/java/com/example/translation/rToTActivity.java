package com.example.translation;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.example.translation.dao.RecordDao;
import com.example.translation.db.AppDatabase;
import com.example.translation.entity.Record;
import com.tencent.cloud.qcloudasrsdk.filerecognize.QCloudFlashRecognizer;
import com.tencent.cloud.qcloudasrsdk.filerecognize.QCloudFlashRecognizerListener;
import com.tencent.cloud.qcloudasrsdk.filerecognize.param.QCloudFlashRecognitionParams;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/*录音转文字*/
public class rToTActivity extends AppCompatActivity implements View.OnClickListener, QCloudFlashRecognizerListener {
    private static final String TAG = "腾讯测试";
    Button select;//选择文件按钮
    Button save;
    EditText resultText;//转换结果

    private QCloudFlashRecognizer fileFlashRecognizer;

    ActivityResultLauncher launcher;//媒体文件访问启动器

    InputStream is;//输入数据流

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rto_tactivity);
        init();
        requestPermissions();//申请存储权限
        initUser();//直接鉴权
        set();//访问手机权限
    }

    public void init() {
        select = (Button) findViewById(R.id.clickButton6);
        save = (Button) findViewById(R.id.saveButton6);
        resultText = (EditText) findViewById(R.id.result4);
        select.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void requestPermissions() {
        try {
            //Android6.0及以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUser() {
        if (fileFlashRecognizer == null) {
            fileFlashRecognizer = new QCloudFlashRecognizer("1313190901", "AKIDzoOhjV0EBqFNEp9CySgeLlunPkob7MJ0", "RbU6RVIn20g6HBoAE2Zpn69vQfcJiqiI");
            fileFlashRecognizer.setCallback(this);
        }
    }

    public void set() {
        launcher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        //将uri对应的音频转为inputstream
                        is = null;
                        ContentResolver contentResolver = getContentResolver();
                        try {
                            is = contentResolver.openInputStream(result);
                            Log.d(TAG, "is=  " + is);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        load();
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clickButton6:
                launcher.launch("audio/*");
                break;
            case R.id.saveButton6:
                if (resultText.getText().toString().equals("")) {
                    Toast.makeText(this, "内容为空,保存失败", Toast.LENGTH_SHORT).show();
                } else {
                    Record record = new Record("语音转写", resultText.getText().toString());
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

    private void load() {
        try {
//            AssetManager am = getResources().getAssets();
//            is = am.open("test2.mp3");
            int length = is.available();
            byte[] audioData = new byte[length];
            is.read(audioData);

            QCloudFlashRecognitionParams params = (QCloudFlashRecognitionParams) QCloudFlashRecognitionParams.defaultRequestParams();
            params.setData(audioData);
            params.setVoiceFormat("m4a"); //音频格式。支持 wav、pcm、ogg-opus、speex、silk、mp3、m4a、aac。
            long ret = 0;
            ret = fileFlashRecognizer.recognize(params);
            if (ret >= 0) {
                Toast.makeText(this, "转译成功", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "录音文件不存在", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void recognizeResult(QCloudFlashRecognizer recognizer, String result, Exception exception) {
        if (exception != null) {
            resultText.setText(exception.getLocalizedMessage());
        } else {
            String s = StringUtils.substringBetween(result, "text\":\"","。");
            resultText.setText(s);
            Log.d(TAG, "result="+s);
        }
    }
}
