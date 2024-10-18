package com.ej4.tinder.logic.service;

import com.ej4.tinder.domain.entity.Foto;
import com.ej4.tinder.domain.entity.Usuario;
import com.ej4.tinder.domain.entity.Zona;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.repository.UsuarioRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UsuarioService /*implements UserDetailsService*/ {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ZonaService zonaService;
    @Autowired
    private FotoService fotoService;


    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave, String clave2, String idZona) throws ErrorService {
        try {
            validar(nombre, apellido, mail, clave, clave2, idZona);
            Zona zona = zonaService.buscarZona(idZona);
            try {
                Usuario usuarioAux = usuarioRepository.findByEmailAndBajaIsNull(mail);
                if (usuarioAux != null && !usuarioAux.isEliminado()) {
                    throw new ErrorService("Existe un usuario con el mail indicado");
                }
            } catch (NoResultException ex) {
            }
            Usuario usuario = new Usuario();
            usuario.setId((UUID.randomUUID().toString()));
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(mail);
            usuario.setClave(clave);
            usuario.setAlta(new Date());

            Foto foto = fotoService.guardar(archivo);
            usuario.setFoto(foto);
            usuarioRepository.save(usuario);
        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave, String idZona) throws ErrorService {
        try {
            validar(nombre, apellido, mail, clave, clave, idZona);
            Zona zona = zonaService.buscarZona(idZona);
            Optional<Usuario> respuesta = usuarioRepository.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setEmail(mail);
                usuario.setClave(clave);

                String idFoto = null;
                if (usuario.getFoto() != null){
                    idFoto = usuario.getFoto().getId();
                }
                Foto foto = fotoService.actualizar(idFoto, archivo);
                usuario.setFoto(foto);
                usuarioRepository.save(usuario);
            } else {
                throw new ErrorService("Debe indicar un usuario");
            }
        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorService {
        try {
            Optional<Usuario> respuesta = usuarioRepository.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setBaja(new Date());
            } else {
                throw new ErrorService("Debe indicar el usuario que desea deshabilitar");
            }
        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorService {
        try {
            Optional<Usuario> respuesta = usuarioRepository.findById(id);
            if (respuesta.isPresent()) {
                Usuario usuario = respuesta.get();
                usuario.setAlta(new Date());
            } else {
                throw new ErrorService("Debe indicar el usuario que desea deshabilitar");
            }
        } catch (ErrorService e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Usuario login(String mail, String clave) throws ErrorService {
        try {
            if (mail == null || mail.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el usuario");
            }

            if (clave == null || clave.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la clave");
            }
            Usuario usuario = null;
            try {
                usuario = usuarioRepository.findByEmailAndClaveAndBajaIsNull(mail, clave);
                return usuario;
            } catch (NoResultException e) {
                throw new ErrorService("No existe un usuario para ese mail y contraseña");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }

    }

    public List<Usuario> listarUsuarios() throws ErrorService {
        try {
            return usuarioRepository.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Usuario buscarUsuario(String idUsuario) throws ErrorService {
        try {
            if (idUsuario == null || idUsuario.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el usuario");
            }

            Optional<Usuario> optional = usuarioRepository.findById(idUsuario);
            Usuario usuario = null;
            if (optional.isPresent()) {
                usuario = optional.get();
            }
            return usuario;
        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }

    public void validar(String nombre, String apellido, String mail, String clave, String clave2, String idZona) throws ErrorService{
        if (nombre == null || nombre.isEmpty()){
            throw new ErrorService("Debe indicar un nombre.");
        }
        if (apellido == null || apellido.isEmpty()) throw new ErrorService("Debe indicar un apellido.");{}
        if (mail == null || mail.isEmpty()) throw new ErrorService("Debe indicar un mail.");
        if (clave == null || clave.isEmpty() || clave.length() < 6) {
            throw new ErrorService("La clave no puede ser nula, debe tenes mas de 6 caracteres.");
        }
        if (clave2 == null || clave2.isEmpty() || clave2.length() < 6) {
            throw new ErrorService("La clave de confirmacion no puede ser nula.");
        }
        if (!clave2.equals(clave)){
            throw new ErrorService("La clave de confirmacion debe ser igual a la clave ingresada.");
        }
        if (idZona == null || idZona.trim().isEmpty()){
            throw new ErrorService("Debe indicar la zona");
        }

    }

    /*
    //El metodo se va a llamar cuando el usuario quiere autenticarse en la plataforma (Sirve para diferenciar tipos de usuarios)
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        //Buscamos el usuario en nuestro dominio y lo transformamos al dominio de Spring Security
        Usuario usuario = usuarioRepository.findByEmailAndBajaIsNull(mail);
        if (usuario != null){
            //Listado de permisos
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_FOTOS");
            permisos.add(p1);
            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
            permisos.add(p1);
            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_VOTOS");
            permisos.add(p1);
            User user = new User(usuario.getEmail(), usuario.getClave(),permisos);
            return user;
        }else{
            return null;
        }
    }

    */
}
