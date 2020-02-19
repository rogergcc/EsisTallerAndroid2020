package com.rogergcc.ormroom.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rogergcc.ormroom.model.Contacto;

/**
 * Created by ROGERGCC on 18/02/2020.
 */
@Database(entities = {Contacto.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDataBase extends RoomDatabase {
    public abstract ContactDAO getContactDAO();
}
