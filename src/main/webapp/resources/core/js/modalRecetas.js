let detallePlanificadorData = [];

document.querySelectorAll('[data-bs-toggle="modal"]').forEach(button => {
    button.addEventListener('click', function() {
        const categoria = this.getAttribute('data-categoria');
        const dia = this.getAttribute('data-dia');
        const modalId = `modal${categoria}${dia}`;

        //solicitud al backend para obtener las recetas y las inserta en el modal
        fetch(`/spring/recetasModal?categoria=${categoria}&dia=${dia}`)
            .then(response => response.text())
            .then(html => {
                const modal = document.getElementById(modalId);
                if (modal) {
                    const modalBody = modal.querySelector('.modal-body');
                    if (modalBody) {
                        modalBody.innerHTML = html;
                        agregarEventListenersRecetas(modalId, categoria, dia);
                    }
                }
            });
    });
});

function agregarEventListenersRecetas(modalId, categoria, dia) {
    document.querySelectorAll(`#${modalId} .receta-card`).forEach(card => {
        card.addEventListener('click', function() {
            const titulo = this.querySelector('.card-title').textContent;
            const recetaId = this.getAttribute('data-receta-id');
            seleccionarReceta(titulo, modalId, categoria, dia, recetaId);
        });
    });
}

function seleccionarReceta(titulo, modalId, categoria, dia, recetaId) {

    const detallePlanificador = { dia, categoria, recetaId };
    detallePlanificadorData.push(detallePlanificador);

    //muestra el título de la receta seleccionada dinámicamente
    const nombreRecetaSeleccionada = document.getElementById(`nombreRecetaSeleccionada${categoria}${dia}`);
    const spanReceta = document.querySelector(`.receta-${categoria}-${dia}`);
    if (nombreRecetaSeleccionada) {
    if (spanReceta){
    spanReceta.innerText = "";
    }
        nombreRecetaSeleccionada.innerText = titulo;
    }

    //cierra el modal después de seleccionar la receta
    const modal = bootstrap.Modal.getInstance(document.getElementById(modalId));
    if (modal) modal.hide();

    const dias = detallePlanificadorData.map(item => item.dia.toUpperCase().trim());
    const categorias = detallePlanificadorData.map(item => item.categoria);
    const recetas = detallePlanificadorData.map(item => item.recetaId);

    const diasInput = document.getElementById('diasSeleccionados');
    const recetasInput = document.getElementById('recetasSeleccionadas');
    const categoriasInput = document.getElementById('categoriasSeleccionadas');

    if (diasInput && recetasInput && categoriasInput) {
        diasInput.value = dias.join(',');
        recetasInput.value = recetas.join(',');
        categoriasInput.value = categorias.join(',');
    }

    form.submit();
    }