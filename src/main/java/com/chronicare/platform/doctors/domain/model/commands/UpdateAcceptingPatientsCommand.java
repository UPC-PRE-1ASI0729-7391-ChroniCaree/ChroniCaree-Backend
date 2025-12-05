package com.chronicare.platform.doctors.domain.model.commands;

public record UpdateAcceptingPatientsCommand(Long doctorId, Boolean acceptingPatients) {}
