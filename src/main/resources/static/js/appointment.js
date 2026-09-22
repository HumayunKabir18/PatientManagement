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


});
