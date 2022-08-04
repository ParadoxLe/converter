package com.example.translation.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.translation.dao.RecordDao;
import com.example.translation.entity.Record;

@Database(entities = {Record.class},version = 1)
public abstract class AppDatabase extends RoomDatabase{
    public abstract RecordDao recordDao();
}
