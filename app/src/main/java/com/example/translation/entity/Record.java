package com.example.translation.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//历史记录数据库模型
@Entity(tableName = "record")
public class Record {
    @PrimaryKey(autoGenerate = true)
    public int id;//编号及主键

    @ColumnInfo(name="type")
    public String type;//转换类型

    @ColumnInfo(name="text")
    public String text;//转换结果

    public Record(int id, String type, String text) {
        this.id = id;
        this.type = type;
        this.text = text;
    }

    @Ignore
    public Record(String type,String text){
        this.type=type;
        this.text=text;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }
}
