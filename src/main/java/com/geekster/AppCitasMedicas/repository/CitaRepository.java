package com.geekster.AppCitasMedicas.repository;

import com.geekster.AppCitasMedicas.model.Cita;
import com.geekster.AppCitasMedicas.model.Consultorio;
import com.geekster.AppCitasMedicas.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByDoctorAndHorarioConsultaBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);
    List<Cita> findByConsultorioAndHorarioConsulta(Consultorio consultorio, LocalDateTime horarioConsulta);
    List<Cita> findByNombrePacienteAndHorarioConsultaBetween(String nombrePaciente, LocalDateTime start, LocalDateTime end);
    List<Cita> findAllByHorarioConsultaBetween(LocalDateTime start, LocalDateTime end);
    List<Cita> findByDoctor(Doctor doctor);
    List<Cita> findByConsultorio(Consultorio consultorio);

}
