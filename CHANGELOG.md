# CHANGELOG - VIDTRACKER

## [1.0.0] - 
#### Added

- Creación de Base de Datos usando H2 - @pcc00031
- Mapeado de entidades - @pcc00031
- Implementación repositorio rastreadores - @cga00037
- Implementación repositorio usuarios - @pcc00031
- Adaptación del servicio a JPA - @pcc00031 , @cga00037
- Adaptación tests a JPA - @pcc00031 , @cga00037

### Fixed

- Corrección método testNotificarContactoCercano() - @pcc00031
- Añadido atributo solo lectura a algunos métodos en repositorioUsuarios
- Modificado método nuevoContacto()
- Lista de Contactos en entidad Usuario mappeada con usuarioPropietario - @pcc00031
- Carga LAZY añadida a usuarioPropietario en entidad Contacto - @pcc00031
- testAccesoServicioVidTracker eliminado - @pcc00031

## [0.0.2] - 2021-11-22
#### Added

 - Creación de Base de Datos usando H2 - @pcc00031
 - Mapeado de entidades - @pcc00031
 - Implementación repositorio rastreadores - @cga00037
 - Implementación repositorio usuarios - @pcc00031
 - Adaptación del servicio a JPA - @pcc00031 , @cga00037
 - Adaptación tests a JPA - @pcc00031 , @cga00037

### Fixed

 - Agregado UML al archivo README.md - @pcc00031
 - Eliminado atributo nombre en el servicio - @pcc00031
 - Limpieza de archivos innecesarios - @pcc00031
 - Actualización de atributos numPositivos y numCuraciones en entidad rastreador - @pcc00031
 - Constructor de contacto remodelado - @cga00037
 - Eliminados constructores innecesarios en Usuario - @cga00037
 - Eliminado verTarjetas y verUsuarios en el servicio - @pcc00031
 - Añadidos métodos auxiliares para testear - @pcc00031 , @cga00037


## [0.0.1] - 2021-10-25
#### Added

 - Implementación de clases Contacto y Usuario - @pcc00031
 - Implementación de clase Rastreador - @cga00037
 - Implementación de operaciones relacionadas con Usuario en el servicio - @pcc00031
 - Implementación de operaciones relacionadas con Rastreador en el servicio - @cga00037
 - Implementación de operaciones relacionadas con Contacto en el servicio - @pcc00031 , @cga00037
 - Realización de métodos con fines estadísticos - @pcc00031 , @cga00037
 - Testing de todas las operaciones - @pcc00031 , @cga00037