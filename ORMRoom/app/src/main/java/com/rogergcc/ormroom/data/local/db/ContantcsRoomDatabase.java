package com.rogergcc.ormroom.data.local.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rogergcc.ormroom.data.local.db.dao.ContactDAO;
import com.rogergcc.ormroom.entity.Contacto;

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
                            //.fallbackToDestructiveMigration //-> users will lose their data once the app is updated
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return contantcsRoomDatabase;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Your migration strategy here
            database.execSQL("ALTER TABLE contact ADD COLUMN id INTEGER");
        }
    };
}
