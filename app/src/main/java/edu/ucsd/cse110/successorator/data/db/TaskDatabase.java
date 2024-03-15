package edu.ucsd.cse110.successorator.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.ucsd.cse110.successorator.util.Converters;

@Database(entities = {TaskEntity.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
