package com.rogergcc.ormroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rogergcc.ormroom.adapter.AdapterContacto;
import com.rogergcc.ormroom.model.Contacto;
import com.rogergcc.ormroom.roomdb.AppDataBase;
import com.rogergcc.ormroom.roomdb.ContactDAO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mAddContactFloatingActionButton;
    private static final int RC_CREATE_CONTACT = 1;
    private static final int RC_UPDATE_CONTACT = 2;

    private ContactDAO mContactDAO;



    private RecyclerView mContactsRecyclerView;
    private AdapterContacto mContactRecyclerAdapter;
    private List<Contacto> misdatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContactDAO = Room.databaseBuilder(this, AppDataBase.class, "db-contacts")
                .allowMainThreadQueries() //Allows room to do operation on main thread
                .build()
                .getContactDAO();

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
        mContactRecyclerAdapter.updateData(mContactDAO.getContacts());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CREATE_CONTACT && resultCode == RESULT_OK) {
loadContacts();
        } else if (requestCode == RC_UPDATE_CONTACT && resultCode == RESULT_OK) {
 loadContacts();
        }
    }

}
