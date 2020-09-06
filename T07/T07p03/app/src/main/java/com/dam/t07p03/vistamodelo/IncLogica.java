package com.dam.t07p03.vistamodelo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dam.t07p03.modelo.AppDatabase;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.DptoDao;
import com.dam.t07p03.modelo.Filtro;
import com.dam.t07p03.modelo.IncDao;
import com.dam.t07p03.modelo.Incidencia;
import com.dam.t07p03.vista.fragmentos.MtoIncsFragment;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class IncLogica {

    /* Métodos Lógica Dptos ***********************************************************************/

    private static class AsyncTask_opIncidencia extends AsyncTask<Incidencia, Void, Boolean> {
        private IncDao incDao;
        private String op;

        private AsyncTask_opIncidencia(IncDao incDao, String op) {
            this.incDao = incDao;
            this.op = op;
        }

        @Override
        protected Boolean doInBackground(Incidencia... incs) {
            try {
                Incidencia inc = incs[0];
                switch (op) {
                    case "existe":
                        inc = incDao.existe(incs[0].getId());
                        return (inc != null);
                    case "alta":
                        inc.setFecha(incs[0].getFecha());
                        incDao.insert(inc);
                        break;
                    case "editar":
                        inc.setFecha(incs[0].getFecha());
                        incDao.update(inc);
                        break;
                    case "baja":
                        incDao.delete(incs[0]);
                        break;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

    }

    public static boolean existeIncidencia(Context context, Incidencia inc) {
        try {
            IncDao incDao = AppDatabase.getAppDatabase(context).getIncDao();
            new AsyncTask_opIncidencia(incDao, "existe").execute(inc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean altaIncidencia(Context context, Incidencia inc) {
        try {
            IncDao incDao = AppDatabase.getAppDatabase(context).getIncDao();
            new AsyncTask_opIncidencia(incDao, "alta").execute(inc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean editarIncidencia(Context context, Incidencia inc) {
        try {
            IncDao incDao = AppDatabase.getAppDatabase(context).getIncDao();
            new AsyncTask_opIncidencia(incDao, "editar").execute(inc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean bajaIncidencia(Context context, Incidencia inc) {
        try {
            IncDao incDao = AppDatabase.getAppDatabase(context).getIncDao();
            new AsyncTask_opIncidencia(incDao, "baja").execute(inc);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static class AsyncTask_opIncidencias extends AsyncTask<String, Void, List<Incidencia>> {
        private IncDao incDao;
        private String op;

        private AsyncTask_opIncidencias(IncDao incDao, String op) {
            this.incDao = incDao;
            this.op = op;
        }

        @Override
        protected List<Incidencia> doInBackground(String... params) {
            try {
                switch (op) {
                    case "recuperarIncs":
                        return incDao.getAllNoLive();
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }

    public static List<Incidencia> recuperarIncidenciasNoLive(Context context) {
        try {
            IncDao incDao = AppDatabase.getAppDatabase(context).getIncDao();
            return new AsyncTask_opIncidencias(incDao, "recuperarIncs").execute().get();
        } catch (Exception e) {
            return null;
        }
    }

    public static LiveData<List<Incidencia>> recuperarIncidenciasFiltro(Context context, Filtro filtro) {
        try {
            IncDao incDao = AppDatabase.getAppDatabase(context).getIncDao();
            if (filtro.getEstado() == 2)
                return incDao.getTodasIndenciasFiltro(filtro.getIdDepto(), filtro.getFecha());
            else
                return incDao.getIndenciasFiltro(filtro.getIdDepto(), filtro.getEstado(), filtro.getFecha());
        } catch (Exception e) {
            return null;
        }
    }

    public static LiveData<List<Incidencia>> recuperarIncidencias(Context context, Departamento
            departamento) {
        IncDao incDao = AppDatabase.getAppDatabase(context).getIncDao();
        switch (departamento.getId()) {
            case 0:
                return incDao.getAll();
            default:
                return incDao.getIndenciasDepartamento(departamento.getId());
        }
    }

    public static Departamento recuperarDepartamento(Context context, Incidencia inc) {
        DptoDao dptoDao = AppDatabase.getAppDatabase(context).getDptoDao();
        try {
            return new AsyncTask_opRecuperarDepartamento(dptoDao, "recuperarDpto").execute(inc.getIdDpto()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class AsyncTask_opRecuperarDepartamento extends AsyncTask<Integer, Void, Departamento> {
        private DptoDao dptoDao;
        private String op;

        private AsyncTask_opRecuperarDepartamento(DptoDao dptoDao, String op) {
            this.dptoDao = dptoDao;
            this.op = op;
        }

        @Override
        protected Departamento doInBackground(Integer... params) {
            try {
                switch (op) {
                    case "recuperarDpto":
                        return dptoDao.existe(params[0]);
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }
}



