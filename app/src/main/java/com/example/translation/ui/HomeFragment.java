package com.example.translation.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.translation.R;
import com.example.translation.agreementActivity;
import com.example.translation.loginActivity;
import com.example.translation.privaryActivity;


public class HomeFragment extends Fragment {
    private static final String TAG = "test";

    ImageView avator;//头像
    TextView username;//用户名

    Button agreement;//用户协议
    Button privacy;//隐私
    Button logout;//登出

    Boolean state = false;//登陆状态 false:未登录

    Intent intent;


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.avator:
                    intent = new Intent(getActivity(), loginActivity.class);
                    startActivity(intent);
                    break;
                case R.id.agreement:
                    intent = new Intent(getActivity(), agreementActivity.class);
                    startActivity(intent);
                    break;
                case R.id.privacy:
                    intent = new Intent(getActivity(), privaryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    if (!state) {
                        Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                    } else {
                        //登出清除数据
                        remove();
                        Toast.makeText(getActivity(), "登出成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("个人中心");//设置actionbar标题
        init(view);
        return view;
    }


    public void init(View view) {
        avator = (ImageView) view.findViewById(R.id.avator);
        username = (TextView) view.findViewById(R.id.username);
        agreement = (Button) view.findViewById(R.id.agreement);
        privacy = (Button) view.findViewById(R.id.privacy);
        logout = (Button) view.findViewById(R.id.logout);

        avator.setOnClickListener(listener);
        agreement.setOnClickListener(listener);
        privacy.setOnClickListener(listener);
        logout.setOnClickListener(listener);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp = getActivity().getSharedPreferences("mydata", Context.MODE_PRIVATE);
        if (sp.getBoolean("flag", false)) {
            String sex=sp.getString("sex","");
            if(sex.equals("male")) avator.setBackgroundResource(R.drawable.male);
            if(sex.equals("female")) avator.setBackgroundResource(R.drawable.female);
            String un = sp.getString("username", "");
            username.setText("Hello! " + un);
            avator.setEnabled(false);
            state = true;
        }
    }

    public void remove() {
        SharedPreferences sp = getActivity().getSharedPreferences("mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        avator.setEnabled(true);
        state = false;
        //重新加载activity
        Intent intent = getActivity().getIntent();
        getActivity().finish();
        startActivity(intent);
    }



}