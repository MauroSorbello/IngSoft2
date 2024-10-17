package com.ej4.tinder.repository;

import com.ej4.tinder.domain.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, String> {
    public List<Mascota> findByEliminadoFalse();

    @Query("SELECT m FROM Mascota m Where m.usuario.id = :id and m.baja IS NULL")
    public List<Mascota> listarMascotasPorUsuario(@Param("id")String id);

    @Query("SELECT m From Mascota m Where m.usuario.id = :id and m.nombre = :nombre and m.baja IS NULL")
    public Mascota buscarMascotaPorNombre(@Param("id")String id, @Param("nombre")String nombre);
}
