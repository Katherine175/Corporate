function abrirModalRegla() {
    const modal = document.getElementById('modalRegla');
    if (modal) {
        const form = document.getElementById('reglaForm');
        if (form) form.reset();
        const idField = document.getElementById('reglaId');
        if (idField) idField.value = '';
        const title = document.getElementById('modalTitle');
        if (title) title.textContent = 'Nueva Regla de Negocio';

        modal.classList.add('active');
    }
}

function cerrarModalRegla() {
    const modal = document.getElementById('modalRegla');
    if (modal) {
        modal.classList.remove('active');
    }
}

function editarRegla(btn) {
    const modal = document.getElementById('modalRegla');
    const form = document.getElementById('reglaForm');
    const title = document.getElementById('modalTitle');

    document.getElementById('reglaId').value = btn.getAttribute('data-id');
    form.querySelector('[name="nombre"]').value = btn.getAttribute('data-nombre');
    form.querySelector('[name="tipo"]').value = btn.getAttribute('data-tipo');
    form.querySelector('[name="umbral"]').value = btn.getAttribute('data-umbral');
    form.querySelector('[name="accion"]').value = btn.getAttribute('data-accion');

    if (title) title.textContent = 'Editar Regla';
    if (modal) modal.classList.add('active');
}

function confirmarEliminacion() {
    return confirm('¿Está seguro de que desea eliminar esta regla?');
}

function validarLongitud(inputElement) {
    if (inputElement.value.length < 5) {
        inputElement.style.borderColor = '#DC2626';
    } else {
        inputElement.style.borderColor = '#10B981';
    }
}

function actualizarUmbralSugerido(selectElement) {
    const umbralInput = document.getElementById('umbral');
    if (umbralInput) {
        if (selectElement.value === 'Energía') {
            umbralInput.value = 5000;
        } else if (selectElement.value === 'Agua') {
            umbralInput.value = 1000;
        } else if (selectElement.value === 'Costo') {
            umbralInput.value = 20000;
        }
    }
}

function validarReglaForm() {
    const umbral = document.getElementById('umbral');
    if (umbral && umbral.value <= 0) {
        alert("El umbral debe ser mayor a 0");
        umbral.style.borderColor = '#DC2626';
        return false;
    }
    return true;
}

document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('modalRegla');
    window.addEventListener('click', function (e) {
        if (e.target === modal) {
            cerrarModalRegla();
        }
    });
});
