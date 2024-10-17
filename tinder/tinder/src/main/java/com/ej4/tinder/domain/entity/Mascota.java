package com.ej4.tinder.domain.entity;

import com.ej4.tinder.domain.enumeration.Sexo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor

public class Mascota {
    @Id
    private String id;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    @Temporal(TemporalType.TIMESTAMP)
    private Date alta;
    @Temporal(TemporalType.TIMESTAMP)
    private Date baja;
    @ManyToOne
    private Usuario usuario;
    @OneToOne
    private Foto foto;
    private boolean eliminado;
}
