package com.ej4.tinder.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 *
 * @author IS2
 */

@Controller
public class FalloController implements ErrorController{


    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest httpRequest) {

        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso";
                break;
            }
            case 404: {
                errorMsg = "El recurso solicitado no fue encontrado";
                break;
            }
            case 500: {
                errorMsg = "Ocurrio un error interno";
                break;
            }
        }
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {

        Map mapa = httpRequest.getParameterMap();
        for (Object key : mapa.keySet()) {

            String[] valores = (String[]) mapa.get(key);
            for (String valor : valores) {

                System.out.println(key.toString()+ ": " + valor);
            }
        }
        return (Integer) httpRequest.getAttribute("jakarta.servlet.error.status_code");
    }

}