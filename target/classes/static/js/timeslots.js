function initializeTimeSlots() {
    const timeSlotButtons = document.querySelectorAll('.time-slot-btn');
    const selectedTimeInput = document.getElementById('selected-time');

    timeSlotButtons.forEach(button => {
        button.addEventListener('click', function() {

            if (this.classList.contains('unavailable')) {
                return;
            }

            document.querySelectorAll('.time-slot-btn').forEach(btn => {
                btn.classList.remove('active');
            });

            this.classList.add('active');

            if (selectedTimeInput) {
                selectedTimeInput.value = this.getAttribute('data-time');
            }
        });
    });
}

document.addEventListener('DOMContentLoaded', function() {
    initializeTimeSlots();

    const dateInput = document.getElementById('date');
    if (dateInput) {
        dateInput.addEventListener('change', function() {
            const selectedTimeInput = document.getElementById('selected-time');
            if (selectedTimeInput) {
                selectedTimeInput.value = '';
            }

            document.querySelectorAll('.time-slot-btn').forEach(btn => {
                btn.classList.remove('active');
            });
        });
    }
});

document.body.addEventListener('htmx:afterSwap', function(event) {
    if (event.detail.target.id === 'time-slots-container') {
        initializeTimeSlots();
    }
});