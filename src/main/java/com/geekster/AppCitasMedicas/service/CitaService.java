package com.geekster.AppCitasMedicas.service;

import com.geekster.AppCitasMedicas.model.Cita;
import com.geekster.AppCitasMedicas.model.Consultorio;
import com.geekster.AppCitasMedicas.model.Doctor;
import com.geekster.AppCitasMedicas.repository.CitaRepository;
import com.geekster.AppCitasMedicas.repository.ConsultorioRepository;
import com.geekster.AppCitasMedicas.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CitaService {
    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultorioRepository consultorioRepository;

    public Cita agendarCita(Cita cita) throws Exception {
        validarCita(cita);
        return citaRepository.save(cita);
    }

    private void validarCita(Cita cita) throws Exception {
        LocalDateTime start = cita.getHorarioConsulta().minusHours(2);
        LocalDateTime end = cita.getHorarioConsulta().plusHours(2);

        if (!citaRepository.findByConsultorioAndHorarioConsulta(cita.getConsultorio(), cita.getHorarioConsulta()).isEmpty()) {
            throw new Exception("El consultorio ya está ocupado en ese horario.");
        }

        if (!citaRepository.findByDoctorAndHorarioConsultaBetween(cita.getDoctor(), start, end).isEmpty()) {
            throw new Exception("El doctor ya tiene una cita en ese horario.");
        }

        if (!citaRepository.findByNombrePacienteAndHorarioConsultaBetween(cita.getNombrePaciente(), start, end).isEmpty()) {
            throw new Exception("El paciente ya tiene una cita en un horario cercano.");
        }

        if (citaRepository.findByDoctorAndHorarioConsultaBetween(cita.getDoctor(), start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay().plusDays(1)).size() >= 8) {
            throw new Exception("El doctor ya tiene 8 citas en el día.");
        }
    }

    public List<Cita> consultarCitasPorFecha(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return citaRepository.findAllByHorarioConsultaBetween(start, end);
    }

    public List<Cita> consultarCitasPorDoctor(Doctor doctor) {
        return citaRepository.findByDoctor(doctor);
    }

    public List<Cita> consultarCitasPorConsultorio(Consultorio consultorio) {
        return citaRepository.findByConsultorio(consultorio);
    }
    public List<Doctor> obtenerDoctores() {
        return doctorRepository.findAll();
    }

    public List<Consultorio> obtenerConsultorios() {
        return consultorioRepository.findAll();
    }
    public List<Cita> consultarCitas() {
        return citaRepository.findAll();
    }

    public Cita obtenerCitaPorId(long id) {
        return citaRepository.getById(id);
    }

    public Cita editarCita(Long id, Cita nuevaCita) throws Exception {
        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new Exception("Cita no encontrada"));

        validarCitaParaEdicion(nuevaCita, citaExistente);

        citaExistente.setConsultorio(nuevaCita.getConsultorio());
        citaExistente.setDoctor(nuevaCita.getDoctor());
        citaExistente.setHorarioConsulta(nuevaCita.getHorarioConsulta());
        citaExistente.setNombrePaciente(nuevaCita.getNombrePaciente());

        return citaRepository.save(citaExistente); // Guardar cita actualizada en la base de datos
    }

    private void validarCitaParaEdicion(Cita nuevaCita, Cita citaExistente) throws Exception {
        LocalDateTime start = nuevaCita.getHorarioConsulta().minusHours(2);
        LocalDateTime end = nuevaCita.getHorarioConsulta().plusHours(2);

        // Validar que el consultorio no esté ocupado en el nuevo horario
        if (!citaExistente.getConsultorio().equals(nuevaCita.getConsultorio())
                && !citaRepository.findByConsultorioAndHorarioConsulta(nuevaCita.getConsultorio(), nuevaCita.getHorarioConsulta()).isEmpty()) {
            throw new Exception("El consultorio ya está ocupado en ese horario.");
        }

        // Validar que el doctor no tenga otra cita en el nuevo horario
        if (!citaExistente.getDoctor().equals(nuevaCita.getDoctor())
                && !citaRepository.findByDoctorAndHorarioConsultaBetween(nuevaCita.getDoctor(), start, end).isEmpty()) {
            throw new Exception("El doctor ya tiene una cita en ese horario.");
        }

        // Validar que el paciente no tenga otra cita cercana en el nuevo horario
        if (!citaExistente.getNombrePaciente().equals(nuevaCita.getNombrePaciente())
                && !citaRepository.findByNombrePacienteAndHorarioConsultaBetween(nuevaCita.getNombrePaciente(), start, end).isEmpty()) {
            throw new Exception("El paciente ya tiene una cita en un horario cercano.");
        }

        // Validar límite de citas por día para el doctor
        LocalDateTime startOfDay = nuevaCita.getHorarioConsulta().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        long count = citaRepository.findByDoctorAndHorarioConsultaBetween(nuevaCita.getDoctor(), startOfDay, endOfDay).stream()
                .filter(c -> !c.getId().equals(citaExistente.getId()))
                .count();
        if (count >= 8) {
            throw new Exception("El doctor ya tiene 8 citas en el día.");
        }
    }

    public void cancelarCita(Long id) throws Exception {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new Exception("Cita no encontrada"));

        LocalDateTime horaActual = LocalDateTime.now();
        if (horaActual.isAfter(cita.getHorarioConsulta())) {
            throw new Exception("La cita no puede ser cancelada, ya ha sido realizada.");
        }

        citaRepository.delete(cita);
    }
}

