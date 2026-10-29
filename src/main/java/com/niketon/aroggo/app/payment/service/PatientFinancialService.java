package com.niketon.aroggo.app.payment.service;

import com.niketon.aroggo.app.payment.entity.PatientCharge;
import com.niketon.aroggo.app.payment.entity.PatientPayment;
import com.niketon.aroggo.app.payment.entity.PaymentAudit;
import com.niketon.aroggo.app.payment.repository.PatientChargeRepository;
import com.niketon.aroggo.app.payment.repository.PatientPaymentRepository;
import com.niketon.aroggo.app.payment.repository.PaymentAuditRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientFinancialService {

	private final PatientChargeRepository chargeRepository;
	private final PatientPaymentRepository paymentRepository;
	private final PaymentAuditRepository auditRepository;


	// ============================================================
	// 1. GET TOTAL OUTSTANDING DUE
	// ============================================================

	@Transactional(readOnly = true)
	public BigDecimal getOutstandingDue(String patientCode) {

		BigDecimal totalCharges =
				chargeRepository.sumChargesByPatientCode(patientCode);

		BigDecimal totalPayments =
				paymentRepository.sumPaymentsByPatientCode(patientCode);

		return totalCharges.subtract(totalPayments);
	}


	// ============================================================
	// 2. SAVE APPOINTMENT CHARGE + INITIAL PAYMENT
	// ============================================================

	@Transactional
	public void recordAppointmentChargeAndPayment(
			String patientCode,
			String appointmentCode,
			BigDecimal appointmentFee,
			BigDecimal paymentNow,
			String paymentMethod,
			String receivedBy,
			String note
	) {

		validateNonNegative(appointmentFee, "Appointment fee");
		validateNonNegative(paymentNow, "Payment amount");

		if (chargeRepository
				.findByPatientCodeAndAppointmentCode(
						patientCode,
						appointmentCode
				)
				.isPresent()) {

			throw new IllegalStateException(
					"A charge already exists for this appointment."
			);
		}

		PatientCharge charge = new PatientCharge();
		charge.setPatientCode(patientCode);
		charge.setAppointmentCode(appointmentCode);
		charge.setAmount(appointmentFee);

		chargeRepository.save(charge);

		// Includes the new appointment charge.
		BigDecimal dueAfterCharge =
				getOutstandingDue(patientCode);

		if (paymentNow.compareTo(dueAfterCharge) > 0) {
			throw new IllegalArgumentException(
					"Payment cannot exceed outstanding due: "
							+ dueAfterCharge.toPlainString()
			);
		}

		// No payment record is necessary for a zero collection.
		if (paymentNow.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		PatientPayment payment = new PatientPayment();

		payment.setPaymentCode(generatePaymentCode());
		payment.setPatientCode(patientCode);
		payment.setAppointmentCode(appointmentCode);
		payment.setAmount(paymentNow);
		payment.setPaymentMethod(paymentMethod);
		payment.setReceivedBy(receivedBy);
		payment.setNote(note);

		paymentRepository.save(payment);
	}


	// ============================================================
	// 3. GET EXISTING APPOINTMENT PAYMENT AMOUNT
	// ============================================================

	@Transactional(readOnly = true)
	public BigDecimal getAppointmentPaymentAmount(
			String patientCode,
			String appointmentCode
	) {

		return paymentRepository
				.findByPatientCodeAndAppointmentCode(
						patientCode,
						appointmentCode
				)
				.map(PatientPayment::getAmount)
				.orElse(BigDecimal.ZERO);
	}


	// ============================================================
	// 4. CORRECT APPOINTMENT FINANCIAL RECORD
	// Call only after the appointment edit-window check succeeds.
	// ============================================================

	@Transactional
	public void updateAppointmentFinancials(
			String patientCode,
			String appointmentCode,
			BigDecimal newAppointmentFee,
			BigDecimal newPaymentAmount,
			String newPaymentMethod,
			String changedBy,
			String reason
	) {

		validateNonNegative(newAppointmentFee, "Appointment fee");
		validateNonNegative(newPaymentAmount, "Payment amount");

		PatientCharge charge =
				chargeRepository
						.findByPatientCodeAndAppointmentCode(
								patientCode,
								appointmentCode
						)
						.orElseThrow(() ->
								new IllegalStateException(
										"Appointment charge not found."
								)
						);

		// Update the existing charge; never create a duplicate.
		charge.setAmount(newAppointmentFee);
		chargeRepository.saveAndFlush(charge);

		PatientPayment existingPayment =
				paymentRepository
						.findByPatientCodeAndAppointmentCode(
								patientCode,
								appointmentCode
						)
						.orElse(null);

		BigDecimal oldPaymentAmount =
				existingPayment == null
						? BigDecimal.ZERO
						: existingPayment.getAmount();

		BigDecimal totalCharges =
				chargeRepository.sumChargesByPatientCode(patientCode);

		BigDecimal totalPayments =
				paymentRepository.sumPaymentsByPatientCode(patientCode);

		// Exclude the payment being corrected from other payments.
		BigDecimal otherPayments =
				totalPayments.subtract(oldPaymentAmount);

		BigDecimal maximumAllowedPayment =
				totalCharges.subtract(otherPayments);

		if (maximumAllowedPayment.compareTo(BigDecimal.ZERO) < 0) {
			maximumAllowedPayment = BigDecimal.ZERO;
		}

		if (newPaymentAmount.compareTo(maximumAllowedPayment) > 0) {
			throw new IllegalArgumentException(
					"Payment cannot exceed the available outstanding "
							+ "balance of "
							+ maximumAllowedPayment.toPlainString()
			);
		}

		if (existingPayment == null) {

			// A previously unpaid appointment can receive a payment
			// during its permitted edit window.
			if (newPaymentAmount.compareTo(BigDecimal.ZERO) > 0) {

				PatientPayment payment = new PatientPayment();

				payment.setPaymentCode(generatePaymentCode());
				payment.setPatientCode(patientCode);
				payment.setAppointmentCode(appointmentCode);
				payment.setAmount(newPaymentAmount);
				payment.setPaymentMethod(newPaymentMethod);
				payment.setReceivedBy(changedBy);
				payment.setNote("Collected during appointment edit");

				paymentRepository.save(payment);
			}

			return;
		}

		// Save an audit entry before changing the existing amount.
		if (oldPaymentAmount.compareTo(newPaymentAmount) != 0) {

			if (reason == null || reason.isBlank()) {
				throw new IllegalArgumentException(
						"A reason is required to correct a payment."
				);
			}

			PaymentAudit audit = new PaymentAudit();

			audit.setPaymentId(existingPayment.getId());
			audit.setOldAmount(oldPaymentAmount);
			audit.setNewAmount(newPaymentAmount);
			audit.setReason(reason.trim());
			audit.setChangedBy(changedBy);

			auditRepository.save(audit);
		}

		// Update the same payment record, not a duplicate payment.
		existingPayment.setAmount(newPaymentAmount);
		existingPayment.setPaymentMethod(newPaymentMethod);

		paymentRepository.save(existingPayment);
	}


	// ============================================================
	// 5. RECEIVE MONEY THROUGH THE SEPARATE PAYMENT MODULE
	// ============================================================

	@Transactional
	public PatientPayment receivePatientPayment(
			String patientCode,
			BigDecimal amount,
			String paymentMethod,
			String receivedBy,
			String note
	) {

		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(
					"Payment amount must be greater than zero."
			);
		}

		BigDecimal outstandingDue =
				getOutstandingDue(patientCode);

		if (outstandingDue.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(
					"This patient has no outstanding due."
			);
		}

		if (amount.compareTo(outstandingDue) > 0) {
			throw new IllegalArgumentException(
					"Payment cannot exceed outstanding due: "
							+ outstandingDue.toPlainString()
			);
		}

		PatientPayment payment = new PatientPayment();

		payment.setPaymentCode(generatePaymentCode());
		payment.setPatientCode(patientCode);

		// Standalone payment: not allocated to one appointment.
		payment.setAppointmentCode(null);

		payment.setAmount(amount);
		payment.setPaymentMethod(paymentMethod);
		payment.setReceivedBy(receivedBy);
		payment.setNote(note);

		return paymentRepository.save(payment);
	}


	// ============================================================
	// VALIDATION / CODE GENERATION
	// ============================================================

	private void validateNonNegative(
			BigDecimal amount,
			String fieldName
	) {

		if (amount == null
				|| amount.compareTo(BigDecimal.ZERO) < 0) {

			throw new IllegalArgumentException(
					fieldName + " must be zero or greater."
			);
		}
	}


	private String generatePaymentCode() {

		return "PAY-"
				+ UUID.randomUUID()
				.toString()
				.replace("-", "");
	}

	@Transactional(readOnly = true)
	public BigDecimal getTotalCharges(String patientCode) {
		return chargeRepository.sumChargesByPatientCode(patientCode);
	}

	@Transactional(readOnly = true)
	public BigDecimal getTotalPayments(String patientCode) {
		return paymentRepository.sumPaymentsByPatientCode(patientCode);
	}
}