package com.dam.t08p01.vista.fragmentos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.t08p01.R;
import com.dam.t08p01.modelo.Departamento;
import com.dam.t08p01.modelo.Filtro;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.modelo.Incidencia;
import com.dam.t08p01.repositorio.LocGoogle;
import com.dam.t08p01.vista.IncsActivity;
import com.dam.t08p01.vista.adaptadores.AdaptadorDptos;
import com.dam.t08p01.vista.adaptadores.AdaptadorIncs;
import com.dam.t08p01.vistamodelo.DptosViewModel;
import com.dam.t08p01.vistamodelo.IncsViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BusIncsFragment extends Fragment {

    private RecyclerView rvIncs;
    private NavController mNavC;
    private AdaptadorIncs mAdaptadorIncs;
    private Button btEliminar, btEditar, btCrear;
    private Departamento mLogin;
    public static boolean filtro;
    public static final String TAG = "BusIncsFragment";
    private BusIncsFragInterface mListener;

    public interface BusIncsFragInterface {
        void onCrearBusIncsFrag();

        void onEditarBusIncsFrag(Incidencia inc);

        void onEliminarBusIncsFrag(Incidencia inc);
    }

    public BusIncsFragment() {
        // Required empty public constructor
    }

    public static BusIncsFragment newInstance(Bundle arguments) {
        BusIncsFragment frag = new BusIncsFragment();
        if (arguments != null) {
            frag.setArguments(arguments);
        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BusIncsFragInterface) {
            mListener = (BusIncsFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BusIncsFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Inits
        mNavC = Navigation.findNavController(getActivity(), R.id.navhostfrag_incs);
        IncsViewModel incsVM = new ViewModelProvider(requireActivity()).get(IncsViewModel.class);
        mLogin = incsVM.getLogin();    // Recuperamos el login del ViewModel
        mAdaptadorIncs = new AdaptadorIncs();


        if (mLogin != null) {
            if(!IncsViewModel.filtroBoolean) {
                // Inits Incs Observer
                incsVM.getIncsME(mLogin).observe(this, new Observer<List<Incidencia>>() {
                    @Override
                    public void onChanged(List<Incidencia> incs) {
                        if(incs.size()==0) {
                            Snackbar.make(getView(), R.string.incs_ko, Snackbar.LENGTH_SHORT).show();
                        } else {
                            mAdaptadorIncs.setDatos(incs);
                            mAdaptadorIncs.notifyDataSetChanged();
                            if (mAdaptadorIncs.getItemPos() != -1 &&
                                    mAdaptadorIncs.getItemPos() < mAdaptadorIncs.getItemCount())
                                rvIncs.scrollToPosition(mAdaptadorIncs.getItemPos());
                            else if (mAdaptadorIncs.getItemCount() > 0)
                                rvIncs.scrollToPosition(mAdaptadorIncs.getItemCount() - 1);
                            mAdaptadorIncs.setItemPos(-1);
                            btEliminar.setEnabled(false);
                            btEditar.setEnabled(false);
                            btCrear.setEnabled(true);
                        }
                    }
                });
            }else{
                Filtro filtro=incsVM.getmFiltro();
                // Inits Incs Observer
                incsVM.getIncsSE(filtro).observe(this, new Observer<List<Incidencia>>() {
                    @Override
                    public void onChanged(List<Incidencia> incs) {
                        if(incs.size()==0) {
                            Snackbar.make(getView(), R.string.buscar_ko, Snackbar.LENGTH_SHORT).show();
                        } else {
                            mAdaptadorIncs.setDatos(incs);
                            mAdaptadorIncs.notifyDataSetChanged();
                            if (mAdaptadorIncs.getItemPos() != -1 &&
                                    mAdaptadorIncs.getItemPos() < mAdaptadorIncs.getItemCount())
                                rvIncs.scrollToPosition(mAdaptadorIncs.getItemPos());
                            else if (mAdaptadorIncs.getItemCount() > 0)
                                rvIncs.scrollToPosition(mAdaptadorIncs.getItemCount() - 1);
                            mAdaptadorIncs.setItemPos(-1);
                            btEliminar.setEnabled(false);
                            btEditar.setEnabled(false);
                            btCrear.setEnabled(true);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bus_incs, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Bundle b;
        switch (item.getItemId()) {
            case R.id.menuFiltro:
                // Lanzo frag. Filtrar
                b = new Bundle();
                b.putParcelable("dpto", mLogin);
                b.putInt("op",2);
                mNavC.navigateUp();
                mNavC.navigate(R.id.action_busIncsFragment_to_filtroFragment, b);
                return true;
            case R.id.menuMapa:
                // Lanzo frag. Mapa
                mNavC.navigateUp();
                b = new Bundle();
                b.putParcelable("dpto", mLogin);
                mNavC.navigate(R.id.action_busIncsFragment_to_mapIncsFragment,b);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus_incs, container, false);

        // FindViewByIds
        rvIncs = v.findViewById(R.id.rvIncs);
        btEliminar = v.findViewById(R.id.btEliminar);
        btEditar = v.findViewById(R.id.btEditar);
        btCrear = v.findViewById(R.id.btCrear);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inits
        if (mAdaptadorIncs.getItemPos() != -1) {
            btEliminar.setEnabled(true);
            btEditar.setEnabled(true);
            btCrear.setEnabled(false);
        } else {
            btEliminar.setEnabled(false);
            btEditar.setEnabled(false);
            btCrear.setEnabled(true);
        }

        // Init RecyclerView Incs
        rvIncs.setHasFixedSize(true);
        rvIncs.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvIncs.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        ItemTouchHelper ith = new ItemTouchHelper(ithSimpleCallback);
        ith.attachToRecyclerView(rvIncs);
        rvIncs.setAdapter(mAdaptadorIncs);

        // Listeners
        btCrear.setOnClickListener(btCrear_OnClickListener);
        btEditar.setOnClickListener(btEditar_OnClickListener);
        btEliminar.setOnClickListener(btEliminar_OnClickListener);
        mAdaptadorIncs.setOnClickListener(mAdaptadorIncs_OnClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IncsViewModel incsVM = new ViewModelProvider(requireActivity()).get(IncsViewModel.class);
        incsVM.eliminarEventosGetIncsME();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private View.OnClickListener mAdaptadorIncs_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos != -1) {
                btEliminar.setEnabled(true);
                btEditar.setEnabled(true);
                btCrear.setEnabled(false);
            } else {
                btEliminar.setEnabled(false);
                btEditar.setEnabled(false);
                btCrear.setEnabled(true);
            }
        }
    };

    private View.OnClickListener btCrear_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos == -1) {
                if (mListener != null)
                    mListener.onCrearBusIncsFrag();
            }
        }
    };

    private View.OnClickListener btEditar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos >= 0) {
                if (mListener != null)
                    mListener.onEditarBusIncsFrag(mAdaptadorIncs.getItem(pos));
            }
        }
    };

    private View.OnClickListener btEliminar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos >= 0) {
                if (mListener != null)
                    mListener.onEliminarBusIncsFrag(mAdaptadorIncs.getItem(pos));
            }
        }
    };

    private ItemTouchHelper.SimpleCallback ithSimpleCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    mAdaptadorIncs.setItemPos(viewHolder.getAdapterPosition());
                    if (direction == ItemTouchHelper.LEFT) { // eliminar
                        btEliminar.callOnClick();
                    } else if (direction == ItemTouchHelper.RIGHT) { // editar
                        btEditar.callOnClick();
                    }
                    viewHolder.itemView.callOnClick();
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                        View itemView = viewHolder.itemView;
                        float height = (float) itemView.getBottom() - (float) itemView.getTop();
                        float width = height / 3;
                        Paint p = new Paint();
                        p.setColor(getResources().getColor(R.color.colorPrimary));
                        if (dX > 0) { // editar
                            RectF background = new RectF((float) itemView.getLeft() + 5, (float) itemView.getTop() + 10, dX - 5, (float) itemView.getBottom() - 10);
                            c.drawRect(background, p);
                            Bitmap icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_edit);
                            RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        } else if (dX < 0) { // eliminar
                            RectF background = new RectF((float) itemView.getRight() + dX + 5, (float) itemView.getTop() + 10, (float) itemView.getRight() - 5, (float) itemView.getBottom() - 10);
                            p.setColor(getResources().getColor(R.color.colorPrimary));
                            c.drawRect(background, p);
                            Bitmap icon = BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_menu_delete);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                }
            };

}

