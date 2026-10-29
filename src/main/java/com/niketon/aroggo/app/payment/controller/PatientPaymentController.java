package com.niketon.aroggo.app.payment.controller;

import com.niketon.aroggo.app.appointment.service.AppointmentService;
import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.service.PatientService;
import com.niketon.aroggo.app.payment.repository.PatientPaymentRepository;
import com.niketon.aroggo.app.payment.service.PatientFinancialService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PatientPaymentController {

	private final PatientService patientService;
	private final AppointmentService appointmentService;
	private final PatientFinancialService financialService;
	private final PatientPaymentRepository paymentRepository;


	/*
	 * ============================================================
	 * PAYMENT SEARCH PAGE
	 * ============================================================
	 */

	@GetMapping({"", "/", "/search"})
	public String paymentSearchPage(Model model) {

		model.addAttribute(
				"patient",
				new Patient()
		);

		return "payment/paymentSearch";
	}


	/*
	 * ============================================================
	 * PATIENT FINANCIAL DETAILS
	 * We will discuss this page next day.
	 * ============================================================
	 */

	@GetMapping("/patient/{patientCode}")
	public String patientFinancialDetails(
			@PathVariable String patientCode,
			Model model
	) {

		Patient patient =
				patientService.getPatientByCode(patientCode);

		model.addAttribute(
				"patient",
				patient
		);

		model.addAttribute(
				"appointments",
				appointmentService.getPatientAppointments(patientCode)
		);

		model.addAttribute(
				"outstandingDue",
				financialService.getOutstandingDue(patientCode)
		);

		model.addAttribute(
				"payments",
				paymentRepository
						.findByPatientCodeOrderByReceivedAtDesc(
								patientCode
						)
		);

		model.addAttribute(
				"totalCharges",
				getTotalCharges(patientCode)
		);

		model.addAttribute(
				"totalPayments",
				getTotalPayments(patientCode)
		);

		return "payment/patientFinancialDetails";
	}


	/*
	 * ============================================================
	 * RECEIVE PAYMENT
	 * ============================================================
	 */

	@PostMapping("/receive")
	public String receivePayment(
			@RequestParam String patientCode,
			@RequestParam BigDecimal amount,
			@RequestParam String paymentMethod,
			@RequestParam(required = false) String note,
			RedirectAttributes redirectAttributes
	) {

		try {

			financialService.receivePatientPayment(
					patientCode,
					amount,
					paymentMethod,
					null,
					note
			);

			redirectAttributes.addFlashAttribute(
					"successMessage",
					"Payment recorded successfully."
			);

		} catch (IllegalArgumentException ex) {

			redirectAttributes.addFlashAttribute(
					"errorMessage",
					ex.getMessage()
			);
		}

		return "redirect:/payments/patient/" + patientCode;
	}


	/*
	 * ============================================================
	 * FINANCIAL TOTALS
	 * ============================================================
	 */

	private BigDecimal getTotalCharges(String patientCode) {

		return financialService.getTotalCharges(patientCode);
	}


	private BigDecimal getTotalPayments(String patientCode) {

		return financialService.getTotalPayments(patientCode);
	}

}