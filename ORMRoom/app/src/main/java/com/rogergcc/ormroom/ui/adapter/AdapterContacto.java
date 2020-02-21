package com.rogergcc.ormroom.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rogergcc.ormroom.ui.MainActivity;
import com.rogergcc.ormroom.R;
import com.rogergcc.ormroom.ui.editcontact.EditContactActivity;
import com.rogergcc.ormroom.entity.Contacto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ROGERGCC on 18/02/2020.
 */
public class AdapterContacto extends RecyclerView.Adapter<AdapterContacto.ViewHolder> {

    private LayoutInflater inflador; List<Contacto> contactList;
    Context micontext; private int[] colors;
    public AdapterContacto(Context context, ArrayList<Contacto> contactList, int[] colors) {
        this.contactList= contactList; this.micontext=context; this.colors = colors;
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.item_contacto, parent, false);return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        Contacto contact = contactList.get(i);
        String fullName = contact.getFirstName() + " " + contact.getLastName();
        holder.mNameTextView.setText(fullName);
        String initial = contact.getFirstName().toUpperCase().substring(0, 1);
        holder.mInitialsTextView.setText(initial);
        holder.mInitialsBackground.setColor(colors[i % colors.length]);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(micontext, EditContactActivity.class);
                intent.putExtra("contact_id", contact.getId());
                ((MainActivity)micontext).startActivityForResult(intent, 2);
            }
        });
    }


    public void setNotes(List<Contacto> notes) {
        contactList = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return contactList.size(); }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;private TextView mInitialsTextView;private GradientDrawable mInitialsBackground;
        ViewHolder(View itemView) {
            super(itemView);
            mInitialsTextView = itemView.findViewById(R.id.initialsTextView);
            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mInitialsBackground = (GradientDrawable) mInitialsTextView.getBackground();
        }
    }
    public void updateData(List<Contacto> contacts) {
        this.contactList = contacts; notifyDataSetChanged();
    }
}