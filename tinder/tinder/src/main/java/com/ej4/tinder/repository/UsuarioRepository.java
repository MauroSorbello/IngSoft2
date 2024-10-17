package com.ej4.tinder.repository;

import com.ej4.tinder.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByEmailAndBajaIsNull(String email);
    Usuario findByEmailAndClaveAndBajaIsNull(String email, String clave);
    Usuario findByNombreAndBajaIsNull(String nombre);

    @Query("SELECT u FROM Usuario u WHERE u.zona.id = :id AND u.baja IS NULL")
    public List<Usuario> findByZonaIdIsNull(@Param("id")String id);

}
