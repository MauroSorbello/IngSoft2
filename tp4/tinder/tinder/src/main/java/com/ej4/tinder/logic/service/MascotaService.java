package com.ej4.tinder.logic.service;

import com.ej4.tinder.domain.entity.Foto;
import com.ej4.tinder.domain.entity.Mascota;
import com.ej4.tinder.domain.entity.Usuario;
import com.ej4.tinder.domain.enumeration.Sexo;
import com.ej4.tinder.domain.enumeration.Tipo;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.repository.MascotaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MascotaService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MascotaRepository mascotaRepository;
    @Autowired
    private FotoService fotoService;

    public void validar(String nombre, Sexo sexo) throws ErrorService{
        try {

            if (nombre == null || nombre.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el nombre");
            }

            if (sexo == null) {
                throw new ErrorService("Debe indicar el sexo");
            }

        } catch (ErrorService e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    @Transactional
    public void agregarMascota(MultipartFile archivo, String idUsuario, String nombre, Sexo sexo, Tipo tipo) throws ErrorService {
        try {
            validar(nombre, sexo);
            Usuario usuario = usuarioService.buscarUsuario(idUsuario);

            Mascota mascota = new Mascota();
            mascota.setNombre(nombre);
            mascota.setId((UUID.randomUUID().toString()));
            mascota.setSexo(sexo);
            mascota.setTipo(tipo);
            mascota.setUsuario(usuario);
            mascota.setAlta(new Date());

            Foto foto = fotoService.guardar(archivo);
            mascota.setFoto(foto);

            mascotaRepository.save(mascota);
        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }
    @Transactional
    public void modificarMascota(MultipartFile archivo, String idUsuario, String idMascota, String nombre, Sexo sexo, Tipo tipo) throws ErrorService {
        try {
            validar(nombre, sexo);
            Optional<Mascota> optional = mascotaRepository.findById(idMascota);
            Mascota mascota = null;
            if (optional.isPresent()){
                mascota = optional.get();
                if (mascota.getUsuario().getId().equals(idUsuario)) {
                    Usuario usuario = usuarioService.buscarUsuario(idUsuario);
                    mascota.setNombre(nombre);
                    mascota.setSexo(sexo);
                    mascota.setTipo(tipo);
                    String idFoto = null;
                    if (mascota.getFoto() != null){
                        idFoto = mascota.getFoto().getId();
                    }
                    Foto foto = fotoService.actualizar(idFoto, archivo);
                    mascota.setFoto(foto);
                    mascotaRepository.save(mascota);
                }else{
                    throw new ErrorService("El usuario debe ser el dueño de la mascota");
                }
            }else{
                throw new ErrorService("Debe indicar una mascota");
            }

        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }
    @Transactional
    public void eliminarMascota(String idUsuario, String idMascota)throws ErrorService {
        try {
            Optional<Mascota> optional = mascotaRepository.findById(idMascota);
            Mascota mascota = null;
            if (optional.isPresent()){
                mascota = optional.get();
                if (mascota.getUsuario().getId().equals(idUsuario)) {
                    mascota.setBaja(new Date());
                    mascota.setEliminado(true);
                    mascotaRepository.save(mascota);
                }else{
                    throw new ErrorService("El usuario debe ser el dueño de la mascota");
                }
            }else{
                throw new ErrorService("Debe indicar una mascota");
            }
        }catch(ErrorService e) {
            throw e;
        }catch(Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public List<Mascota> listarMascotas() throws ErrorService {
        try {
            return mascotaRepository.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Mascota buscarMascota(String idMascota) throws ErrorService {
        try {
            if (idMascota == null || idMascota.trim().isEmpty()) {
                throw new ErrorService("Debe indicar la mascota");
            }

            Optional<Mascota> optional = mascotaRepository.findById(idMascota);
            Mascota mascota = null;
            if (optional.isPresent()) {
                mascota = optional.get();
            }
            return mascota;
        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }

    public List<Mascota>  listarMascotasPorUsuario(String idUsuario) throws ErrorService {
        try {
            if (idUsuario == null || idUsuario.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el usuario");
            }
            return mascotaRepository.listarMascotasPorUsuario(idUsuario);
        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }
}
