package com.dam.t07p03.vista.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Incidencia;
import com.dam.t07p03.vista.fragmentos.MtoIncsFragment;

import java.util.List;

public class AdaptadorIncs extends RecyclerView.Adapter<AdaptadorIncs.IncVH> {

    private List<Incidencia> mDatos;
    private int mItemPos;
    private View.OnClickListener mListener;

    public AdaptadorIncs() {
        mDatos = null;
        mItemPos = -1;
        mListener = null;
    }

    public void setDatos(List<Incidencia> datos) {
        mDatos = datos;
    }

    public int getItemPos() {
        return mItemPos;
    }

    public void setItemPos(int itemPos) {
        mItemPos = itemPos;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    public Incidencia getItem(int pos) {
        return mDatos.get(pos);
    }

    @NonNull
    @Override
    public AdaptadorIncs.IncVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rv_incs, parent, false);
        return new IncVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorIncs.IncVH holder, int position) {
        if (mDatos != null) {
            holder.setItem(mDatos.get(position));
            holder.itemView.setBackgroundColor((mItemPos == position)
                    ? holder.itemView.getContext().getResources().getColor(R.color.colorPrimary)
                    : Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        if (mDatos != null)
            return mDatos.size();
        else
            return 0;
    }

    public class IncVH extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView tvDpto;
        private TextView tvNdpto;
        private TextView tvIncId;
        private TextView tvIncFecha;
        private TextView tvIncTipoEstado;
        private TextView tvIncDescripcion;
        private View mItemView;

        public IncVH(@NonNull View itemView) {
            super(itemView);
            tvDpto = itemView.findViewById(R.id.tvDpto);
            tvNdpto = itemView.findViewById(R.id.tvIncNdpto);
            tvIncId = itemView.findViewById(R.id.tvIncId);
            tvIncFecha = itemView.findViewById(R.id.tvIncFecha);
            tvIncTipoEstado = itemView.findViewById(R.id.tvIncTipoEstado);
            tvIncDescripcion = itemView.findViewById(R.id.tvIncDescripcion);
            itemView.setOnClickListener(this);
            mItemView = itemView;
        }

        private void setItem(Incidencia inc) {
            tvDpto.setText("DPTO: " + inc.getIdDpto());
            tvNdpto.setText("Nombre Dpto: "+inc.getDptoNombre());
            tvIncId.setText("ID: " + inc.getId());
            tvIncFecha.setText("Fecha: " + MtoIncsFragment.date2string(inc.getFecha()));
            if (inc.getEstado() == 1)
                tvIncTipoEstado.setText("Tipo: " + inc.getTipo() + " Resuelta");
            else
                tvIncTipoEstado.setText("Tipo: " + inc.getTipo() + " No Resuelta");
            tvIncDescripcion.setText("Descripción: " + inc.getDescipcion());
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            notifyItemChanged(mItemPos);
            mItemPos = (mItemPos == pos) ? -1 : pos;
            notifyItemChanged(mItemPos);
            if (mListener != null)
                mListener.onClick(v);
        }
    }

}

