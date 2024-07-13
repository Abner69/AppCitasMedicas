package com.geekster.AppCitasMedicas.controller;

import com.geekster.AppCitasMedicas.model.Cita;
import com.geekster.AppCitasMedicas.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/citas")
public class CitaController {
    @Autowired
    private CitaService citaService;

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaCita(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("doctores", citaService.obtenerDoctores());
        model.addAttribute("consultorios", citaService.obtenerConsultorios());
        return "nueva-cita";
    }

    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute Cita cita, Model model) {
        try {
            citaService.agendarCita(cita);
            return "redirect:/citas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "nueva-cita";
        }
    }

    @GetMapping
    public String listarCitas(Model model) {
        model.addAttribute("citas", citaService.consultarCitas());
        return "lista-citas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCita(@PathVariable Long id, Model model) {
        Cita citaExistente = (Cita) (Cita) citaService.obtenerCitaPorId(id);
        if (citaExistente == null) {
            // Manejar cita no encontrada
            return "redirect:/citas";
        }

        model.addAttribute("cita", citaExistente);
        model.addAttribute("doctores", citaService.obtenerDoctores());
        model.addAttribute("consultorios", citaService.obtenerConsultorios());
        return "editar-cita";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCita(@PathVariable Long id, @ModelAttribute Cita nuevaCita, Model model) {
        try {
            Cita citaActualizada = citaService.editarCita(id, nuevaCita);
            return "redirect:/citas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("doctores", citaService.obtenerDoctores());
            model.addAttribute("consultorios", citaService.obtenerConsultorios());
            return "editar-cita";
        }
    }
    @PostMapping("/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, Model model) {
        try {
            citaService.cancelarCita(id);
            return "redirect:/citas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/citas";
        }
    }

}
