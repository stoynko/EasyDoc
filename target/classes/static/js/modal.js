function openModal() {
    const modal = document.getElementById('medicament-modal');
    if (!modal) return;

    modal.classList.add('is-open');
    document.body.classList.add('modal-open');
}

function closeModal() {
    const modal = document.getElementById('medicament-modal');
    if (!modal) return;

    modal.classList.remove('is-open');
    document.body.classList.remove('modal-open');
}

window.openModal = openModal;
window.closeModal = closeModal;

document.addEventListener('DOMContentLoaded', () => {
    const overlay = document.getElementById('medicament-modal');
    if (!overlay) return;

    overlay.addEventListener('click', (event) => {
        if (event.target === overlay) {
            closeModal();
        }
    });

    document.addEventListener('keydown', (event) => {
        if ((event.key === 'Escape' || event.key === 'Esc') &&
            overlay.classList.contains('is-open')) {
            closeModal();
        }
    });
});