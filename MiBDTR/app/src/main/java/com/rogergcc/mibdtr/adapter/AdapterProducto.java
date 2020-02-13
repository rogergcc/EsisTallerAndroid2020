package com.rogergcc.mibdtr.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rogergcc.mibdtr.DetalleProductActivity;
import com.rogergcc.mibdtr.R;
import com.rogergcc.mibdtr.model.Producto;

import java.util.ArrayList;

/**
 * Created by ROGERGCC on 13/02/2020.
 */
public class AdapterProducto extends RecyclerView.Adapter<AdapterProducto.ViewHolder> {
    private LayoutInflater inflador; ArrayList<Producto> datos; Context micontext;
    public AdapterProducto(Context context, ArrayList<Producto> datos) {
        this.datos= datos;
        micontext=context;
        inflador = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
//        holder.miid.setText(datos.get(i).getId());
        holder.miid.setText("$" + datos.get(i).getPrecio());
        holder.nombreproducto.setText(datos.get(i).getNombre());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(micontext, DetalleProductActivity.class);
                intent.putExtra("miid",datos.get(i).getId());
                intent.putExtra("nombreproducto",datos.get(i).getNombre());
            }
        });

        holder.btn_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("PRODUCTOS");


                AlertDialog.Builder alertbox = new AlertDialog.Builder(micontext,R.style.MyAlertDialogStyle);
                alertbox.setMessage("Borrar Registro");
                alertbox.setTitle("Accion");
                alertbox.setIcon(R.drawable.common_google_signin_btn_icon_dark_focused);

                alertbox.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                reference.child(datos.get(i).getId()).removeValue();

                                Toast.makeText(micontext, "Item Borrado", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertbox.show();

            }
        });

    }
    @Override
    public int getItemCount() {
        return datos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView miid, nombreproducto;
        public Button btn_borrar;
        ViewHolder(View itemView) {
            super(itemView);
            miid = (TextView)itemView.findViewById(R.id.id_text);
            nombreproducto = (TextView)itemView.findViewById(R.id.nombre);
            btn_borrar= itemView.findViewById(R.id.btnDelete);
        }
    }
}