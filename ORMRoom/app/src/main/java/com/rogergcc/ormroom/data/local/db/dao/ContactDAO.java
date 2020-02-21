package com.rogergcc.ormroom.data.local.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rogergcc.ormroom.entity.Contacto;

import java.util.List;

/**
 * Created by ROGERGCC on 18/02/2020.
 */

@Dao
public interface ContactDAO {
    @Insert
    public void insert(Contacto... contacts);

    @Update
    public void update(Contacto... contacts);

    @Delete
    public void delete(Contacto contact);

    @Query("SELECT * FROM contact")
    public LiveData<List<Contacto>> getContacts();

//    @Query("SELECT * FROM contact WHERE phoneNumber = :number")
    @Query("SELECT * FROM contact WHERE id=:Id")
    public LiveData<Contacto> getContactWithId(int Id);
}