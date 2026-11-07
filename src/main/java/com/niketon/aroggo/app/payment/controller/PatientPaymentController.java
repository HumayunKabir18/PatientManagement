package com.niketon.aroggo.app.payment.controller;

import com.niketon.aroggo.app.appointment.entity.Appointment;
import com.niketon.aroggo.app.appointment.service.AppointmentService;
import com.niketon.aroggo.app.patient.entity.Patient;
import com.niketon.aroggo.app.patient.service.PatientService;
import com.niketon.aroggo.app.payment.dto.AppointmentDueView;
import com.niketon.aroggo.app.payment.repository.PatientPaymentRepository;
import com.niketon.aroggo.app.payment.service.PatientFinancialService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PatientPaymentController {

	private final PatientService patientService;
	private final AppointmentService appointmentService;
	private final PatientFinancialService financialService;
	private final PatientPaymentRepository paymentRepository;

	@GetMapping({"", "/", "/search"})
	public String paymentSearchPage(Model model) {
		model.addAttribute("patient", new Patient());
		return "payment/paymentSearch";
	}

	/*
	 * Patient financial details and outstanding appointments.
	 */
	@GetMapping("/patient/{patientCode}")
	public String patientFinancialDetails(
			@PathVariable String patientCode,
			Model model
	) {
		Patient patient =
				patientService.getPatientByCode(patientCode);

		List<Appointment> appointments =
				appointmentService.getPatientAppointments(patientCode);

		List<AppointmentDueView> dueAppointments =
				new ArrayList<>();

		for (Appointment appointment : appointments) {

			String appointmentCode =
					appointment.getId().getAppointmentCode();

			BigDecimal fee =
					financialService.getAppointmentChargeAmount(
							patientCode,
							appointmentCode
					);

			BigDecimal collected =
					financialService.getAppointmentPaymentAmount(
							patientCode,
							appointmentCode
					);

			BigDecimal due = fee.subtract(collected);

			// Display only unpaid or partially paid appointments.
			if (fee.compareTo(BigDecimal.ZERO) > 0
					&& due.compareTo(BigDecimal.ZERO) > 0) {

				dueAppointments.add(
						new AppointmentDueView(
								appointmentCode,
								appointment.getAppointmentDate(),
								fee,
								collected,
								due
						)
				);
			}
		}

		BigDecimal outstandingDue =
				financialService.getOutstandingDue(patientCode);

		model.addAttribute("patient", patient);
		model.addAttribute("dueAppointments", dueAppointments);
		model.addAttribute("outstandingDue", outstandingDue);

		// Select the first due appointment by default.
		if (!dueAppointments.isEmpty()) {
			AppointmentDueView first = dueAppointments.get(0);

			model.addAttribute(
					"selectedAppointmentCode",
					first.getAppointmentCode()
			);

			model.addAttribute(
					"selectedAppointmentFee",
					first.getAppointmentFee()
			);

			model.addAttribute(
					"selectedAppointmentDue",
					first.getAppointmentDue()
			);
		} else {
			model.addAttribute("selectedAppointmentCode", "");
			model.addAttribute("selectedAppointmentFee", BigDecimal.ZERO);
			model.addAttribute("selectedAppointmentDue", BigDecimal.ZERO);
		}

		// Retained for any existing financial-summary UI or future use.
		model.addAttribute(
				"payments",
				paymentRepository
						.findByPatientCodeOrderByReceivedAtDesc(patientCode)
		);

		model.addAttribute(
				"totalCharges",
				financialService.getTotalCharges(patientCode)
		);

		model.addAttribute(
				"totalPayments",
				financialService.getTotalPayments(patientCode)
		);

		return "payment/patientFinancialDetails";
	}

	/*
	 * Collect a payment against one selected appointment.
	 */
	@PostMapping("/receive-appointment")
	public String receiveAppointmentPayment(
			@RequestParam String patientCode,
			@RequestParam String appointmentCode,
			@RequestParam BigDecimal amount,
			@RequestParam String paymentMethod,
			@RequestParam(required = false) String note,
			RedirectAttributes redirectAttributes
	) {
		try {
			financialService.receiveAppointmentPayment(
					patientCode,
					appointmentCode,
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
	 * Keep the existing standalone patient payment endpoint.
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
}