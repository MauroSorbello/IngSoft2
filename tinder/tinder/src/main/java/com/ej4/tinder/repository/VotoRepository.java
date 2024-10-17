package com.ej4.tinder.repository;

import com.ej4.tinder.domain.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VotoRepository extends JpaRepository<Voto, String>{

    @Query("SELECT v FROM Voto v WHERE v.mascota1.id = :id ORDER BY v.fecha DESC")
    public List<Voto> buscarVotosPropios(@Param("id") String id);

    @Query("SELECT v FROM Voto v WHERE v.mascota2.id = :id ORDER BY v.fecha DESC")
    public List<Voto> buscarVotosRecibidos(@Param("id") String id);
}
