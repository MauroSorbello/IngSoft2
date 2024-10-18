package com.ej4.tinder.controller;

import com.ej4.tinder.domain.entity.Zona;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.logic.service.ZonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@Controller
//Cual es la url que va a escuchar este controlador, este va a escuchar a partir de la barra

public class PortalController {

    @Autowired
    private ZonaService zonaService;
    //Este metodo se accede a traves de una operacion get de http
    //Cuando entre a la raiz de mi servidor va a ejecutar
    // y va a devolver la vista que quiero que se dibuje.
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(ModelMap modelo){
        try {
            Collection<Zona> zonas = zonaService.listarZonaActiva();
            modelo.put("zonas", zonas);
            return "register";
        } catch (ErrorService e) {
                modelo.put("error", e.getMessage());
                return "";
        }
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session){
        return "inicio";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, ModelMap modelo){
        session.setAttribute("usuariosession", null);
        modelo.put("logout", "Ha salido correctamente de la plataforma");
        return "redirect:/login";
    }


}
