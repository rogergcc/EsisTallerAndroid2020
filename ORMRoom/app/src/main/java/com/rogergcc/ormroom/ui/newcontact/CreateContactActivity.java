package com.rogergcc.ormroom.ui.newcontact;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.rogergcc.ormroom.R;
import com.rogergcc.ormroom.data.local.db.dao.ContactDAO;
import com.rogergcc.ormroom.entity.Contacto;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateContactActivity extends AppCompatActivity {

    @BindView(R.id.firstNameEditText)
    EditText firstNameEditText;
    @BindView(R.id.lastNameEditText)
    EditText lastNameEditText;
    @BindView(R.id.phoneNumberEditText)
    EditText phoneNumberEditText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;


    private ContactDAO mContactDAO;
    private ContactViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        ButterKnife.bind(this);

//        mContactDAO = Room.databaseBuilder(this, ContantcsRoomDatabase.class, "db-contacts")
//                .allowMainThreadQueries() //Allows room to do operation on main thread
//                .build()
//                .getContactDAO();
        noteViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

    }


    @OnClick(R.id.createContactFloatingActionButton)
    public void onViewClicked() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        if (firstName.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0) {
            Toast.makeText(CreateContactActivity.this, "Please make sure all details are correct", Toast.LENGTH_SHORT).show();
            return;
        }
        Contacto contact = new Contacto();
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setPhoneNumber(phoneNumber);
        contact.setCreatedDate(new Date());
        //Insert to database
        try {
            //mContactDAO.insert(contact);

            noteViewModel.insert(contact);
            setResult(RESULT_OK);
            finish();
        } catch (SQLiteConstraintException e) {
            Toast.makeText(CreateContactActivity.this, "A cotnact with same phone number already exists.", Toast.LENGTH_SHORT).show();
        }
    }
}
