let detallePlanificadorData = [];

// Función que se llama cuando el usuario hace clic en un botón para abrir el modal
document.querySelectorAll('[data-bs-toggle="modal"]').forEach(button => {
    button.addEventListener('click', function() {
        const categoria = this.getAttribute('data-categoria');
        const dia = this.getAttribute('data-dia');
        const modalId = `modal${categoria}${dia}`;
        console.log(`Modal seleccionado - Categoria: ${categoria}, Dia: ${dia}, ID: ${modalId}`);

        // Realiza una solicitud al backend para obtener las recetas y las inserta en el modal
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
            });
    });
});

// Agrega eventos de clic a cada tarjeta de receta en el modal
function agregarEventListenersRecetas(modalId, categoria, dia) {
    document.querySelectorAll(`#${modalId} .receta-card`).forEach(card => {
        card.addEventListener('click', function() {
            const titulo = this.querySelector('.card-title').textContent;
            const recetaId = this.getAttribute('data-receta-id'); // Captura el ID de la receta
            console.log(`Receta seleccionada - Título: ${titulo}, ID: ${recetaId}`);
            seleccionarReceta(titulo, modalId, categoria, dia, recetaId);
        });
    });
}

// Función para procesar la selección de una receta
function seleccionarReceta(titulo, modalId, categoria, dia, recetaId) {
    console.log(`Receta seleccionada - Título: ${titulo}, ID: ${recetaId}, Día: ${dia}, Categoría: ${categoria}`);

    // Crear un objeto DetallePlanificador y agregarlo al array de detalles
    const detallePlanificador = { dia, recetaId };
    detallePlanificadorData.push(detallePlanificador);

    // Mostrar el título de la receta seleccionada en la interfaz
    const nombreRecetaSeleccionada = document.getElementById(`nombreRecetaSeleccionada${categoria}${dia}`);
    if (nombreRecetaSeleccionada) {
        nombreRecetaSeleccionada.innerText = titulo;
        console.log('Título actualizado');
    }

    // Cierra el modal después de seleccionar la receta
    const modal = bootstrap.Modal.getInstance(document.getElementById(modalId));
    if (modal) modal.hide();
    console.log(detallePlanificadorData);
}

    const dias = detallePlanificadorData.map(item => item.dia);
    const recetas = detallePlanificadorData.map(item => item.recetaId);
    console.log(dias);
    console.log(recetas);

// Al hacer clic en el botón de "Guardar Planificador"
document.getElementById('guardarPlanificadorBtn').addEventListener('click', function() {

    // Convertimos el array a listas de días y recetas (en lugar de JSON)
    const dias = detallePlanificadorData.map(item => item.dia);
    const recetas = detallePlanificadorData.map(item => item.recetaId);
    console.log(dias);
    console.log(recetas);


    // Obtener el formulario
    const form = document.getElementById('formPlanificador');

    // Vaciar el formulario antes de agregar nuevos inputs
    form.innerHTML = '';

    // Agregar los datos al formulario como inputs ocultos (para enviar las listas)
    dias.forEach((dia, index) => {
        let inputDia = document.createElement('input');
        inputDia.type = 'hidden';
        inputDia.name = `dias[${index}]`; // Nombre para que sea un array
        inputDia.value = dia;

        let inputRecetaId = document.createElement('input');
        inputRecetaId.type = 'hidden';
        inputRecetaId.name = `recetas[${index}]`; // Nombre para que sea un array
        inputRecetaId.value = recetas[index];

        // Agregar los inputs al formulario
        form.appendChild(inputDia);
        form.appendChild(inputRecetaId);

        console.log("Días a enviar:", inputDia.value);
        console.log("Recetas a enviar:", inputRecetaId.value);

    });

    console.log("Días a enviar:", dias);
    console.log("Recetas a enviar:", recetas);

    // Enviar el formulario
    form.submit();
});





/*let detallePlanificadorData = [];

// Función que se llama cuando el usuario hace clic en un botón para abrir el modal
document.querySelectorAll('[data-bs-toggle="modal"]').forEach(button => {
    button.addEventListener('click', function() {
        const categoria = this.getAttribute('data-categoria');
        const dia = this.getAttribute('data-dia');
        const modalId = `modal${categoria}${dia}`;
        console.log(`Modal seleccionado - Categoria: ${categoria}, Dia: ${dia}, ID: ${modalId}`);

        // Realiza una solicitud al backend para obtener las recetas y las inserta en el modal
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
            });
    });
});

// Agrega eventos de clic a cada tarjeta de receta en el modal
function agregarEventListenersRecetas(modalId, categoria, dia) {
    document.querySelectorAll(`#${modalId} .receta-card`).forEach(card => {
        card.addEventListener('click', function() {
            const titulo = this.querySelector('.card-title').textContent;
            const recetaId = this.getAttribute('data-receta-id'); // Captura el ID de la receta
            console.log(`Receta seleccionada - Título: ${titulo}, ID: ${recetaId}`);
            seleccionarReceta(titulo, modalId, categoria, dia, recetaId);
        });
    });
}

// Función para procesar la selección de una receta
function seleccionarReceta(titulo, modalId, categoria, dia, recetaId) {
    console.log(`Receta seleccionada - Título: ${titulo}, ID: ${recetaId}, Día: ${dia}, Categoría: ${categoria}`);

    // Crear un objeto DetallePlanificador y agregarlo al array de detalles
    const detallePlanificador = { dia, categoria, recetaId };
    detallePlanificadorData.push(detallePlanificador);

    // Mostrar el título de la receta seleccionada en la interfaz
    const nombreRecetaSeleccionada = document.getElementById(`nombreRecetaSeleccionada${categoria}${dia}`);
    if (nombreRecetaSeleccionada) {
        nombreRecetaSeleccionada.innerText = titulo;
        console.log('Título actualizado');
    }

    // Cierra el modal después de seleccionar la receta
    const modal = bootstrap.Modal.getInstance(document.getElementById(modalId));
    if (modal) modal.hide();
    console.log(detallePlanificadorData)
}
document.getElementById('guardarPlanificadorBtn').addEventListener('click', function() {

     // Convertimos el array a JSON como un string
     const detallePlanificadorJson = JSON.stringify(detallePlanificadorData);

     // Asignamos este string al input oculto
     document.getElementById('detallePlanificadorInput').value = detallePlanificadorJson;

     // Enviamos el formulario
     document.getElementById('formPlanificador').submit();
 });
*/

// Función para enviar el array de DetallePlanificador al backend
/*function guardarPlanificador() {
console.log(detallePlanificadorData)
    fetch('/spring/guardarPlanificador', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'  // Especifica que estás enviando JSON
        },
        body: JSON.stringify(detallePlanificadorData)  // Convierte el objeto a una cadena JSON
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al guardar el planificador');
        }
        return response.json();  // Si la respuesta es JSON, la procesas aquí
    })
    .then(data => {
        console.log('Planificador guardado correctamente:', data);
    })
    .catch(error => {
        console.error('Error al guardar el planificador:', error);
    });
}*/











