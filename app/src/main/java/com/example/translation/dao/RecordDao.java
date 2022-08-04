package com.example.translation.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.translation.entity.Record;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("select * from record")
    List<Record> getAll();//查询所有

    @Query("select * from record where id=:id")
    Record findRecord(int id);//根据id查询

    @Query("select * from record where text like :text")
    List<Record> findRecordBlur(String text);//根据内容模糊查询

    @Insert
    void insert(Record record);//新增一条记录

    @Delete
    void delete(Record record);//删除一条记录

    @Query("delete from record")
    void deleteAll();//删除所有

    @Update
    void update(Record record);//修改一条记录
}
