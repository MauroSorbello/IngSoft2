package com.ej4.tinder.controller;

import com.ej4.tinder.domain.entity.Mascota;
import com.ej4.tinder.domain.entity.Usuario;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.logic.service.FotoService;
import com.ej4.tinder.logic.service.MascotaService;
import com.ej4.tinder.logic.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/foto")
public class FotoController {

    @Autowired
    private FotoService fotoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MascotaService mascotaService;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<byte[]> fotoUsuario(@PathVariable String id){
        try{
            Usuario usuario = usuarioService.buscarUsuario(id);
            if(usuario.getFoto() == null){
                throw new ErrorService("El usuario no tiene una foto asignada");
            }
            byte[] foto = usuario.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            //Estas cabeceras le dicen al navegador que estoy enviando una imagen
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        }catch (ErrorService ex){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mascota/{id}")
    public ResponseEntity<byte[]> fotoMascota(@PathVariable String id){
        try{
            Mascota mascota = mascotaService.buscarMascota(id);
            if(mascota.getFoto() == null){
                throw new ErrorService("El mascota no tiene una foto asignada");
            }
            byte[] foto = mascota.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            //Estas cabeceras le dicen al navegador que estoy enviando una imagen
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        }catch (ErrorService ex){

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
