package com.rogergcc.ormroom.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rogergcc.ormroom.R;
import com.rogergcc.ormroom.ui.adapter.AdapterContacto;
import com.rogergcc.ormroom.entity.Contacto;
import com.rogergcc.ormroom.ui.newcontact.ContactViewModel;
import com.rogergcc.ormroom.data.local.db.ContantcsRoomDatabase;
import com.rogergcc.ormroom.data.local.db.dao.ContactDAO;
import com.rogergcc.ormroom.ui.newcontact.CreateContactActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mAddContactFloatingActionButton;
    private static final int RC_CREATE_CONTACT = 1;
    private static final int RC_UPDATE_CONTACT = 2;
    private static final int RC_DELETE_CONTACT = 3;

    private ContactDAO mContactDAO;


    private RecyclerView mContactsRecyclerView;
    private AdapterContacto mContactRecyclerAdapter;
    private List<Contacto> misdatos;

    private ContantcsRoomDatabase db;
    private ContactViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        mContactDAO = Room.databaseBuilder(this, ContantcsRoomDatabase.class, "db-contacts")
//                .allowMainThreadQueries() //Allows room to do operation on main thread
//                .build()
//                .getContactDAO();

//        db = ContantcsRoomDatabase.getDatabase(getApplicationContext());
//        mContactDAO = db.getContactDAO();

        mAddContactFloatingActionButton = findViewById(R.id.addContactFloatingActionButton);
        mAddContactFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateContactActivity.class);
                startActivityForResult(intent, RC_CREATE_CONTACT);
            }
        });


        mContactsRecyclerView = findViewById(R.id.contactsRecyclerView);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int colors[] = {ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, android.R.color.holo_red_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_blue_dark),
                ContextCompat.getColor(this, android.R.color.holo_purple)};
        mContactRecyclerAdapter = new AdapterContacto(this, new ArrayList<Contacto>(), colors);
        mContactsRecyclerView.setAdapter(mContactRecyclerAdapter);

        noteViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Contacto>>() {
            @Override
            public void onChanged(@Nullable List<Contacto> notes) {
                mContactRecyclerAdapter.setNotes(notes);
            }
        });

        loadContacts();

    }

    private void loadContacts() {
//        misdatos = new ArrayList<>();
//        misdatos.add( new Contacto("JUAN", "Ticona", "952002434"));
//        misdatos.add( new Contacto("WILSON", "Maquera", "954350345"));
//        misdatos.add(new Contacto("ADRIAN", "Mamani", "943545453"));
//        misdatos.add( new Contacto("CARLA", "Fuentes", "954543534"));
//        misdatos.add(new Contacto("Delia", "Caceres", "969558585"));
//        mContactRecyclerAdapter.updateData(misdatos);
//        mContactRecyclerAdapter.updateData(mContactDAO.getContacts());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int isDelete = -1;
        if (data != null) {
            isDelete = data.getIntExtra("cod_delete", -1);
        }


        if (requestCode == RC_CREATE_CONTACT && resultCode == RESULT_OK) {
//            loadContacts();
            Toast.makeText(
                    getApplicationContext(),
                    R.string.saved,
                    Toast.LENGTH_LONG).show();

        }
        if (requestCode == RC_UPDATE_CONTACT && resultCode == RESULT_OK) {

//            Contacto contacto = new Contacto(
//                    "dato f",
//                    "fasf",
//                    "422",
//                    new Date()
//
//            );
//
            if (isDelete == RC_DELETE_CONTACT)
                Toast.makeText(getApplicationContext(),R.string.deleted,Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),R.string.updated,Toast.LENGTH_LONG).show();

        }

    }

}
