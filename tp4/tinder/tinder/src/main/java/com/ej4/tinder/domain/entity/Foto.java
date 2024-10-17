package com.ej4.tinder.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Foto {
    @Id
    private String id;
    private String nombre;
    //@Lob es que es un tipo de datos grande
    //Q busque la foto solo cuando la pida (osea cuando haga un get)
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

    //Que tipo de foto es
    private String mime;


}
