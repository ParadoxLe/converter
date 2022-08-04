package com.example.translation;

import static com.example.translation.util.XunfeiUtil.initXunFei;

import android.app.Activity;

import android.os.Bundle;
import android.os.MemoryFile;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.msc.util.FileUtil;

import java.util.Vector;

/*
*保存功能未实现
* 文字转语音*/
public class tToVActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    private static final String TAG = "tToVActivity";

    private SpeechSynthesizer mTts;//音乐合成对象

    // 默认发音人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Vector<byte[]> container = new Vector<>();
    //内存文件
    MemoryFile memoryFile;
    //总大小
    public volatile long mTotalSize = 0;



    //发音人名称
    private static final String[] arrayName = {"讯飞小燕", "讯飞许久", "讯飞小萍", "讯飞小婧", "讯飞许小宝"};

    //发音人值
    private static final String[] arrayValue = {"xiaoyan", "aisjiuxu", "aisxping", "aisjinger", "aisbabyxu"};

    //数组适配器
    private ArrayAdapter<String> arrayAdapter;

    EditText resultText;
    Button startButton;
    Button cancelButton;
    Button stopButton;
    Button contButton;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tto_vactivity);
        init();//初始化图标
        initXunFei(this);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

    }

    public void init() {
        startButton = (Button) findViewById(R.id.start);
        cancelButton = (Button) findViewById(R.id.cancel);
        stopButton = (Button) findViewById(R.id.stop);
        contButton = (Button) findViewById(R.id.cont);
        resultText = (EditText) findViewById(R.id.result2);
        startButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        contButton.setOnClickListener(this);

        spinner=(Spinner)findViewById(R.id.spinner);
        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, arrayName);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(arrayAdapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start://开始合成
                String text=resultText.getText().toString().trim();
                if(text.isEmpty()){
                    Toast.makeText(this, "请输入有效语句", Toast.LENGTH_SHORT).show();
                }
                else {
                    setParam();//设置参数
                    int code = mTts.startSpeaking(text, mTtsListener);
                    if (code != ErrorCode.SUCCESS) {
                        Toast.makeText(this, "语音合成失败！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.cancel://取消合成
                mTts.stopSpeaking();
                break;
            case R.id.stop://暂停播放
                mTts.pauseSpeaking();
                break;
            case R.id.cont://继续播放
                mTts.resumeSpeaking();
                break;
        }
    }

    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        // 设置音频保存路径，保存音频格式支持pcm、wav
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, getExternalFilesDir(null) + "/msc/tts.pcm");
    }


    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(tToVActivity.this, "初始化失败！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(tToVActivity.this, "初始化成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 合成过程监听生命周期。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        //开始播放
        @Override
        public void onSpeakBegin() {
            Log.d(TAG, "开始播放");
        }

        //暂停播放
        @Override
        public void onSpeakPaused() {
            Log.d(TAG, "暂停播放");
        }

        //继续播放
        @Override
        public void onSpeakResumed() {
            Log.d(TAG, "继续播放");
        }

        //合成进度
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            Log.d(TAG, "合成进度：" + percent + "%");
        }

        //播放进度
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            Log.i(TAG, "播放进度：" + percent + "%");
        }

        //播放完成
        @Override
        public void onCompleted(SpeechError error) {
            if(error==null){
                Log.d(TAG, "播放完成");
                for (int i = 0; i < container.size(); i++) {
                    //写入文件
                    writeToFile(container.get(i));
                }
                //保存文件
                FileUtil.saveFile(memoryFile, mTotalSize, getExternalFilesDir(null) + "/1.pcm");
                Toast.makeText(tToVActivity.this, "已保存到手机", Toast.LENGTH_SHORT).show();
            }else{
                Log.d(TAG, error.getPlainDescription(true));
            }
        }


        //抛出事件给讯飞
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };


    /**
     * 写入文件
     */
    private void writeToFile(byte[] data) {
        if (data == null || data.length == 0) {
            return;
        }
        try {
            if (memoryFile == null) {
                Log.i(TAG, "memoryFile is null");
                String mFilepath = getExternalFilesDir(null) + "/1.pcm";
                memoryFile = new MemoryFile(mFilepath, 1920000);
                memoryFile.allowPurging(false);
            }
            memoryFile.writeBytes(data, 0, (int) mTotalSize, data.length);
            mTotalSize += data.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //spinner点击
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        voicer = arrayValue[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
