package com.ej4.tinder.controller;

import com.ej4.tinder.domain.entity.Mascota;
import com.ej4.tinder.domain.entity.Usuario;
import com.ej4.tinder.domain.entity.Zona;
import com.ej4.tinder.domain.enumeration.Sexo;
import com.ej4.tinder.domain.enumeration.Tipo;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.logic.service.MascotaService;
import com.ej4.tinder.logic.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
//Cual es la url que va a escuchar este controlador, este va a escuchar a partir de la barra
@RequestMapping("/mascota")
public class MascotaController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MascotaService mascotaService;

    @GetMapping("/mis-mascotas")
    public String misMascotas(HttpSession session, ModelMap modelo) {
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado == null) {
            return "redirect:/login";
        }
        try {
            List<Mascota> mascotas = mascotaService.listarMascotasPorUsuario(logeado.getId());
            modelo.put("mascotas", mascotas);
            return "mascotas";
        } catch (ErrorService e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, ModelMap modelo, @RequestParam(required = false) String id, @RequestParam(required = false) String accion){
        if(accion == null){
            accion = "Crear";
        }

        Usuario logeado = (Usuario) session.getAttribute("usuariosession");
        if (logeado == null) {
            return "redirect:/index";
        }
        Mascota mascota = new Mascota();
        if(id != null && !id.isEmpty()){
            try {
                mascota = mascotaService.buscarMascota(id);
            } catch (ErrorService e) {
                throw new RuntimeException(e);
            }
        }
        modelo.put("perfil", mascota);
        modelo.put("accion", accion);
        modelo.put("sexos", Sexo.values());
        modelo.put("tipos", Tipo.values());
        return "mascota";
    }

    @PostMapping("/actualizar-perfil")
    public String actualizar(HttpSession session, ModelMap modelo, MultipartFile archivo, @RequestParam String id, @RequestParam String nombre, @RequestParam Sexo sexo, @RequestParam Tipo tipo){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario == null) {
            return "redirect:/login";
        }
        try {

            if (id.equals("")) {
                mascotaService.agregarMascota(archivo,usuario.getId(), nombre, sexo, tipo);
            }else {
                mascotaService.modificarMascota(archivo,usuario.getId(), id, nombre,sexo, tipo);
            }
            return "redirect:/mascota/mis-mascotas";
        }catch (ErrorService ex){
            Mascota mascota = new Mascota();
            mascota.setId(id);
            mascota.setNombre(nombre);
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);
            modelo.put("accion", "Actualizar");
            modelo.put("perfil", mascota);
            modelo.put("sexos", Sexo.values());
            modelo.put("tipos", Tipo.values());
            modelo.put("error", ex.getMessage());
//
            return "/mascota";
        }
    }

    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id){
        try{
            Usuario logeado = (Usuario) session.getAttribute("usuariosession");
            mascotaService.eliminarMascota(logeado.getId(), id);
        }catch (ErrorService ex){
            Logger.getLogger(MascotaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/mascota/mis-mascotas";
    }
}
