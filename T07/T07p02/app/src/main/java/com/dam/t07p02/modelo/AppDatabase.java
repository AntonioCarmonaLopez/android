package com.dam.t07p02.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

import com.dam.t07p02.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AppDatabase {

    private static volatile AppDatabase db = null;  // Singleton
    private static Connection con;

    private AppDatabase() {
        // Patrón Singleton
    }

    public Connection getCon() {
        return con;
    }

    public static AppDatabase getAppDatabase(Context context) {
        if (db == null) {
            synchronized (AppDatabase.class) {
                if (db == null) {
                    db = new AppDatabase();

//                    String name = "";
//                    String user = "";
//                    String password = "";
//                    String url = "jdbc:mysql://remotemysql:3306/" + name;

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                    String name = pref.getString(context.getResources().getString(R.string.MySQL_name_key), "");
                    String user = pref.getString(context.getResources().getString(R.string.MySQL_user_key), "");
                    String password = pref.getString(context.getResources().getString(R.string.MySQL_password_key), "");
                    String url = "jdbc:mysql://" +
                            pref.getString(context.getResources().getString(R.string.MySQL_url_key), "") + ":3306/" + name;
                    int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));

                    boolean conOk = false;
                    try {
                        conOk = new AsyncTask_abrirAppDatabase().execute(url, user, password).get(timeout, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        db = null;
                    }
                    if (!conOk) {
                        db = null;
                    }
                }
            }
        }
        return db;
    }

    private static class AsyncTask_abrirAppDatabase extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(params[0], params[1], params[2]);
                crearTablasMySQL();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    public static boolean cerrarAppDatabase(Context context) {
        if (db != null) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            int timeout = Integer.parseInt(Objects.requireNonNull(pref.getString(context.getResources().getString(R.string.MySQL_timeout_key), "10")));
            try {
                return new AsyncTask_cerrarAppDatabase().execute().get(timeout, TimeUnit.SECONDS);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private static class AsyncTask_cerrarAppDatabase extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                db.getCon().close();
                db = null;
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    private static void crearTablasMySQL() throws Exception {

        String sql = "";
        try (Statement st = db.getCon().createStatement()) {
            sql = "CREATE TABLE IF NOT EXISTS Departamentos (" +
                    "id INTEGER NOT NULL," +
                    "nombre VARCHAR(50) NOT NULL," +
                    "clave VARCHAR(50) NOT NULL," +
                    "CONSTRAINT pk_Departamentos PRIMARY KEY (id));";
            st.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS Incidencias (" +
                    "idDpto INTEGER NOT NULL," +
                    "id VARCHAR(50) NOT NULL," +
                    "fecha CHAR(10) NOT NULL," +
                    "descripcion VARCHAR(255) NOT NULL," +
                    "tipo CHAR(3) NOT NULL," +
                    "estado BOOLEAN NOT NULL," +
                    "resolucion VARCHAR(255) NOT NULL," +
                    "CONSTRAINT pk_Incidencias PRIMARY KEY (idDpto,id,fecha)," +
                    "CONSTRAINT fk_Incidencias_Departamentos FOREIGN KEY (idDpto)" +
                    " REFERENCES Departamentos (id) ON DELETE CASCADE);";
            st.executeUpdate(sql);
            sql = "insert ignore into Departamentos values (0, 'admin', 'a');";
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new Exception("Error crearTablas()!!", e);
        }
    }

}
