package com.niketon.aroggo.app.prescriber.controller;


import com.niketon.aroggo.app.prescriber.dto.PrescriberForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/prescriber")
public class PrescriberController {

	@GetMapping
	public String showForm(
			@ModelAttribute("form") PrescriberForm form) {

		if (form.getAppointmentDate() == null) {
			form.setAppointmentDate(LocalDate.now());
		}

		if (form.getAppointmentTime() == null) {
			form.setAppointmentTime(LocalTime.now()
					.withSecond(0)
					.withNano(0));
		}

		if (form.getMedicines() == null) {
			form.setMedicines(new ArrayList<>());
		}

		return "prescriber/prescriberForm";
	}

	@PostMapping("/print")
	public String printPrescription(
			@ModelAttribute("form") PrescriberForm form) {

		if (form.getMedicines() == null) {
			form.setMedicines(new ArrayList<>());
		}

		return "prescriber/prescriberPrint";
	}
}