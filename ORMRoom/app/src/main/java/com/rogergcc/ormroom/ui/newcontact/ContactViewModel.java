package com.rogergcc.ormroom.ui.newcontact;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.rogergcc.ormroom.entity.Contacto;
import com.rogergcc.ormroom.data.local.db.dao.ContactDAO;
import com.rogergcc.ormroom.data.local.db.ContantcsRoomDatabase;

import java.util.List;

/**
 * Created by rogergcc on 20/02/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class ContactViewModel extends AndroidViewModel {

    private String TAG = this.getClass().getSimpleName();
    private ContactDAO noteDao;
    private ContantcsRoomDatabase noteDB;
    private LiveData<List<Contacto>> mAllNotes;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        noteDB = ContantcsRoomDatabase.getDatabase(application);
        noteDao = noteDB.getContactDAO();
        mAllNotes = noteDao.getContacts();
    }
    public void insert(Contacto note) {
        new InsertAsyncTask(noteDao).execute(note);
    }

    public LiveData<Contacto> getNote(int noteId) {
        return noteDao.getContactWithId(noteId);
    }

    public LiveData<List<Contacto>> getAllNotes() {
        return mAllNotes;
    }

    public void update(Contacto note) {
        new UpdateAsyncTask(noteDao).execute(note);
    }

    public void delete(Contacto note) {
        new DeleteAsyncTask(noteDao).execute(note);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }

    private class OperationsAsyncTask extends AsyncTask<Contacto, Void, Void> {

        ContactDAO mAsyncTaskDao;

        OperationsAsyncTask(ContactDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Contacto... notes) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(ContactDAO mNoteDao) {
            super(mNoteDao);
        }

        @Override
        protected Void doInBackground(Contacto... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends OperationsAsyncTask {

        UpdateAsyncTask(ContactDAO noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(Contacto... notes) {
            mAsyncTaskDao.update(notes[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationsAsyncTask {

        public DeleteAsyncTask(ContactDAO noteDao) {
            super(noteDao);
        }

        @Override
        protected Void doInBackground(Contacto... notes) {
            mAsyncTaskDao.delete(notes[0]);
            return null;
        }
    }

}

