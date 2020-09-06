package com.dam.adaptadores;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.modelo.Alumno;
import com.dam.t03p02.R;

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
            implements View.OnClickListener {

        private LinearLayout llAlumnoItem;
        private TextView txDni, txNombre, txFecNac,txCiclo;

        public AlumnoViewHolder(@NonNull View itemView) {
            super(itemView);
            llAlumnoItem = itemView.findViewById(R.id.llAlumnoItem);
            txDni = itemView.findViewById(R.id.txDni);
            txNombre = itemView.findViewById(R.id.txNombre);
            txFecNac = itemView.findViewById(R.id.txFecNac);
            txCiclo = itemView.findViewById(R.id.txCiclo);
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
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            if (mItemPos == -1 || mItemPos != pos) {
                mItemPos = pos;
                for (int i = 0; i < ((RecyclerView) v.getParent()).getChildCount(); i++) {
                    LinearLayout ll = ((RecyclerView) v.getParent()).getChildAt(i).findViewById(R.id.llAlumnoItem);
                    ll.setBackgroundColor(Color.TRANSPARENT);
                }
                llAlumnoItem.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorAccent));
            } else {
                mItemPos = -1;
                llAlumnoItem.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

}
