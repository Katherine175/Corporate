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

// También restablecer formulario cuando se cierre el modal
function resetFormToCreateGlobal() {
    const form = document.getElementById('usuarioForm');
    if (!form) return;
    form.action = '/usuarios/crear';
    const inputId = document.getElementById('usuarioId');
    if (inputId) inputId.value = '';
    const inputNombre = document.getElementById('nombre');
    const inputEmail = document.getElementById('email');
    const selectRol = document.getElementById('rol');
    const inputUbicacion = document.getElementById('ubicacion');
    const selectEstado = document.getElementById('estado');
    const modalTitle = document.getElementById('usuarioModalTitle');
    const submitBtn = document.getElementById('submitUsuarioBtn');
    if (inputNombre) inputNombre.value = '';
    if (inputEmail) inputEmail.value = '';
    if (selectRol) {
        selectRol.value = '';
        cambiarColorRol(selectRol);
    }
    if (inputUbicacion) inputUbicacion.value = '';
    if (selectEstado) selectEstado.value = '';
    if (modalTitle) modalTitle.innerText = 'Nuevo Usuario';
    if (submitBtn) submitBtn.innerText = 'Crear Usuario';
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
            const rol = document.getElementById('rol');

            const errorNombre = document.getElementById('errorNombre');
            const errorEmail = document.getElementById('errorEmail');

            // Validar nombre: DEBE tener nombre + 2 apellidos (3 palabras mínimo)
            if (nombre) {
                const nombreTrimmed = nombre.value.trim();
                const partes = nombreTrimmed.split(/\s+/).filter(p => p.length > 0);
                if (partes.length < 3) {
                    nombre.style.borderColor = '#DC2626';
                    if (errorNombre) errorNombre.innerText = "Ingrese nombre y dos apellidos (mínimo 3 palabras).";
                    valid = false;
                } else {
                    nombre.style.borderColor = '';
                    if (errorNombre) errorNombre.innerText = "";
                }
            }

            // Validar email: DEBE terminar con @smartcorp.com
            const emailRegex = /^[a-zA-Z0-9._%+-]+@smartcorp\.com$/;
            if (email) {
                if (!emailRegex.test(email.value.trim())) {
                    email.style.borderColor = '#DC2626';
                    if (errorEmail) errorEmail.innerText = "Debe ser un correo @smartcorp.com";
                    valid = false;
                } else {
                    email.style.borderColor = '';
                    if (errorEmail) errorEmail.innerText = "";
                }
            }

            // Validar rol: es obligatorio
            if (rol && !rol.value.trim()) {
                rol.style.borderColor = '#DC2626';
                if (errorEmail) errorEmail.innerText = "Seleccione un rol";
                valid = false;
            } else if (rol) {
                rol.style.borderColor = '';
            }

            // Si no hay estado seleccionado, asignar ACTIVO por defecto
            const estado = document.getElementById('estado');
            if (estado && !estado.value.trim()) {
                estado.value = 'ACTIVO';
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

    // Manejar botones de editar: rellenar formulario y cambiar acción
    const editButtons = document.querySelectorAll('.btn-edit');
    editButtons.forEach(btn => {
        btn.addEventListener('click', function () {
            const id = this.dataset.id;
            const nombre = this.dataset.nombre || '';
            const email = this.dataset.email || '';
            const rol = this.dataset.rol || '';
            const ubicacion = this.dataset.ubicacion || '';
            const estado = this.dataset.estado || '';

            // Rellenar campos
            const inputId = document.getElementById('usuarioId');
            const inputNombre = document.getElementById('nombre');
            const inputEmail = document.getElementById('email');
            const selectRol = document.getElementById('rol');
            const inputUbicacion = document.getElementById('ubicacion');
            const selectEstado = document.getElementById('estado');
            const modalTitle = document.getElementById('usuarioModalTitle');
            const submitBtn = document.getElementById('submitUsuarioBtn');

            if (inputId) inputId.value = id;
            if (inputNombre) inputNombre.value = nombre;
            if (inputEmail) inputEmail.value = email;
            if (selectRol) {
                // A veces el rol viene como objeto o como texto; aseguramos el valor
                selectRol.value = rol && rol.name ? rol.name : rol;
                cambiarColorRol(selectRol);
            }
            if (inputUbicacion) inputUbicacion.value = ubicacion;
            if (selectEstado) {
                // El estado puede venir como objeto o como texto
                selectEstado.value = estado && estado.name ? estado.name : estado;
            }

            // Cambiar acción del form a editar
            const form = document.getElementById('usuarioForm');
            if (form) {
                form.action = '/usuarios/editar/' + encodeURIComponent(id);
            }
            if (modalTitle) modalTitle.innerText = 'Editar Usuario';
            if (submitBtn) submitBtn.innerText = 'Guardar cambios';

            // Abrir modal
            abrirModal();
        });
    });

    // Al cerrar el modal, limpiar y restablecer el formulario para crear nuevo
    const btnCerrar = document.getElementById('btnCerrarModal');
    const btnCancelar = document.getElementById('btnCancelar');
    function resetFormToCreate() {
        const form = document.getElementById('usuarioForm');
        if (!form) return;
        form.action = '/usuarios/crear';
        const inputId = document.getElementById('usuarioId');
        if (inputId) inputId.value = '';
        const inputNombre = document.getElementById('nombre');
        const inputEmail = document.getElementById('email');
        const selectRol = document.getElementById('rol');
        const inputUbicacion = document.getElementById('ubicacion');
        const selectEstado = document.getElementById('estado');
        const modalTitle = document.getElementById('usuarioModalTitle');
        const submitBtn = document.getElementById('submitUsuarioBtn');
        if (inputNombre) inputNombre.value = '';
        if (inputEmail) inputEmail.value = '';
        if (selectRol) {
            selectRol.value = '';
            cambiarColorRol(selectRol);
        }
        if (inputUbicacion) inputUbicacion.value = '';
        if (selectEstado) selectEstado.value = '';
        if (modalTitle) modalTitle.innerText = 'Nuevo Usuario';
        if (submitBtn) submitBtn.innerText = 'Crear Usuario';
    }

    if (btnCerrar) btnCerrar.addEventListener('click', resetFormToCreate);
    if (btnCancelar) btnCancelar.addEventListener('click', resetFormToCreate);

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
