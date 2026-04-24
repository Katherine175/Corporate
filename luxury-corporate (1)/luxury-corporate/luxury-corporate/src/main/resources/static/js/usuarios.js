function abrirModal() {
    const modal = document.getElementById('modalNuevoUsuario');
    if (modal) {
        modal.classList.add('active');
    }
}

function cerrarModal() {
    const modal = document.getElementById('modalNuevoUsuario');
    if (modal) {
        modal.classList.remove('active');
    }
}

function cambiarColorRol(selectElement) {
    const val = selectElement.value;
    if (val === 'ADMINISTRADOR') {
        selectElement.style.borderLeft = '4px solid #1E40AF';
    } else if (val === 'SUPERVISOR') {
        selectElement.style.borderLeft = '4px solid #5B21B6';
    } else if (val === 'OPERADOR') {
        selectElement.style.borderLeft = '4px solid #065F46';
    } else {
        selectElement.style.borderLeft = '1px solid #D1D5DB';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('usuarioForm');
    if (form) {
        form.onsubmit = function (e) {
            let valid = true;
            const nombre = document.getElementById('nombre');
            const email = document.getElementById('email');

            const errorNombre = document.getElementById('errorNombre');
            const errorEmail = document.getElementById('errorEmail');

            const nameRegex = /^[A-Za-zÁÉÍÓÚáéíóúÑñ]{2,}\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]{2,}\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]{2,}(\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]{2,})*$/;
            if (nombre && !nameRegex.test(nombre.value.trim())) {
                nombre.style.borderColor = '#DC2626';
                if (errorNombre) errorNombre.innerText = "Ingrese nombre y dos apellidos.";
                valid = false;
            } else if (nombre) {
                nombre.style.borderColor = '';
                if (errorNombre) errorNombre.innerText = "";
            }

            const emailRegex = /^[a-zA-Z0-9._%+-]+@smartcorp\.com$/;
            if (email && !emailRegex.test(email.value.trim())) {
                email.style.borderColor = '#DC2626';
                if (errorEmail) errorEmail.innerText = "Dominio inválido.";
                valid = false;
            } else if (email) {
                email.style.borderColor = '';
                if (errorEmail) errorEmail.innerText = "";
            }

            if (!valid) {
                e.preventDefault();
                return false;
            }
            return true;
        };
    }

    const modal = document.getElementById('modalNuevoUsuario');
    window.onclick = function (e) {
        if (e.target === modal) {
            cerrarModal();
        }
    };

    const toast = document.getElementById('toastMensaje');
    if (toast) {
        setTimeout(() => {
            toast.style.display = 'none';
        }, 4000);
    }


    const modalError = document.getElementById('modalError');
    if (modalError && modal) {
        modal.classList.add('active');
    }
});
