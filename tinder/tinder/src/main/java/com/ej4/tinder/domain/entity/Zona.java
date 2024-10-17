package com.ej4.tinder.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data

public class Zona {
    @Id
    private String id;
    private String nombre;
    private boolean eliminado;

    public void setDescripcion(String descripcion) {
    }
}
