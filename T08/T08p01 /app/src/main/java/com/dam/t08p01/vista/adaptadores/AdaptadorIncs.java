package com.dam.t08p01.vista.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.t08p01.R;
import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.modelo.Incidencia;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class AdaptadorIncs extends RecyclerView.Adapter<AdaptadorIncs.IncVH> {

    private List<Incidencia> mDatos;
    private int mItemPos;
    private View.OnClickListener mListener;
    private Bitmap foto;

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
    public IncVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rv_incs, parent, false);
        return new IncVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IncVH holder, int position) {
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
        private ImageView ivFoto;
        private TextView tvDpto;
        private TextView tvIncDptoIdHora;
        private TextView tvIncFecha;
        private TextView tvIncTipoEstado;
        private TextView tvIncDescripcion;
        private View mItemView;

        public IncVH(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivInc);
            tvDpto = itemView.findViewById(R.id.tvIncDpto);
            tvIncDptoIdHora = itemView.findViewById(R.id.tvIncDptoIdHora);
            tvIncFecha = itemView.findViewById(R.id.tvIncFecha);
            tvIncTipoEstado = itemView.findViewById(R.id.tvIncTipoEstado);
            tvIncDescripcion = itemView.findViewById(R.id.tvIncDescripcion);
            itemView.setOnClickListener(this);
            mItemView = itemView;
        }

        private void setItem(Incidencia inc) {
            if(!inc.getFoto().equals("")) {
                Bitmap foto = decodeFromFirebaseBase64(inc.getFoto());
                ivFoto.setImageBitmap(foto);
            }else{
                ivFoto.setImageResource(R.drawable.ic_info_foreground);
            }
            tvDpto.setText("DPTO: " + inc.getDeptoNombre());
            tvIncDptoIdHora.setText("ID: " + inc.getId());
            tvIncFecha.setText("Fecha: " + inc.getFecha());
            if (inc.getEstado() == 1)
                tvIncTipoEstado.setText("Tipo: "+inc.getTipo()+" Resuelta");
            else
                tvIncTipoEstado.setText("Tipo: "+inc.getTipo()+" No Resuelta");
            tvIncDescripcion.setText("Descripción:" + inc.getDescripcion());

        }

        public Bitmap decodeFromFirebaseBase64(String image)  {
            byte[] decodedByteArray = android.util.Base64.decode(image, 0);
            return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
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

