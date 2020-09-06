package com.dam.t07p03.modelo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IncDao {

    @Query("SELECT * FROM incidencias ORDER BY id")
    LiveData<List<Incidencia>> getAll();

    @Query("SELECT * FROM incidencias WHERE idDpto = :idDepartamento ORDER BY idDpto,id,fecha")
    LiveData<List<Incidencia>> getIndenciasDepartamento(int idDepartamento);

    @Query("SELECT * FROM incidencias WHERE idDpto = :idDepartamento AND estado = :estado AND Date(fecha) > Date(:fecha) ORDER BY idDpto,id,fecha")
    LiveData<List<Incidencia>> getIndenciasFiltro(int idDepartamento,int estado,String fecha);

    @Query("SELECT * FROM incidencias WHERE idDpto = :idDepartamento  AND Date(fecha) > Date(:fecha) ORDER BY idDpto,id,fecha")
    LiveData<List<Incidencia>> getTodasIndenciasFiltro(int idDepartamento,String fecha);

    @Query("SELECT * FROM incidencias ORDER BY id")
    List<Incidencia> getAllNoLive();

    @Query("SELECT * FROM incidencias WHERE id LIKE :id")
    List<Incidencia> getDptoFiltroNoLive(String id);

    @Query("SELECT * FROM incidencias WHERE id = :id")
    Incidencia existe(String id);

    @Insert
    void insert(Incidencia inc);

    @Update
    void update(Incidencia inc);

    @Delete
    void delete(Incidencia inc);

}
