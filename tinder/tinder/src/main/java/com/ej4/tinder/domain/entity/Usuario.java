package com.ej4.tinder.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity

public class Usuario {
    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String clave;
    private boolean eliminado;
    //TimeStamp Fecha hora y zona horaria
    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;
    @ManyToOne
    private Zona zona;
    @OneToOne
    private Foto foto;
}
