package com.example.translation.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.translation.R;
import com.example.translation.entity.Record;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {
    private List<Record> list;//显示数据

    //item点击事件接口
    public interface OnItemClickListener{
        public void OnItemClick(View view,int position);
    }

    OnItemClickListener onItemClickListener_short;//短时间点击事件
    OnItemClickListener onItemClickListener_long;//长时间点击事件

    //传递接口
    public void setOnItemClickListener_short(OnItemClickListener onItemClickListener_short){
        this.onItemClickListener_short=onItemClickListener_short;
    }

    public void setOnItemClickListener_long(OnItemClickListener onItemClickListener_long){
        this.onItemClickListener_long=onItemClickListener_long;
    }

    //数据初始化
    public RecordAdapter(List<Record> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView kind;//类型
        TextView content;//内容
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            kind=(TextView) itemView.findViewById(R.id.kind);
            content=(TextView) itemView.findViewById(R.id.content);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Record record=list.get(position);
        holder.kind.setText(record.getType());
        holder.content.setText(record.getText());
        //短时间点击
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener_short.OnItemClick(v,position);
            }
        });
        //长时间点击
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener_long.OnItemClick(v,position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
