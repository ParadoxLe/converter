package com.example.translation;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.translation.dao.RecordDao;
import com.example.translation.db.AppDatabase;
import com.example.translation.entity.Record;
import com.example.translation.util.BaiduUtil;
import com.example.translation.util.JsonResult;
import com.google.gson.Gson;

import java.io.File;


/*拍照取字*/
public class pToTActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "百度测试";
    EditText resultText;
    Button take;
    Button save;

    //百度平台上的apikey
    private String apiKey = "P0XPG9XysDa153H62g0euFYs";

    //百度平台上的应用secretKey
    private String secretKey = "w8tEt5sAUT7wWuxtF0InT77Is9xcdLkE";

    /**
     * 通用文字识别请求码
     */
    private static final int REQUEST_CODE_GENERAL_BASIC = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pto_tactivity);
        init();
        initTextSDK();//检验sdk安装是否成功
        requestPermissions();//申请相机与图片的权限
    }

    private void initTextSDK() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                Log.d(TAG, "成功！" + token);
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.d(TAG, "失败！" + error.getMessage());

            }
        }, getApplicationContext(), apiKey, secretKey);
    }


    public void init() {
        resultText = (EditText) findViewById(R.id.result3);
        take = (Button) findViewById(R.id.take);
        save = (Button) findViewById(R.id.save1);
        take.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void requestPermissions() {
        try {
            //Android6.0及以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.READ_EXTERNAL_STORAGE},
                            0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take:
                take();
                break;
            case R.id.save1:
                if (resultText.getText().toString().equals("")) {
                    Toast.makeText(this, "内容为空,保存失败", Toast.LENGTH_SHORT).show();
                } else {
                    Record record = new Record("文字识别", resultText.getText().toString());
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
     * 通用文字识别(高精度)
     * 拍照实现
     *
     * @param
     */
    public void take() {
        Intent intent = new Intent(pToTActivity.this, CameraActivity.class);
        //传入文件保存的路径
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, getSaveFile(getApplication()).getAbsolutePath());
        //传入文件类型
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
        //跳转页面时传递请求码，返回时根据请求码判断获取识别的数据。
        startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
    }


    /**
     * Toast提示
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 获取保存文件
     *
     * @param context
     * @return
     */
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    /**
     * Activity回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            BaiduUtil.recAccurateBasic(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new BaiduUtil.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            //数据解析 Gson将JSON字符串转为实体Bean
                            JsonResult jsonResult = new Gson().fromJson(result, JsonResult.class);
                            if (jsonResult.getWords_result() == null && jsonResult.getWords_result().size() <= 0) {
                                return;
                            }
                            String text = "";
                            //数据不为空并且大于0
                            for (int i = 0; i < jsonResult.getWords_result().size(); i++) {
                                text += jsonResult.getWords_result().get(i).getWords() + "\n";
                            }
                            resultText.setText(text);
                            Log.d(TAG, result);
                        }
                    });
        }
    }

}
