document.addEventListener('DOMContentLoaded', function() {
    let sidebar = document.getElementById('sidebar');
    let btnNav = document.getElementById('sidebarToggle');
    let body = document.body;

    btnNav.addEventListener('click', function() {
        sidebar.classList.toggle('show');
        body.classList.toggle('sidebar-open');
    });

    // Cierra el sidebar al hacer clic fuera de él en pantallas pequeñas
    document.addEventListener('click', function(event) {
        let clickEnElSidebar = sidebar.contains(event.target);
        let clickEnBtnNav = btnNav.contains(event.target);

        if (!clickEnElSidebar && !clickEnBtnNav && window.innerWidth < 992) {
            sidebar.classList.remove('show');
            body.classList.remove('sidebar-open');
            }
    });
});