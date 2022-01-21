$(document).ready(function () {
    loginInfo();
});

async function logout() {
    let dni = document.getElementsByClassName('sb-sidenav-footer')[0].innerText.substr(14, 12)
    const request = await fetch('http://localhost:8080/vidtracker/logout', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });
}

async function cargarRastreador() {
    let dni = document.getElementsByClassName('sb-sidenav-footer')[0].innerText.substr(14, 12)
    const request = await fetch('http://localhost:8080/vidtracker/rastreadores/' + dni, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });
    const rastreador = await request.json();
    console.log(rastreador);

    let listadoCuraciones = '';
    listadoCuraciones += ' - Nombre: ' + rastreador.nombre + '<br>' +
        '- DNI: ' + rastreador.dni + '<br>' +
        ' - Telefono: ' + rastreador.telefono + '<br>' +
        ' - Num curaciones: ' + rastreador.numCuraciones;

    let listadoPositivos = '';
    listadoPositivos += ' - Nombre: ' + rastreador.nombre + '<br>' +
        '- DNI: ' + rastreador.dni + '<br>' +
        ' - Telefono: ' + rastreador.telefono + '<br>' +
        ' - Num positivos: ' + rastreador.numPositivos;

    document.querySelector('#positivos').outerHTML = listadoPositivos;
    document.querySelector('#curaciones').outerHTML = listadoCuraciones;
}

async function cargarUsuario() {
    let uuid = document.querySelector('#busUsu').value;
    const request = await fetch('http://localhost:8080/vidtracker/usuarios/' + uuid, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    });
    const usuario = await request.json();
    console.log(usuario);

    let userInfo = '';
    userInfo += ' - Telefono: ' + usuario.telefono + '<br>';

    document.querySelector('#usuario').outerHTML = userInfo;
}

async function registrarRastreador() {
    let datos = {};
    datos.telefono = document.querySelector('#tlfnRas').value;
    datos.nombre = document.querySelector('#nombreRas').value;
    datos.dni = document.querySelector('#dniRas').value;
    datos.clave = document.querySelector('#claveRas').value;

    const request = await fetch('http://localhost:8080/vidtracker/rastreadores', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datos)
    });
    document.querySelector('#regRas').outerHTML = 'Rastreador registrado';
}

async function registrarUsuario() {
    let datosU = {};
    datosU.telefono = document.querySelector('#tlfnUsu').value;

    const request = await fetch('http://localhost:8080/vidtracker/usuarios', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(datosU)
    });
    const usuario = await request.json();
    document.querySelector('#usu').outerHTML = 'Su UUID es: ' + usuario;
}

async function notificarUsuario() {
    let notificacion = {};
    notificacion.fecha = document.querySelector('#fechaPos').value;
    notificacion.positivo = document.querySelector('#pos').checked;
    let uuid = document.querySelector('#busUsu').value;
    const request = await fetch('http://localhost:8080/vidtracker/usuarios/' + uuid + '/notificacion', {
        method: 'POST',

        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
            //TODO autenticacion
        },
        body: JSON.stringify(notificacion)
    });
    const usuario = await request.json();
    console.log(usuario);
    if (usuario !== true) {
        document.querySelector('#noti').outerHTML = 'Usuario marcado como curado';
    } else {
        document.querySelector('#noti').outerHTML = 'Usuario marcado como positivo';
    }
}

async function loginInfo() {

    const request = await fetch('http://localhost:8080/vidtracker/rastreador', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const login = await request.text();
    // dni = request.text();
    //console.log(login);
    document.querySelector('#login').outerHTML = 'Logeado como: ' + login;

}

//FIXME notificarContactos parametrizar

async function obtenerContactos() {
    let uuid = document.querySelector('#busUsu').value;
    const request = await fetch('http://localhost:8080/vidtracker/usuarios/' + uuid + '/contactos', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const c = await request.json();
    let contactos = '';
    for (let cs of c) {
        contactos += ' - UUID: ' + cs.contacto + '<br>' +
            'Fecha contacto: ' + cs.fecha_cont + '<br>';
    }

    document.querySelector('#contactos').outerHTML = 'Sus contactos son: ' + contactos;
}

async function notificarContacto() {
    let contactos = {};
    contactos.fecha_cont = document.querySelector('#fechaCon').value;
    contactos.contacto = document.querySelector('#uuidC').value;

    let cs = [contactos];

    let uuid = document.querySelector('#busUsu').value;
    const request = await fetch('http://localhost:8080/vidtracker/usuarios/' + uuid + '/contactos', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(cs)
    });
}

async function infectados() {
    let bool = document.querySelector('#posi').checked;
    const request = await fetch('http://localhost:8080/vidtracker/estadisticas/positivos/?actual=' + bool, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const p = await request.json();
    let positivos = p;
    if (bool == true)
        document.querySelector('#infectados').outerHTML = 'Los positivos actuales son: ' + positivos;
    if (bool == false)
        document.querySelector('#infectados').outerHTML = 'Los positivos totales son: ' + positivos;

}

async function numPosDias() {
    let dias = document.querySelector('#numPosi').value;
    const request = await fetch('http://localhost:8080/vidtracker/estadisticas/positivo/?dias=' + dias, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const p = await request.json();
    let positivos = p;
    document.querySelector('#numPosDias').outerHTML = 'Los positivos en los últimos ' + dias +
        ' días son: ' + positivos;
}

async function media() {
    const request = await fetch('http://localhost:8080/vidtracker/estadisticas/mediaContagio', {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
    });
    const p = await request.json();
    let media = p;
    document.querySelector('#media').outerHTML = 'La media de contagio es: ' + media;
}