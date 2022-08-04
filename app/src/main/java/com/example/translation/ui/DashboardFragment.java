package com.example.translation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.translation.R;
import com.example.translation.pToTActivity;
import com.example.translation.rToTActivity;
import com.example.translation.tToVActivity;
import com.example.translation.vToTActivity;


public class DashboardFragment extends Fragment {

    Button tToV;//文字转语音
    Button vToT;//语音转文字
    Button rToT;//录音转文字
    Button pToT;//文字转语音
    Intent intent;

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.textToVoice:
                    intent=new Intent(getActivity(), tToVActivity.class);
                    startActivity(intent);
                    break;
                case R.id.voiceToText:
                    intent=new Intent(getActivity(), vToTActivity.class);
                    startActivity(intent);
                    break;
                case R.id.radioToText:
                    intent=new Intent(getActivity(), rToTActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pictureToText:
                    intent=new Intent(getActivity(), pToTActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard,container,false);
        init(view);
        getActivity().setTitle("文字转换语音");
        return view;
    }

    public void init(View view) {
        tToV=(Button)view.findViewById(R.id.textToVoice);
        vToT=(Button)view.findViewById(R.id.voiceToText);
        rToT=(Button)view.findViewById(R.id.radioToText);
        pToT=(Button)view.findViewById(R.id.pictureToText);

        tToV.setOnClickListener(listener);
        vToT.setOnClickListener(listener);
        rToT.setOnClickListener(listener);
        pToT.setOnClickListener(listener);
    }

}