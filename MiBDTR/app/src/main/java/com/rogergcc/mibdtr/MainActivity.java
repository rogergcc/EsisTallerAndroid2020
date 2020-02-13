package com.rogergcc.mibdtr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.rogergcc.mibdtr.adapter.AdapterProducto;
import com.rogergcc.mibdtr.model.Producto;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.btnSave)
    Button btnSave;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterProducto adaptador;
    private ArrayList<Producto> misdatos;

    private static final String PATH_PRODUCTO = "PRODUCTOS";
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.recycler_view);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(PATH_PRODUCTO);


        misdatos = new ArrayList<>();
//        misdatos.add( new Producto("123", "Camisa", "200"));
//        misdatos.add( new Producto("432", "Polo", "400"));
//        misdatos.add(new Producto("5674", "Jean", "300"));
//        misdatos.add( new Producto("345", "Zapato", "150"));
//        misdatos.add(new Producto("678", "Chompa", "350"));
        adaptador = new AdapterProducto(this,
                misdatos);
        recyclerView.setAdapter(adaptador);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AddItems();
    }

    @OnClick(R.id.btnSave)
    public void onViewClicked() {
        Toast.makeText(this,"test",Toast.LENGTH_LONG).show();
        Producto producto = new Producto(etName.getText().toString().trim(),
                etPrice.getText().toString().trim());
        // reference.push().setValue(producto);

        Producto productoupdate= getProducto(producto.getNombre());
        if(productoupdate != null){
            reference.child(productoupdate.getId()).setValue(producto);
        } else {
            reference.push().setValue(producto);
        }
        etName.setText("");
        etPrice.setText("");

    }
    Producto getProducto(String nombre){
        for (Producto prod : misdatos) {
            if (prod.getNombre().equals(nombre)){
                return prod;
            }
        }
        return null;
    }

    void AddItems(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Producto producto = dataSnapshot.getValue(Producto.class);
                producto.setId(dataSnapshot.getKey());
                if (!misdatos.contains(producto)) {
                    misdatos.add(producto);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Producto producto = dataSnapshot.getValue(Producto.class);
                producto.setId(dataSnapshot.getKey()); int index=-1;
                for (Producto prod : misdatos) {
                    Log.i("iteracion", prod.getId() + " = " + producto.getId());
                    index++;
                    if (prod.getId().equals(producto.getId())) {
                        misdatos.set(index, producto); break;
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Producto producto = dataSnapshot.getValue(Producto.class);
                producto.setId(dataSnapshot.getKey()); int index=-1;
                for (Producto prod : misdatos) {
                    Log.i("iteracion", prod.getId() + " = " + producto.getId());
                    index++;
                    if (prod.getId().equals(producto.getId())) {
                        misdatos.remove(index); break;
                    }
                }
                recyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(MainActivity.this,"Se movio el producto",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"Transaccion cancelada",Toast.LENGTH_LONG).show();
            }
        });
    }

}
