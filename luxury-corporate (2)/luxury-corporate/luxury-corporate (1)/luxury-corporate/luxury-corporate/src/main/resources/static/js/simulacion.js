document.addEventListener('DOMContentLoaded', () => {
    const montoInput = document.getElementById('monto');
    const sedeInput = document.getElementById('sede');
    const tipoRecursoSelect = document.getElementById('tipoRecurso');
    const form = document.querySelector('form');

    if (montoInput) {
        montoInput.addEventListener('input', () => {
            const valor = parseFloat(montoInput.value);
            if (valor <= 0 && montoInput.value !== '') {
                montoInput.style.borderColor = '#f44336';
            } else {
                montoInput.style.borderColor = '';
            }
        });

        form.addEventListener('submit', (e) => {
            const valor = parseFloat(montoInput.value);
            if (!valor || valor <= 0) {
                e.preventDefault();
                montoInput.style.borderColor = '#f44336';
            }
        });
    }

    const generarButton = document.querySelector('form[action="/simular"] button');
    if (generarButton) {
        generarButton.addEventListener('click', () => {
        });
    }

    document.addEventListener('mouseleave', () => {
    });

    const links = document.querySelectorAll('a');
    links.forEach(link => {
        link.addEventListener('click', () => {
        });
    });
});
