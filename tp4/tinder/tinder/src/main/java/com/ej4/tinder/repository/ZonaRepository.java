package com.ej4.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ej4.tinder.domain.entity.Zona;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface ZonaRepository extends JpaRepository<Zona, String> {
    //Consulta JPA basado en el nombre del metodo
    public Collection<Zona> findByEliminadoFalse();

    public Zona findByNombreAndEliminadoFalse(String nombre);
}
