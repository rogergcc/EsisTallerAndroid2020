package com.rogergcc.ormroom.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.rogergcc.ormroom.model.Contacto;

/**
 * Created by ROGERGCC on 18/02/2020.
 */
//@Database(entities = {Contacto.class}, version = 1, exportSchema = false)
@Database(entities = {Contacto.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class ContantcsRoomDatabase extends RoomDatabase {
    public abstract ContactDAO getContactDAO();

    private static volatile ContantcsRoomDatabase contantcsRoomDatabase;

    public static ContantcsRoomDatabase getDatabase(Context context) {
        if (contantcsRoomDatabase == null) {
            synchronized (ContantcsRoomDatabase.class) {
                if (contantcsRoomDatabase == null) {
                    contantcsRoomDatabase = Room.databaseBuilder(context.getApplicationContext(),
                            ContantcsRoomDatabase.class, "db-contacts")
//                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return contantcsRoomDatabase;
    }
}
