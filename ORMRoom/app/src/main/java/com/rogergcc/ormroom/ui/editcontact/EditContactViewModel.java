package com.rogergcc.ormroom.ui.editcontact;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rogergcc.ormroom.entity.Contacto;
import com.rogergcc.ormroom.data.local.db.dao.ContactDAO;
import com.rogergcc.ormroom.data.local.db.ContantcsRoomDatabase;

/**
 * Created by rogergcc on 20/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class EditContactViewModel extends AndroidViewModel {
    private String TAG = this.getClass().getSimpleName();
    private ContactDAO noteDao;
    private ContantcsRoomDatabase db;


    public EditContactViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "Edit ViewModel");
        db = ContantcsRoomDatabase.getDatabase(application);
        noteDao = db.getContactDAO();
    }



    public LiveData<Contacto> getNote(int noteId) {
        return noteDao.getContactWithId(noteId);
    }
}
