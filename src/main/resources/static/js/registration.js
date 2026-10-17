$(document).ready(function () {

    $('#patient\\.dateOfBirth').on('change', function () {
        const dobValue = $(this).val();

        if (!dobValue) {
            $('#patient\\.age').val('');
            return;
        }

        const dob = new Date(dobValue + 'T00:00:00');
        const today = new Date();

        let age = today.getFullYear() - dob.getFullYear();

        // Subtract one if the birthday hasn't occurred this year
        const birthdayNotReached =
            today.getMonth() < dob.getMonth() ||
            (today.getMonth() === dob.getMonth() &&
                today.getDate() < dob.getDate());

        if (birthdayNotReached) {
            age--;
        }

        // Prevent invalid future dates
        if (age < 0) {
            $('#patient\\.age').val('');
            return;
        }

        $('#patient\\.age').val(age);
    });

});