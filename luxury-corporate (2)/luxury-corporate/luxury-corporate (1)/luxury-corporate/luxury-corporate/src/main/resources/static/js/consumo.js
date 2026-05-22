
function confirmarSimulacion() {
    return confirm("¿Generar datos de prueba aleatorios?");
}

function validarSede(inputElement) {
    if (inputElement.value.trim().length < 3) {
        inputElement.style.borderColor = '#DC2626';
    } else {
        inputElement.style.borderColor = '#10B981';
    }
}

function actualizarMontoBase(selectElement) {
    const montoInput = document.getElementById('monto');
    if (montoInput) {
        if (selectElement.value === 'Energía') {
            montoInput.value = 1500.00;
        } else if (selectElement.value === 'Agua') {
            montoInput.value = 300.00;
        } else if (selectElement.value === 'Gas') {
            montoInput.value = 800.00;
        } else {
            montoInput.value = '';
        }
    }
}

function validarConsumoForm() {
    const monto = document.getElementById('monto');
    if (monto && parseFloat(monto.value) <= 0) {
        alert("El monto debe ser mayor a 0");
        monto.style.borderColor = '#DC2626';
        return false;
    }
    return true;
}

function editarConsumo(btn) {
    const modal = document.getElementById('modalEditar');
    if (modal) {
        document.getElementById('consumoIdEditar').value = btn.getAttribute('data-id');
        document.getElementById('editarSede').value = btn.getAttribute('data-sede');
        document.getElementById('editarTipoRecurso').value = btn.getAttribute('data-tipo');
        document.getElementById('editarMonto').value = btn.getAttribute('data-monto');
        modal.classList.add('active');
    }
}

function cerrarModalEditarConsumo() {
    const modal = document.getElementById('modalEditar');
    if (modal) {
        modal.classList.remove('active');
    }
}

function validarEdicionConsumo() {
    const monto = document.getElementById('editarMonto');
    if (monto && parseFloat(monto.value) <= 0) {
        alert("El monto editado debe ser mayor a 0");
        monto.style.borderColor = '#DC2626';
        return false;
    }
    return true;
}

document.addEventListener('DOMContentLoaded', () => {
    const modal = document.getElementById('modalEditar');
    window.addEventListener('click', function (e) {
        if (e.target === modal) {
            cerrarModalEditarConsumo();
        }
    });


    const msgs = document.querySelectorAll('.msg-ok, .msg-error');
    msgs.forEach(msg => {
        setTimeout(() => {
            msg.style.display = 'none';
        }, 4000);
    });
});
