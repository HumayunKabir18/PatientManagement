/*

* ============================================================
* APPOINTMENT PAGE JAVASCRIPT
* ============================================================
  */

document.addEventListener("DOMContentLoaded", function () {

    
/*
 * ========================================================
 * FIND ELEMENTS
 * ========================================================
 */

const addMedicineButton =
    document.getElementById("addMedicineButton");


const medicineBody =
    document.getElementById("medicineBody");


const noMedicineMessage =
    document.getElementById("noMedicineMessage");


/*
 * ========================================================
 * VALIDATION
 * ========================================================
 */

if (!medicineBody) {

    console.error(
        "ERROR: #medicineBody was not found."
    );

    return;
}


/*
 * ========================================================
 * MEDICINE INDEX
 *
 * Start after existing medicine rows.
 * ========================================================
 */

let medicineIndex =
    medicineBody.querySelectorAll(
        ".medicine-row"
    ).length;


/*
 * ========================================================
 * ADD MEDICINE
 * ========================================================
 */

if (addMedicineButton) {

    addMedicineButton.addEventListener(
        "click",
        function () {


            /*
             * Hide empty message.
             */

            if (noMedicineMessage) {

                noMedicineMessage.style.display =
                    "none";

            }


            /*
             * Current index.
             */

            const index =
                medicineIndex;


            /*
             * Create row.
             */

            const row =
                document.createElement("tr");


            row.classList.add(
                "medicine-row"
            );


            /*
             * ====================================================
             * ROW HTML
             * ====================================================
             */

            row.innerHTML = `
        

<td class="medicine-serial text-center">
    ${index + 1}
</td>

<!-- MEDICINE NAME -->

<td>


    <input
    type="text"
    class="form-control"
    name="appointment.medicines[${index}].medicineName"
    placeholder="Medicine name">
        

</td>
<td>


    <input
    type="text"
    class="form-control"
    name="appointment.medicines[${index}].originalName"
    placeholder="Original medicine name">
        

</td>

<!-- MEDICINE TYPE -->

<td>


        <select
    class="form-select"
    name="appointment.medicines[${index}].medicineType">

        <option value="">
        Select
        </option>

    <option value="TABLET">
        Tablet
    </option>

    <option value="CAPSULE">
        Capsule
    </option>

    <option value="LIQUID">
        Liquid
    </option>

    <option value="SYRUP">
        Syrup
    </option>

    <option value="INJECTION">
        Injection
    </option>

    <option value="CREAM">
        Cream
    </option>

    <option value="DROPS">
        Drops
    </option>

    <option value="SUPPOSITORY">
        Suppository
    </option>

    <option value="OTHER">
        Other
    </option>

</select>
    

</td>

<!-- DOSES -->

<td>


    <input
    type="text"
    class="form-control"
    name="appointment.medicines[${index}].doses"
    placeholder="e.g. 1">
        

</td>

<!-- FREQUENCY -->

<td>


        <input
    type="text"
    class="form-control"
    name="appointment.medicines[${index}].frequency"
    placeholder="e.g. Every 4 hours">
        

</td>

<!-- MEAL -->

<td>


        <select
    class="form-select"
    name="appointment.medicines[${index}].meal">

        <option value="">
        Select
        </option>

    <option value="BEFORE">
        Before Meal
    </option>

    <option value="AFTER">
        After Meal
    </option>

</select>
    

</td>

<!-- MORNING -->

<td class="text-center">


    <input
    type="checkbox"
    class="form-check-input checkbox-medicine"
    name="appointment.medicines[${index}].morning"
    value="true">
        

</td>

<!-- NOON -->

<td class="text-center">


        <input
    type="checkbox"
    class="form-check-input checkbox-medicine"
    name="appointment.medicines[${index}].noon"
    value="true">
        

</td>

<!-- NIGHT -->

<td class="text-center">


        <input
    type="checkbox"
    class="form-check-input checkbox-medicine"
    name="appointment.medicines[${index}].night"
    value="true">
        

</td>

<!-- DELETE -->

<td class="text-center">


        <button
    type="button"
    class="btn btn-outline-danger btn-sm remove-medicine"
    title="Remove medicine">

        <i class="bi bi-trash"></i>

</button>
    

</td>

`;

    
            /*
             * Add row to table.
             */

            medicineBody.appendChild(
                row
            );


            /*
             * Increment index.
             */

            medicineIndex++;


            /*
             * Refresh serial numbers.
             */

            refreshMedicineSerials();

        }
    );

}


/*
 * ========================================================
 * REMOVE MEDICINE
 * ========================================================
 */

medicineBody.addEventListener(
    "click",
    function (event) {


        const removeButton =
            event.target.closest(
                ".remove-medicine"
            );


        if (!removeButton) {

            return;

        }


        const row =
            removeButton.closest(
                ".medicine-row"
            );


        if (!row) {

            return;

        }


        row.remove();


        /*
         * Refresh serial numbers.
         */

        refreshMedicineSerials();


        /*
         * Check remaining rows.
         */

        const rows =
            medicineBody.querySelectorAll(
                ".medicine-row"
            );


        /*
         * Show empty message.
         */

        if (
            rows.length === 0 &&
            noMedicineMessage
        ) {

            noMedicineMessage.style.display =
                "block";

        }

    }
);


/*
 * ========================================================
 * SERIAL NUMBERS
 * ========================================================
 */

function refreshMedicineSerials() {

    const rows =
        medicineBody.querySelectorAll(
            ".medicine-row"
        );


    rows.forEach(
        function (row, index) {

            const serial =
                row.querySelector(
                    ".medicine-serial"
                );


            if (serial) {

                serial.textContent =
                    index + 1;

            }

        }
    );

}

    const datePicker = document.getElementById("followUpDateDisplay");
    const hidden = document.getElementById("followUpDate");

    if (!datePicker || !hidden) return;

    function updateHiddenDate() {
        hidden.value = datePicker.value;
    }

    datePicker.addEventListener("input", updateHiddenDate);
    datePicker.addEventListener("change", updateHiddenDate);

    updateHiddenDate();

    const $height = $('#appointment\\.height');
    const $weight = $('#appointment\\.weight');
    const $bmi = $('#appointment\\.bmi');
    const $message = $('#bmiMessage');
    const $doctorAdvice = $('#appointment\\.doctorAdvice');

    function calculateBMI() {
        const heightValue = ($height.val() || '').trim();
        const weight = parseFloat($weight.val());

        // Clear BMI and indication if inputs are incomplete
        if (!heightValue || !Number.isFinite(weight) || weight <= 0) {
            $bmi.val('');
            updateWeightAdvice(null, null);
            return;
        }

        // Height format: 5.7 means 5 feet 7 inches
        const parts = heightValue.split('.');

        if (parts.length > 2 || !/^\d+$/.test(parts[0]) ||
            (parts.length === 2 && !/^\d{1,2}$/.test(parts[1]))) {
            $bmi.val('');
            updateWeightAdvice(null, null);
            return;
        }

        const feet = Number(parts[0]);
        const inches = parts.length === 2 ? Number(parts[1]) : 0;

        if (feet <= 0 || inches > 11) {
            $bmi.val('');
            updateWeightAdvice(null, null);
            return;
        }

        const heightInMeters = (feet * 12 + inches) * 0.0254;

        if (heightInMeters <= 0) {
            $bmi.val('');
            updateWeightAdvice(null, null);
            return;
        }

        // Calculate and display BMI
        const bmi = weight / (heightInMeters * heightInMeters);
        $bmi.val(bmi.toFixed(2));

        updateWeightAdvice(bmi, {
            weight: weight,
            heightInMeters: heightInMeters
        });
    }

    function updateWeightAdvice(bmi, measurements) {
        $message
            .text('')
            .removeClass('text-success text-warning text-danger')
            .css('color', '');

        let adviceMessage = '';

        $message
            .text('')
            .removeClass('text-success text-warning text-danger');

        if (bmi !== null && measurements) {
            const { weight, heightInMeters } = measurements;

            if (bmi < 18.5) {
                const targetWeight = 18.5 * heightInMeters * heightInMeters;
                const gain = targetWeight - weight;

                adviceMessage = `ওজন ${gain.toFixed(1)} kg বাড়াতে হবে`;

                $message.text(adviceMessage).css('color', 'blue');
            } else if (bmi >= 25) {
                const targetWeight = 24.9 * heightInMeters * heightInMeters;
                const lose = weight - targetWeight;

                adviceMessage = `ওজন ${lose.toFixed(1)} kg কমাতে হবে`;

                $message.text(adviceMessage).addClass('text-danger');

            } else {
                $message
                    .text('স্বাস্থ্যকর BMI সীমার মধ্যে আছে')
                    .addClass('text-success');
            }
        }

        // Preserve other doctor notes and replace any previous
        // generated Bangla weight-indication line.
        if ($doctorAdvice.length) {
            let advice = $doctorAdvice.val() || '';

            advice = advice
                .split(/\r?\n/)
                .filter(line =>
                    !/^ওজন\s+[\d.]+\s*kg\s+(কমাতে হবে|বাড়াতে হবে)\s*$/.test(line.trim())
                )
                .join('\n')
                .trim();

            if (adviceMessage) {
                advice = advice
                    ? advice + '\n' + adviceMessage
                    : adviceMessage;
            }

            $doctorAdvice.val(advice);
        }
    }

    // Calculate on every height or weight change
    $height.add($weight).on('input change', calculateBMI);

    // Also calculate when editing an existing appointment
    calculateBMI();

    const feeInput = document.getElementById("appointmentFee");
    const paymentInput = document.getElementById("paymentNow");
    const previousDueInput = document.getElementById("previousOutstandingDue");
    const estimatedDueInput = document.getElementById("estimatedDue");
    const warning = document.getElementById("paymentWarning");

    if (
        !feeInput ||
        !paymentInput ||
        !previousDueInput ||
        !estimatedDueInput ||
        !warning
    ) {
        return;
    }

    // Read the previous outstanding due displayed by the server.
    const previousDue = Number(
        previousDueInput.value.replace(/,/g, "")
    ) || 0;

    function updatePaymentEstimate() {
        const fee = Number(feeInput.value || 0);
        const payment = Number(paymentInput.value || 0);

        const totalDue = previousDue + fee;
        const remaining = totalDue - payment;

        estimatedDueInput.value = remaining.toFixed(2);

        const invalid =
            feeInput.value.trim() === "" ||
            paymentInput.value.trim() === "" ||
            !Number.isFinite(fee) ||
            !Number.isFinite(payment) ||
            fee < 0 ||
            payment < 0 ||
            payment > totalDue;

        warning.classList.toggle("d-none", !invalid);
        estimatedDueInput.classList.toggle("is-invalid", invalid);

        // Prevent negative values and overpayment.
        paymentInput.setCustomValidity(
            payment > totalDue
                ? "Payment cannot exceed the total outstanding due."
                : ""
        );

        feeInput.setCustomValidity(
            fee < 0
                ? "Appointment fee cannot be negative."
                : ""
        );
    }

    feeInput.addEventListener("input", updatePaymentEstimate);
    paymentInput.addEventListener("input", updatePaymentEstimate);

    const form = feeInput.closest("form");

    if (form) {
        form.addEventListener("submit", function (event) {
            updatePaymentEstimate();

            if (!form.checkValidity()) {
                event.preventDefault();
                form.reportValidity();
            }

            const fee = Number(feeInput.value || 0);
            const payment = Number(paymentInput.value || 0);

            if (
                feeInput.value.trim() === "" ||
                paymentInput.value.trim() === "" ||
                !Number.isFinite(fee) ||
                !Number.isFinite(payment) ||
                fee < 0 ||
                payment < 0 ||
                payment > previousDue + fee
            ) {
                event.preventDefault();
                warning.classList.remove("d-none");
            }
        });
    }

    // Calculate the initial estimate on page load.
    updatePaymentEstimate();
});
