
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("prescriberForm");
    const addButton = document.getElementById("prescriberAddMedicine");
    const medicineBody = document.getElementById("prescriberMedicineBody");
    const noMedicineMessage = document.getElementById("prescriberNoMedicineMessage");

    if (!form || !addButton || !medicineBody) {
        return;
    }

    function createInput(name, type = "text", placeholder = "") {
        const input = document.createElement("input");
        input.type = type;
        input.name = name;
        input.className = type === "checkbox"
            ? "form-check-input"
            : "form-control";

        if (placeholder) {
            input.placeholder = placeholder;
        }

        if (type === "checkbox") {
            input.value = "true";
        }

        return input;
    }

    function createSelect(name, options) {
        const select = document.createElement("select");
        select.name = name;
        select.className = "form-select";

        options.forEach(function (item) {
            const option = document.createElement("option");
            option.value = item.value;
            option.textContent = item.label;
            select.appendChild(option);
        });

        return select;
    }

    function createCell(row, element, className = "") {
        const cell = document.createElement("td");

        if (className) {
            cell.className = className;
        }

        cell.appendChild(element);
        row.appendChild(cell);

        return cell;
    }

    function addMedicineRow() {
        const row = document.createElement("tr");
        row.className = "prescriber-medicine-row";

        const serial = document.createElement("td");
        serial.className = "prescriber-serial";
        row.appendChild(serial);

        const index = medicineBody.querySelectorAll("tr").length;

        createCell(
            row,
            createInput(`medicines[${index}].medicineName`)
        );

        createCell(
            row,
            createInput(`medicines[${index}].originalName`)
        );

        createCell(row, createSelect(
            `medicines[${index}].medicineType`,
            [
                { value: "", label: "Select" },
                { value: "TABLET", label: "Tablet" },
                { value: "CAPSULE", label: "Capsule" },
                { value: "LIQUID", label: "Liquid" },
                { value: "SYRUP", label: "Syrup" },
                { value: "INJECTION", label: "Injection" },
                { value: "CREAM", label: "Cream" },
                { value: "DROPS", label: "Drops" },
                { value: "SUPPOSITORY", label: "Suppository" },
                { value: "OTHER", label: "Other" }
            ]
        ));

        createCell(
            row,
            createInput(`medicines[${index}].doses`)
        );

        createCell(
            row,
            createInput(
                `medicines[${index}].frequency`,
                "text",
                "e.g. Twice daily"
            )
        );

        createCell(row, createSelect(
            `medicines[${index}].meal`,
            [
                { value: "", label: "Select" },
                { value: "BEFORE", label: "Before Meal" },
                { value: "AFTER", label: "After Meal" }
            ]
        ));

        ["morning", "noon", "night"].forEach(function (period) {
            const checkbox = createInput(
                `medicines[${index}].${period}`,
                "checkbox"
            );

            createCell(row, checkbox, "text-center");
        });

        const removeButton = document.createElement("button");
        removeButton.type = "button";
        removeButton.className =
            "btn btn-sm btn-outline-danger prescriber-remove-medicine";
        removeButton.title = "Remove medicine";
        removeButton.innerHTML = '<i class="bi bi-trash"></i>';

        createCell(row, removeButton);

        medicineBody.appendChild(row);
        refreshMedicineRows();
    }

    function refreshMedicineRows() {
        const rows = medicineBody.querySelectorAll("tr");

        rows.forEach(function (row, index) {
            const serial = row.querySelector(".prescriber-serial");

            if (serial) {
                serial.textContent = index + 1;
            }

            row.querySelectorAll("input, select").forEach(function (field) {
                const match = field.name.match(
                    /^medicines\[\d+]\.(.+)$/
                );

                if (match) {
                    field.name = `medicines[${index}].${match[1]}`;
                }
            });
        });

        noMedicineMessage.style.display = rows.length === 0 ? "" : "none";
    }

    addButton.addEventListener("click", addMedicineRow);

    medicineBody.addEventListener("click", function (event) {
        const removeButton = event.target.closest(
            ".prescriber-remove-medicine"
        );

        if (!removeButton) {
            return;
        }

        const row = removeButton.closest("tr");

        if (row) {
            row.remove();
            refreshMedicineRows();
        }
    });

    // --------------------------------------------------
    // BMI CALCULATION
    // Height format: 5.7 means 5 feet 7 inches.
    // Also accepts a decimal height in metres, e.g. 1.70.
    // --------------------------------------------------

    const heightInput = document.getElementById("prescriberHeight");
    const weightInput = document.getElementById("prescriberWeight");
    const bmiInput = document.getElementById("prescriberBmi");
    const bmiMessage = document.getElementById("prescriberBmiMessage");
    const doctorAdvice = document.getElementById("prescriberDoctorAdvice");

    const advicePrefix = "ওজন";
    const weightAdvicePattern = /^\s*ওজন\s+[\d.]+\s*kg\s+(?:কমাতে হবে|বাড়াতে হবে)\s*$/gm;

    function cleanNumber(value) {
        return Number.parseFloat(String(value).trim());
    }

    function getHeightInMetres(value) {
        const text = String(value || "").trim();

        if (!text) {
            return NaN;
        }

        // Feet.inches format, e.g. 5.7 means 5 ft 7 in.
        const feetInches = text.match(/^(\d+)\.(\d{1,2})$/);

        if (feetInches) {
            const feet = Number(feetInches[1]);
            const inches = Number(feetInches[2]);

            if (inches > 11) {
                return NaN;
            }

            return ((feet * 12) + inches) * 0.0254;
        }

        // Metres format, e.g. 1.70.
        const metres = cleanNumber(text);

        return metres > 0.5 && metres < 2.8 ? metres : NaN;
    }

    function updateWeightAdvice(message) {
        if (!doctorAdvice) {
            return;
        }

        const currentText = doctorAdvice.value || "";

        // Remove a previous automatically generated weight message.
        const cleanedText = currentText
            .replace(weightAdvicePattern, "")
            .trim();

        if (!message) {
            doctorAdvice.value = cleanedText;
            return;
        }

        doctorAdvice.value = cleanedText
            ? `${cleanedText}\n${message}`
            : message;
    }

    function calculateBMI() {
        const height = getHeightInMetres(heightInput.value);
        const weight = cleanNumber(weightInput.value);

        if (!Number.isFinite(height) ||
            !Number.isFinite(weight) ||
            height <= 0 ||
            weight <= 0) {

            bmiInput.value = "";
            bmiMessage.textContent = "";
            bmiMessage.style.color = "";
            updateWeightAdvice(null);
            return;
        }

        const bmi = weight / (height * height);
        bmiInput.value = bmi.toFixed(1);

        if (bmi < 18.5) {
            const targetWeight = 18.5 * height * height;
            const gain = Math.max(0, targetWeight - weight);

            bmiMessage.textContent =
                `ওজন কম। প্রায় ${gain.toFixed(1)} kg বাড়াতে হবে।`;
            bmiMessage.style.color = "#000000";

            updateWeightAdvice(
                `${advicePrefix} ${gain.toFixed(1)} kg বাড়াতে হবে`
            );
        } else if (bmi >= 25) {
            const targetWeight = 24.9 * height * height;
            const loss = Math.max(0, weight - targetWeight);

            bmiMessage.textContent =
                `ওজন বেশি। প্রায় ${loss.toFixed(1)} kg কমাতে হবে।`;
            bmiMessage.style.color = "#dc3545";

            updateWeightAdvice(
                `${advicePrefix} ${loss.toFixed(1)} kg কমাতে হবে`
            );
        } else {
            bmiMessage.textContent = "BMI স্বাভাবিক সীমার মধ্যে আছে।";
            bmiMessage.style.color = "#198754";
            updateWeightAdvice(null);
        }
    }

    if (heightInput && weightInput && bmiInput && bmiMessage) {
        heightInput.addEventListener("input", calculateBMI);
        heightInput.addEventListener("change", calculateBMI);
        weightInput.addEventListener("input", calculateBMI);
        weightInput.addEventListener("change", calculateBMI);

        calculateBMI();
    }

    refreshMedicineRows();
});
