package com.ej4.tinder.logic.service;

import com.ej4.tinder.domain.entity.Mascota;
import com.ej4.tinder.domain.entity.Voto;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.repository.VotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VotoService {
    @Autowired
    MascotaService mascotaService;

    @Autowired
    VotoRepository repository;
    @Transactional
    public void votar(String idUsuario, String idMascota1, String idMascota2) throws ErrorService{
        try{
            Voto voto = new Voto();
            voto.setId((UUID.randomUUID().toString()));
            voto.setFecha(new Date());
            //Que no haya autovoto
            if (idMascota1.equals(idMascota2)){
                throw new ErrorService("No se pueden realizar auto votos.");
            }

            Mascota mascota1 = mascotaService.buscarMascota(idMascota1);
            if (mascota1.getUsuario().getId().equals(idUsuario)){
                voto.setMascota1(mascota1);
            }else {
                throw new ErrorService("No tiene permisos para realizar la operacion solicitada");
            }
            Mascota mascota2 = mascotaService.buscarMascota(idMascota2);
            voto.setMascota2(mascota2);
            repository.save(voto);
        }catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }
    @Transactional
    public void responder(String idUsuario, String idVoto) throws ErrorService{
        try{
            Optional<Voto> respuesta = repository.findById(idVoto);
            if (respuesta.isPresent()){
                Voto voto = respuesta.get();
                voto.setRespuesta(new Date());

                if (voto.getMascota2().getUsuario().getId().equals(idUsuario)){
                    repository.save(voto);
                } else{
                    throw new ErrorService("No tiene permisos para realizar esta accion");
                }
            }else{
                throw new ErrorService("Debe seleccionar el voto");
            }

        }catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }
    public List<Voto> listarVotos() throws ErrorService {
        try {
            return repository.findAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }

    public Voto buscarVoto(String idVoto) throws ErrorService {
        try {
            if (idVoto == null || idVoto.trim().isEmpty()) {
                throw new ErrorService("Debe indicar el voto");
            }

            Optional<Voto> optional = repository.findById(idVoto);
            Voto voto = null;
            if (optional.isPresent()) {
                voto = optional.get();
            }
            return voto;
        } catch (ErrorService ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorService("Error de sistema");
        }
    }
}
