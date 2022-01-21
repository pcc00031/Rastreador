package uja.dae.rastreador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uja.dae.rastreador.repositorios.RepositorioRastreadores;

/**
 * @author Pedro
 */
@Controller
@RequestMapping("/vidtracker")
public class VidTrackerController {
//
//    @Autowired
//    RepositorioRastreadores rastreadores;
//
//    public VidTrackerController(RepositorioRastreadores rastreadores) {
//        this.rastreadores = rastreadores;
//    }
//
//    @GetMapping("/rastreadores")
//    public String verRastreadores(Model model) {
//        model.addAttribute("rastreadores", rastreadores.verRastreadores());
//        return "index";
//    }
}
