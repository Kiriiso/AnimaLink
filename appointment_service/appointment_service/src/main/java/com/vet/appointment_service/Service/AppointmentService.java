package com.vet.appointment_service.service;

import com.vet.appointment_service.model.Appointment;
import java.util.List;

public interface AppointmentService {

    Appointment save(Appointment entity);

    Appointment update(Long id, Appointment entity);

    Appointment findById(Long id);

    List<Appointment> findAll();

    void deleteById(Long id);
}
