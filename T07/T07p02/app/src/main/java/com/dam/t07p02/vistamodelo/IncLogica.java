package com.dam.t07p02.vistamodelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

import com.dam.t07p02.R;
import com.dam.t07p02.modelo.AppDatabase;
import com.dam.t07p02.modelo.Departamento;
import com.dam.t07p02.modelo.Filtro;
import com.dam.t07p02.modelo.Incidencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class IncLogica {

    /* Métodos Lógica Incs ***********************************************************************/

    private static class AsyncTask_opIncidencia extends AsyncTask<Incidencia, Void, Boolean> {
        private Connection con;
        private String op;

        private AsyncTask_opIncidencia(Connection con, String op) {
            this.con = con;
            this.op = op;
        }

        @Override
        protected Boolean doInBackground(Incidencia... incs) {
            String sql = "";
            boolean errorSQL = false;
            switch (op) {
                case "existe":
                    sql = "SELECT * FROM Incidencias WHERE id=? LIMIT 1";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, incs[0].getId());
                        ResultSet rs = pst.executeQuery();
                        return (rs.next());
                    } catch (Exception e) {
                        errorSQL = true;
                    }
                    break;
                case "alta":
                    sql = "INSERT INTO Incidencias VALUES (?,?,?,?,?,?,?)";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, incs[0].getIdDpto());
                        pst.setString(2, incs[0].getId());
                        pst.setString(3, incs[0].getFecha());
                        pst.setString(4, incs[0].getDescripcion());
                        pst.setString(5, incs[0].getTipo());
                        pst.setBoolean(6, incs[0].isEstado());
                        pst.setString(7, incs[0].getResolucion());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        errorSQL = true;
                    }
                    break;
                case "editar":
                    sql = "UPDATE Incidencias SET descripcion=?, tipo=? ,estado=?, resolucion=? WHERE id=?";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, incs[0].getDescripcion());
                        pst.setString(2, incs[0].getTipo());
                        pst.setBoolean(3, incs[0].isEstado());
                        pst.setString(4, incs[0].getResolucion());
                        pst.setString(5, incs[0].getId());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        errorSQL = true;
                    }
                    break;
                case "baja":
                    sql = "DELETE FROM Incidencias WHERE id=?";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, incs[0].getId());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        errorSQL = true;
                    }
                    break;
            }
            return !errorSQL;
        }
    }

    public static boolean existeIncidencia(Context context, Incidencia incidencia) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opIncidencia(AppDatabase.getAppDatabase(context).getCon(), "existe").execute(incidencia).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean altaIncidencia(Context context, Incidencia incidencia) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opIncidencia(AppDatabase.getAppDatabase(context).getCon(), "alta").execute(incidencia).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean editarIncidencia(Context context, Incidencia incidencia) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opIncidencia(AppDatabase.getAppDatabase(context).getCon(), "editar").execute(incidencia).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean bajaIncidencia(Context context, Incidencia incidencia) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opIncidencia(AppDatabase.getAppDatabase(context).getCon(), "baja").execute(incidencia).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    private static class AsyncTask_opIncidencias extends AsyncTask<String, Void, Boolean> {
        private Connection con;
        private String op;
        private List<Incidencia> tIncs;

        private AsyncTask_opIncidencias(Connection con, String op, List<Incidencia> tIncs) {
            this.con = con;
            this.op = op;
            this.tIncs = tIncs;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String sql = "";
            boolean errorSQL = false;
            switch (op) {
                case "recuperarIncs":
                    if (params[0].equals("0")) {
                        sql = "SELECT* FROM Incidencias ORDER BY idDpto,id,fecha";
                        try (PreparedStatement pst = con.prepareStatement(sql)) {
                            ResultSet rs = pst.executeQuery();
                            while (rs.next()) {
                                Incidencia incidencia = new Incidencia();
                                incidencia.setIdDpto(rs.getInt("idDpto"));
                                incidencia.setId(rs.getString("id"));
                                incidencia.setFecha(rs.getString("fecha"));
                                incidencia.setDescripcion(rs.getString("descripcion"));
                                incidencia.setTipo(rs.getString("tipo"));
                                incidencia.setEstado(rs.getBoolean("estado"));
                                incidencia.setResolucion(rs.getString("resolucion"));
                                tIncs.add(incidencia);
                            }

                        } catch (Exception e) {
                            errorSQL = true;
                        }
                    } else {
                        int idDepto = Integer.parseInt(params[0]);
                        sql = "SELECT* FROM Incidencias WHERE idDpto = ? ORDER BY idDpto,id,fecha";
                        try (PreparedStatement pst = con.prepareStatement(sql)) {
                            pst.setInt(1, idDepto);
                            ResultSet rs = pst.executeQuery();
                            while (rs.next()) {
                                Incidencia incidencia = new Incidencia();
                                incidencia.setIdDpto(rs.getInt("idDpto"));
                                incidencia.setId(rs.getString("id"));
                                incidencia.setFecha(rs.getString("fecha"));
                                incidencia.setDescripcion(rs.getString("descripcion"));
                                incidencia.setTipo(rs.getString("tipo"));
                                incidencia.setEstado(rs.getBoolean("estado"));
                                incidencia.setResolucion(rs.getString("resolucion"));
                                tIncs.add(incidencia);
                            }

                        } catch (Exception e) {
                            errorSQL = true;
                        }
                    }
                    break;
                case "recuperarIncFiltro":
                    if (params[1].equals("%"))
                        sql = "SELECT * FROM Incidencias WHERE idDpto = ? AND fecha >= ? ORDER BY idDpto,id,fecha";
                    else if (params[1].equals("1"))
                        sql = "SELECT * FROM Incidencias WHERE idDpto = ? AND estado = '1' AND fecha >= ? ORDER BY idDpto,id,fecha";
                    else
                        sql = "SELECT * FROM Incidencias WHERE idDpto = ? AND estado = '0' AND fecha >= ? ORDER BY fecha,id";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        int id = Integer.parseInt(params[0]);
                        pst.setInt(1, id);
                        pst.setString(2, params[2]);
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            Incidencia incidencia = new Incidencia();
                            incidencia.setIdDpto(rs.getInt("idDpto"));
                            incidencia.setId(rs.getString("id"));
                            incidencia.setFecha(rs.getString("fecha"));
                            incidencia.setDescripcion(rs.getString("descripcion"));
                            incidencia.setTipo(rs.getString("tipo"));
                            incidencia.setEstado(rs.getBoolean("estado"));
                            incidencia.setResolucion(rs.getString("resolucion"));
                            tIncs.add(incidencia);
                        }
                    } catch (Exception e) {
                        errorSQL = true;
                    }
                    break;
            }
            return !errorSQL;
        }
    }

    public static List<Incidencia> recuperarIncidencias(Context context, Departamento departamento) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        List<Incidencia> tIncs = new ArrayList<>();
        String idDpto = String.valueOf(departamento.getId());
        try {
            boolean ok = new AsyncTask_opIncidencias(
                    AppDatabase.getAppDatabase(context).getCon(),
                    "recuperarIncs",
                    tIncs)
                    .execute(idDpto).get(timeout, TimeUnit.SECONDS);
            if (!ok) tIncs.clear();
            return tIncs;
        } catch (Exception e) {
            tIncs.clear();
            return tIncs;
        }
    }

    public static List<Incidencia> recuperarIncidenciasFiltro(Context context, Filtro filtro) {

        String est;
        if (filtro.getEstado() == 1)
            est = "%";
        else if (filtro.getEstado() == 2)
            est = "1";
        else
            est = "0";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        List<Incidencia> tIncs = new ArrayList<>();
        try {
            boolean ok = new AsyncTask_opIncidencias(
                    AppDatabase.getAppDatabase(context).getCon(),
                    "recuperarIncFiltro",
                    tIncs)
                    .execute(String.valueOf(filtro.getIdDepto()), est, filtro.getFecha()).get(timeout, TimeUnit.SECONDS);
            if (!ok) tIncs.clear();
            return tIncs;
        } catch (Exception e) {
            tIncs.clear();
            return tIncs;
        }
    }

}

