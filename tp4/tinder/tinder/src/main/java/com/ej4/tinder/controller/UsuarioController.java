package com.ej4.tinder.controller;

import com.ej4.tinder.domain.entity.Usuario;
import com.ej4.tinder.domain.entity.Zona;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.logic.service.UsuarioService;
import com.ej4.tinder.logic.service.ZonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@Controller
//Cual es la url que va a escuchar este controlador, este va a escuchar a partir de la barra
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ZonaService zonaService;


    //el modelo lo usamos para insertar en ese modelo toda la info q vamos a mostrar
    // en pantalla o utilizar en interfaces
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo, MultipartFile archivo , @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona){
        try {
            usuarioService.registrar(archivo,nombre,apellido,mail,clave1, clave2,idZona);
        }catch (ErrorService ex){
            try {
                Collection<Zona> zonas = zonaService.listarZonaActiva();
                modelo.put("zonas", zonas);

            } catch (ErrorService e) {
                modelo.put("error", e.getMessage());
            }
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("mail", mail);
            modelo.put("clave1", clave1);
            modelo.put("clave2", clave2);
            return "/register";
        }
        modelo.put("titulo", "Bienvenido a Tinder de Mascotas");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria.");
        return "/exito";
    }

    @PostMapping("/login")
    public String login(ModelMap modelo, @RequestParam String username, @RequestParam String password, HttpSession session){
        try {
            Usuario usuario = usuarioService.login(username,password);
            session.setAttribute("usuariosession", usuario);
            return "/inicio";
        }catch (ErrorService ex){
            modelo.put("error", ex.getMessage());
            modelo.put("username", username);
            modelo.put("password", password);

            return "login";
        }catch (Exception e){
            modelo.put("error", e.getMessage());
            return "login";
        }
//        modelo.put("titulo", "Bienvenido a Tinder de Mascotas");
//        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria.");

    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, ModelMap modelo, @RequestParam String id){
        try {
            Collection<Zona> zonas = zonaService.listarZonaActiva();
            modelo.put("zonas", zonas);
        } catch (ErrorService e) {
            modelo.put("error", e.getMessage());
        }
        //Validamos q nadie modifique otro usuario que no sea el suyo ingresando el id
        //del otro usuario en la url
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if(logeado == null || !logeado.getId().equals(id)){
            return "/index";
        }
        try {
            Usuario usuario = usuarioService.buscarUsuario(id);
            modelo.addAttribute("perfil", usuario);
        }catch (ErrorService ex){
            modelo.addAttribute("error", ex.getMessage());
            return "/register";
        }
        return "perfil";
    }

    @PostMapping("/actualizar-perfil")
    public String actualizar(HttpSession session, ModelMap modelo, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String idZona){
        try {
            Usuario logeado = (Usuario) session.getAttribute("usuariosession");
            if(logeado == null || !logeado.getId().equals(id)){
                return "/index";
            }
            usuarioService.modificar(archivo,id,nombre,apellido,mail,clave1,clave2);
            session.setAttribute("usuariosession", usuarioService.buscarUsuario(id));
            return "redirect:/inicio";
        }catch (ErrorService ex){
            try {
                Collection<Zona> zonas = zonaService.listarZonaActiva();
                modelo.put("zonas", zonas);
            } catch (ErrorService e) {
                modelo.put("error", e.getMessage());
            }
            modelo.put("error", ex.getMessage());
//            modelo.put("perfil", usuario);
            return "/perfil";
        }
    }
}
