/*
 * ============================================================
 * APPOINTMENT PAGE JAVASCRIPT
 * ============================================================
 */

document.addEventListener("DOMContentLoaded", function () {

    console.log(
        "================================="
    );

    console.log(
        "Appointment JavaScript loaded"
    );

    console.log(
        "================================="
    );


    /*
     * ========================================================
     * FIND ELEMENTS
     * ========================================================
     */

    const addMedicineButton =
        document.getElementById(
            "addMedicineButton"
        );


    const medicineBody =
        document.getElementById(
            "medicineBody"
        );


    const noMedicineMessage =
        document.getElementById(
            "noMedicineMessage"
        );


    /*
     * ========================================================
     * VALIDATION
     * ========================================================
     */

    if (!addMedicineButton) {

        console.error(
            "ERROR: #addMedicineButton was not found."
        );

        return;
    }


    if (!medicineBody) {

        console.error(
            "ERROR: #medicineBody was not found."
        );

        return;
    }


    console.log(
        "Add Medicine button found successfully."
    );


    /*
     * ========================================================
     * MEDICINE INDEX
     * ========================================================
     */

    let medicineIndex = 0;


    /*
     * ========================================================
     * ADD MEDICINE
     * ========================================================
     */

    addMedicineButton.addEventListener(
        "click",
        function () {

            console.log(
                "Add Medicine button clicked."
            );


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
             * Row HTML.
             */

            row.innerHTML = `

<td class="medicine-serial text-center">
    ${index + 1}
</td>


<td>

    <input
        type="text"
        class="form-control"
        name="appointment.medicines[${index}].medicineName"
        placeholder="Medicine name">

</td>


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

    </select>

</td>


<td>

    <input
        type="text"
        class="form-control"
        name="appointment.medicines[${index}].doses"
        placeholder="e.g. 1">

</td>


<td class="text-center">

    <input
        type="checkbox"
        class="form-check-input checkbox-medicine"
        name="appointment.medicines[${index}].morning"
        value="true">

</td>


<td class="text-center">

    <input
        type="checkbox"
        class="form-check-input checkbox-medicine"
        name="appointment.medicines[${index}].noon"
        value="true">

</td>


<td class="text-center">

    <input
        type="checkbox"
        class="form-check-input checkbox-medicine"
        name="appointment.medicines[${index}].night"
        value="true">

</td>


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


console.log(
"Medicine row added."
);

console.log(
"Medicine index:",
index
);

}
);


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


/*
 * Not a remove button.
 */

if (!removeButton) {

    return;

}


/*
 * Find medicine row.
 */

const row =
    removeButton.closest(
        ".medicine-row"
    );


/*
 * Remove row.
 */

if (row) {

    row.remove();

    console.log(
        "Medicine removed."
    );

}


/*
 * Refresh numbers.
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
        "table-row";

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
