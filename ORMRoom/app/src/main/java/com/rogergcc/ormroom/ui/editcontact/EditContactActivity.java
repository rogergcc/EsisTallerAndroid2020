package com.rogergcc.ormroom.ui.editcontact;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rogergcc.ormroom.R;
import com.rogergcc.ormroom.data.local.db.dao.ContactDAO;
import com.rogergcc.ormroom.entity.Contacto;
import com.rogergcc.ormroom.ui.newcontact.ContactViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditContactActivity extends AppCompatActivity {

    public static String EXTRA_CONTACT_ID = "contact_id";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.createdTimeTextView)
    TextView createdTimeTextView;
    @BindView(R.id.firstNameEditText)
    EditText firstNameEditText;
    @BindView(R.id.lastNameEditText)
    EditText lastNameEditText;
    @BindView(R.id.phoneNumberEditText)
    EditText phoneNumberEditText;
    @BindView(R.id.updateButton)
    Button updateButton;
    @BindView(R.id.editContactFloatingActionButton)
    FloatingActionButton editContactFloatingActionButton;
    private TextView mCreatedTimeTextView;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mPhoneNumberEditText;
    private Button mUpdateButton;
    private Toolbar mToolbar;
    private Contacto CONTACT;

    private ContactDAO mContactDAO;

    private Bundle bundle;
    private int contactId;

    private LiveData<Contacto> note;

    EditContactViewModel noteModel;
    ContactViewModel contactViewModel;
    static final String UPDATED_NAME = "contact_name";
    static final String UPDATED_LASTNAME = "contact_lastname";
    static final String UPDATED_PHONE = "contact_phone";
    static final String UPDATED_DATE = "contact_date";

    boolean dialogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        ButterKnife.bind(this);

//        mContactDAO = Room.databaseBuilder(this, ContantcsRoomDatabase.class, "db-contacts")
//                .allowMainThreadQueries() //Allows room to do operation on main thread
//                .build()
//                .getContactDAO();

        //CONTACT = mContactDAO.getContactWithId(getIntent().getStringExtra(EXTRA_CONTACT_ID));

        mFirstNameEditText = findViewById(R.id.firstNameEditText);
        mLastNameEditText = findViewById(R.id.lastNameEditText);
        mPhoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        mUpdateButton = findViewById(R.id.updateButton);
        mCreatedTimeTextView = findViewById(R.id.createdTimeTextView);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        bundle = getIntent().getExtras();

        if (bundle != null) {
            contactId = bundle.getInt("contact_id");
        }
        initViews();

    }

    private void initViews() {
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

//        noteModel = ViewModelProviders.of(this).get(EditContactViewModel.class);

        note = contactViewModel.getNote(contactId);
        note.observe(this, new Observer<Contacto>() {

            @Override
            public void onChanged(@Nullable Contacto contact) {
                if (contact != null) {
                    CONTACT = contact;

                    mFirstNameEditText.setText(contact.getFirstName());
                    mLastNameEditText.setText(contact.getLastName());
                    mPhoneNumberEditText.setText(contact.getPhoneNumber());
                    mCreatedTimeTextView.setText(contact.getCreatedDate().toString());

                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete: {
//                mContactDAO.delete(CONTACT);
                if (!dialogShown) {
                    dialogShown = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditContactActivity.this, R.style.AppTheme_DialogLight);
                    builder.setTitle("Eliminar Contacto").setMessage("¿Esta seguro que desea eliminar el contacto?")

                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    contactViewModel.delete(CONTACT);
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("cod_delete", 3);
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogShown = false;
                                    dialogInterface.cancel();
                                }
                            });
                    builder.setCancelable(false);
                    builder.create().show();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void click_actualizar(View view) {
        String firstName = mFirstNameEditText.getText().toString();
        String lastName = mLastNameEditText.getText().toString();
        String phoneNumber = mPhoneNumberEditText.getText().toString();
        if (firstName.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0) {
            Toast.makeText(EditContactActivity.this, "Please make sure all details are correct", Toast.LENGTH_SHORT).show();
            return;
        }
//        Contacto contact = new Contacto();
//        contact.setCreatedDate(CONTACT.getCreatedDate());
//        contact.setFirstName(CONTACT.getFirstName());
//        contact.setLastName(CONTACT.getLastName());
//        contact.setPhoneNumber(CONTACT.getPhoneNumber());

        CONTACT.setFirstName(firstName);
        CONTACT.setLastName(lastName);
        CONTACT.setPhoneNumber(phoneNumber);
//Insert to database

        try {
            contactViewModel.update(CONTACT);

//                    String mirstNameEditText = mFirstNameEditText.getText().toString();
//                    String LastNameEditText = mLastNameEditText.getText().toString();
//                    String PhoneNumberEditText = mPhoneNumberEditText.getText().toString();
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra(EXTRA_CONTACT_ID, contactId);
//                    resultIntent.putExtra(UPDATED_NAME, firstName);
//                    resultIntent.putExtra(UPDATED_LASTNAME, lastName);
//                    resultIntent.putExtra(UPDATED_PHONE, phoneNumber);
//                    resultIntent.putExtra(UPDATED_DATE, mCreatedTimeTextView.getText().toString());
//                    setResult(RESULT_OK, resultIntent);

//                mContactDAO.update(CONTACT);
            setResult(RESULT_OK);
            finish();
        } catch (SQLiteConstraintException e) {
            Toast.makeText(EditContactActivity.this, "A cotnact with same phone number already exists.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.editContactFloatingActionButton)
    public void onViewClicked() {
        String firstName = mFirstNameEditText.getText().toString();
        String lastName = mLastNameEditText.getText().toString();
        String phoneNumber = mPhoneNumberEditText.getText().toString();
        if (firstName.length() == 0 || lastName.length() == 0 || phoneNumber.length() == 0) {
            Toast.makeText(EditContactActivity.this, "Please make sure all details are correct", Toast.LENGTH_SHORT).show();
            return;
        }

        CONTACT.setFirstName(firstName);
        CONTACT.setLastName(lastName);
        CONTACT.setPhoneNumber(phoneNumber);
//Insert to database

        try {
            contactViewModel.update(CONTACT);


            setResult(RESULT_OK);
            finish();
        } catch (SQLiteConstraintException e) {
            Toast.makeText(EditContactActivity.this, "A contact with same phone number already exists.", Toast.LENGTH_SHORT).show();
        }
    }
}
