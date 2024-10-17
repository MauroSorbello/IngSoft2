package com.ej4.tinder.logic.service;


import com.ej4.tinder.domain.entity.Foto;
import com.ej4.tinder.domain.entity.Usuario;
import com.ej4.tinder.domain.entity.Zona;
import com.ej4.tinder.logic.error.ErrorService;
import com.ej4.tinder.repository.FotoRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class FotoService {

    @Autowired
    private FotoRepository repository;
    //MultiPartFile es el archivo donde se almacena el archivo de la foto
    //Transactional: Si el metodo lanza una excepcion y no es atrapada, se vuelve atras con la
    //transaccion y no se aplica nada en la db
    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErrorService{
        try {
            Foto foto = null;
            if (archivo != null){
                foto = new Foto();
                foto.setId((UUID.randomUUID().toString()));
                foto.setMime(archivo.getContentType()); //Devuelve el tipo del archivo mime
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes()); //Pasa el contenido a un arreglo de bytes

                repository.save(foto);

            }
            return foto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo)throws ErrorService{
        try {
            Foto foto = null;
            if (archivo != null){
                if (idFoto != null){
                    Optional<Foto> optional = repository.findById(idFoto);
                    if (optional.isPresent()){
                        foto = optional.get();
                    }
                }
                foto.setMime(archivo.getContentType()); //Devuelve el tipo del archivo mime
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes()); //Pasa el contenido a un arreglo de bytes

                repository.save(foto);
            }
            return foto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorService("Error de Sistemas");
        }
    }
}
