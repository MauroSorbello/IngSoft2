package com.ej4.tinder.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Voto {
    @Id
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Temporal(TemporalType.TIMESTAMP)
    private Date respuesta;
    //La mascota 1 origina el voto
    @ManyToOne
    private Mascota mascota1;
    //La mascota 2 recibe el voto
    @ManyToOne
    private Mascota mascota2;
    private boolean eliminado;

}
