package com.dam.adaptadores;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.modelo.Alumno;
import com.dam.t03p03.R;

import java.util.List;


public class AdaptadorAlumnosRv extends RecyclerView.Adapter<AdaptadorAlumnosRv.AlumnoViewHolder> {

    private List<Alumno> mDatos;
    private int mItemPos;


    public AdaptadorAlumnosRv(List<Alumno> datos) {
        mDatos = datos;
        mItemPos = -1;

    }

    public int getItemPos() {
        return mItemPos;
    }

    public void setItemPos(int mItemPos) {
        this.mItemPos = mItemPos;
    }

    @NonNull
    @Override
    public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_alumnos, parent, false);
        return new AlumnoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
        holder.setItem(mDatos.get(position));
        holder.llAlumnoItem.setBackgroundColor((mItemPos == position) ? Color.YELLOW : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    public class AlumnoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        private LinearLayout llAlumnoItem;
        private TextView txDni, txNombre, txFecNac, txCiclo;
        private ImageView ivFoto;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            llAlumnoItem = itemView.findViewById(R.id.llAlumnoItem);
            txDni = itemView.findViewById(R.id.txDni);
            txNombre = itemView.findViewById(R.id.txNombre);
            txFecNac = itemView.findViewById(R.id.txFecNac);
            txCiclo = itemView.findViewById(R.id.txCiclo);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            llAlumnoItem.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        private void setItem(Alumno a) {
            String dato1 = "DNI: " + a.getDni();
            txDni.setText(dato1);
            String dato2 = "Nombre: " + a.getNombre();
            txNombre.setText(dato2);
            String dato3 = "Fecha Nacimiento: " + a.getFecNac();
            txFecNac.setText(dato3);
            String dato4 = "Ciclo: " + a.getCiclo();
            txCiclo.setText(dato4);
            Bitmap dato5 = a.getImagen();
            ivFoto.setImageBitmap(dato5);
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            notifyItemChanged(mItemPos);
            if (mItemPos == -1 || mItemPos != pos) {
                mItemPos = pos;
            } else {
                mItemPos = -1;
            }
            notifyItemChanged(mItemPos);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle(R.string.alumnos);
            menu.add(Menu.NONE, 0, 0, R.string.editar);
            menu.add(Menu.NONE, 1, 0, R.string.baja);
            menu.add(Menu.NONE, 2, 0, R.string.tomar_foto);
        }
    }

}
