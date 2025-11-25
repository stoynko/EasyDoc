function openAppointmentModal() {
    const modal = document.getElementById('appointment-modal');
    if (!modal) {
        return;
    }

    modal.classList.add('is-open');
    document.body.classList.add('modal-open');
}

function closeAppointmentModal() {
    const modal = document.getElementById('appointment-modal');
    if (!modal) {
        return;
    }

    modal.classList.remove('is-open');
    document.body.classList.remove('modal-open');
}

window.openAppointmentModal = openAppointmentModal;
window.closeAppointmentModal = closeAppointmentModal;