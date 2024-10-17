package com.ej4.tinder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//Cual es la url que va a escuchar este controlador, este va a escuchar a partir de la barra
@RequestMapping("/")
public class PortalController {

    //Este metodo se accede a traves de una operacion get de http
    //Cuando entre a la raiz de mi servidor va a ejecutar
    // y va a devolver la vista que quiero que se dibuje.
    @GetMapping("/")
    public String index(){
        return "index";
    }

}
