package com.dam.t07p02.vistamodelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

import com.dam.t07p02.R;
import com.dam.t07p02.modelo.AppDatabase;
import com.dam.t07p02.modelo.Departamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DptoLogica {


    /* Métodos Lógica Dptos ***********************************************************************/

    private static class AsyncTask_opDepartamento extends AsyncTask<Departamento, Void, Boolean> {
        private Connection con;
        private String op;

        private AsyncTask_opDepartamento(Connection con, String op) {
            this.con = con;
            this.op = op;
        }

        @Override
        protected Boolean doInBackground(Departamento... dptos) {
            String sql = "";
            boolean errorSQL = false;
            switch (op) {
                case "existe":
                    sql = "SELECT * FROM Departamentos WHERE id=? LIMIT 1";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, dptos[0].getId());
                        ResultSet rs = pst.executeQuery();
                        return (rs.next());
                    } catch (Exception e) {
                        errorSQL = true;
                    }
                    break;
                case "alta":
                    sql = "INSERT INTO Departamentos VALUES (?,?,?)";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, dptos[0].getId());
                        pst.setString(2, dptos[0].getNombre());
                        pst.setString(3, dptos[0].getClave());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        errorSQL = true;
                    }
                    break;
                case "editar":
                    sql = "UPDATE Departamentos SET nombre=?, clave=? WHERE id=?";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, dptos[0].getNombre());
                        pst.setString(2, dptos[0].getClave());
                        pst.setInt(3, dptos[0].getId());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        errorSQL = true;
                    }
                    break;
                case "baja":
                    sql = "DELETE FROM Departamentos WHERE id=?";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, dptos[0].getId());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        errorSQL = true;
                    }
                    break;
                case "cascade":
                    sql = "DELETE FROM Incidencias WHERE idDpto=?";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, dptos[0].getId());
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        errorSQL = true;
                    }
                    break;
            }
            return !errorSQL;
        }
    }

    public static boolean existeDepartamento(Context context, Departamento dpto) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opDepartamento(AppDatabase.getAppDatabase(context).getCon(), "existe").execute(dpto).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean altaDepartamento(Context context, Departamento dpto) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opDepartamento(AppDatabase.getAppDatabase(context).getCon(), "alta").execute(dpto).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean editarDepartamento(Context context, Departamento dpto) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opDepartamento(AppDatabase.getAppDatabase(context).getCon(), "editar").execute(dpto).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean bajaDepartamento(Context context, Departamento dpto) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opDepartamento(AppDatabase.getAppDatabase(context).getCon(), "baja").execute(dpto).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean onCascade(Context context, Departamento dpto) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        try {
            return new AsyncTask_opDepartamento(AppDatabase.getAppDatabase(context).getCon(), "cascade").execute(dpto).get(timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

    private static class AsyncTask_opDepartamentos extends AsyncTask<String, Void, Boolean> {
        private Connection con;
        private String op;
        private List<Departamento> tDptos;

        private AsyncTask_opDepartamentos(Connection con, String op, List<Departamento> tDptos) {
            this.con = con;
            this.op = op;
            this.tDptos = tDptos;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String sql = "";
            boolean errorSQL = false;
            switch (op) {
                case "recuperarDptos":
                    sql = "SELECT id,nombre,clave FROM Departamentos ORDER BY id";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            Departamento dpto = new Departamento();
                            dpto.setId(rs.getInt("id"));
                            dpto.setNombre(rs.getString("nombre"));
                            dpto.setClave(rs.getString("clave"));
                            tDptos.add(dpto);
                        }
                    } catch (Exception e) {
                        errorSQL = true;
                    }
                    break;
                case "recuperarDptoFiltro":
                    sql = "SELECT id,nombre,clave FROM Departamentos" +
                            " WHERE id LIKE ? ORDER BY id";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, params[0]);
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            Departamento dpto = new Departamento();
                            dpto.setId(rs.getInt("id"));
                            dpto.setNombre(rs.getString("nombre"));
                            dpto.setClave(rs.getString("clave"));
                            tDptos.add(dpto);
                        }
                    } catch (Exception e) {
                        errorSQL = true;
                    }
                    break;
            }
            return !errorSQL;
        }
    }

    public static List<Departamento> recuperarDepartamentos(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        List<Departamento> tDptos = new ArrayList<>();
        try {
            boolean ok = new AsyncTask_opDepartamentos(
                    AppDatabase.getAppDatabase(context).getCon(),
                    "recuperarDptos",
                    tDptos)
                    .execute().get(timeout, TimeUnit.SECONDS);
            if (!ok) tDptos.clear();
            return tDptos;
        } catch (Exception e) {
            tDptos.clear();
            return tDptos;
        }
    }

    public static Departamento recuperarDepartamentoFiltro(Context context, String idDpto) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
        List<Departamento> tDptos = new ArrayList<>();
        try {
            boolean ok = new AsyncTask_opDepartamentos(
                    AppDatabase.getAppDatabase(context).getCon(),
                    "recuperarDptoFiltro",
                    tDptos)
                    .execute(idDpto).get(timeout, TimeUnit.SECONDS);
            if (!ok) return null;
            return tDptos.get(0);
        } catch (Exception e) {
            return null;
        }
    }

}
