package com.geekster.AppCitasMedicas.repository;

import com.geekster.AppCitasMedicas.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {}
