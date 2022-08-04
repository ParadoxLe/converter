package com.example.translation.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.translation.ModifyActivity;
import com.example.translation.R;
import com.example.translation.adapter.RecordAdapter;
import com.example.translation.dao.RecordDao;
import com.example.translation.db.AppDatabase;
import com.example.translation.entity.Record;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "刷新测试";

    private List<Record> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecordAdapter adapter;

    TextView hintText;//提示信息
    Button deleteAll;//清空按钮
    SearchView search;//搜索按钮

    RecordDao recordDao;//数据库操作对象

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        getActivity().setTitle("历史记录");
        initData();
        init(view);
        initRecyclerView(view);
        setItemclickListener();//设置每一个item的点击事件
        setSearchlistener();//搜索事件
        Log.d(TAG, "onCreateView: ");
        return view;
    }

    public void initData() {
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        recordDao = db.recordDao();
        list = recordDao.getAll();
    }

    public void init(View view) {
        hintText = (TextView) view.findViewById(R.id.hintText);
        deleteAll = (Button) view.findViewById(R.id.deleteAll);
        search=(SearchView)view.findViewById(R.id.search);
        deleteAll.setOnClickListener(this);
    }


    public void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        // 定义一个线性布局管理器（默认是垂直排列）
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()); //默认垂直排列
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences sp = getActivity().getSharedPreferences("mydata", Context.MODE_PRIVATE);
        boolean flag = sp.getBoolean("flag", false);
        adapter = new RecordAdapter(list);
        if (flag) {
            hintText.setText("历史记录");
            recyclerView.setAdapter(adapter);
        } else {
            deleteAll.setEnabled(false);//按钮禁用
            //提示红色登陆提醒
            hintText.setText("请先登录！(登录后可查看)");
            hintText.setTextColor(android.graphics.Color.RED);
        }
    }

    private void setItemclickListener() {
        //短时间点击，进入编辑界面
        adapter.setOnItemClickListener_short(new RecordAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Record record = list.get(position);
                Intent intent = new Intent(getActivity(), ModifyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", record.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //长时间点击，删除操作
        adapter.setOnItemClickListener_long(new RecordAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Record record = list.get(position);
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确认删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recordDao.delete(record);
                                load();
                                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    public void setSearchlistener() {
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击搜索发生事件
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            //内容变化发生事件
            @Override
            public boolean onQueryTextChange(String newText) {
                //内容为空显示
                if(newText.length()==0){
                    list.clear();
                    load();
                }else{
                    //不为空则判断有没有该记录
                    List<Record> list=recordDao.findRecordBlur("%"+newText+"%");
                    if(list.size()==0){
                        Toast.makeText(getActivity(), "没有该记录！", Toast.LENGTH_SHORT).show();
                    }else{
                        adapter=new RecordAdapter(list);
                        recyclerView.setAdapter(adapter);
                    }
                }
                return false;
            }
        });
    }

    //页面加载方法
    public void load(){
        list = recordDao.getAll();
        adapter = new RecordAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteAll:
                list = recordDao.getAll();
                if (list.size() != 0) {
                    recordDao.deleteAll();
                    load();
                    Toast.makeText(getActivity(), "清空完成", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "内容为空！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}