document.querySelectorAll('[data-bs-toggle="modal"]').forEach(button => {
    button.addEventListener('click', function() {
        const categoria = this.getAttribute('data-categoria');
        const dia = this.getAttribute('data-dia');
        const modalId = `modal${categoria}${dia}`;

        fetch(`/spring/recetasModal?categoria=${categoria}&dia=${dia}`)
            .then(response => response.text())
            .then(html => {
                const modal = document.getElementById(modalId);
                if (modal) {
                    const modalBody = modal.querySelector('.modal-body');
                    if (modalBody) {
                        modalBody.innerHTML = html;
                        console.log('Contenido HTML insertado en el modal');
                        agregarEventListenersRecetas(modalId, categoria, dia);
                    }
                } else {
                    console.error('Modal no encontrado:', modalId);
                }
            })
    });
});

function agregarEventListenersRecetas(modalId, categoria, dia) {
    document.querySelectorAll(`#${modalId} .receta-card`).forEach(card => {
        card.addEventListener('click', function() {
            const titulo = this.querySelector('.card-title').textContent;
            seleccionarReceta(titulo, modalId, categoria, dia);
        });
    });
}

function seleccionarReceta(titulo, modalId, categoria, dia) {

    console.log('Intentando actualizar el título a:', titulo);
    const nombreRecetaSeleccionada = document.getElementById(`nombreRecetaSeleccionada${categoria}${dia}`);
    if (nombreRecetaSeleccionada) {
        nombreRecetaSeleccionada.innerText = titulo;
        console.log('Título actualizado');
}

    const modal = bootstrap.Modal.getInstance(document.getElementById(modalId));
    if (modal) {
        modal.hide();
        console.log('Modal cerrado');
    }
}







