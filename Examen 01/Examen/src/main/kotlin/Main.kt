import java.time.LocalDate
import java.time.format.DateTimeFormatter
import models.Concesionaria
import models.Auto
import services.CRUD


fun main() {
    while (true) {
        println("\n=== Menú Principal ===")
        println("1. Crear Concesionaria")
        println("2. Listar Concesionarias")
        println("3. Modificar Concesionaria")
        println("4. Eliminar Concesionaria")
        println("5. Crear Auto")
        println("6. Listar Autos de Concesionaria")
        println("7. Modificar Auto")
        println("8. Eliminar Auto")
        println("9. Salir")
        print("Elige una opción: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> crearConcesionariaInteractivo()
            2 -> listarConcesionarias()
            3 -> actualizarConcesionariaInteractivo()
            4 -> eliminarConcesionariaInteractivo()
            5 -> crearAutoInteractivo()
            6 -> listarAutosDeConcesionaria()
            7 -> actualizarAutoInteractivo()
            8 -> eliminarAutoInteractivo()
            9 -> {
                println("¡Hasta luego!")
                break
            }
            else -> println("Opción inválida. Intenta de nuevo.")
        }
    }
}

fun crearConcesionariaInteractivo() {
    println("=== Crear Concesionaria ===")
    print("Nombre: ")
    val nombre = readlnOrNull() ?: return
    print("Dirección: ")
    val direccion = readlnOrNull() ?: return
    print("Fecha de Fundación (YYYY-MM-DD): ")
    val fecha = readlnOrNull()?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) } ?: return
    print("¿Está abierta? (true/false): ")
    val abierta = readlnOrNull()?.toBoolean() ?: return

    val concesionaria = Concesionaria(nombre, direccion, 0, fecha, abierta)
    CRUD.crearConcesionaria(concesionaria)
    println("Concesionaria creada exitosamente.")
}

fun listarConcesionarias() {
    println("=== Listar Concesionarias ===")
    val concesionarias = CRUD.leerConcesionarias()
    if (concesionarias.isEmpty()) {
        println("No hay concesionarias registradas.")
    } else {
        concesionarias.forEach {
            println("Nombre: ${it.nombre}, Dirección: ${it.direccion}, Autos: ${it.numeroAutos}, Fundación: ${it.fechaFundacion}, Abierta: ${it.abierta}")
        }
    }
}

fun crearAutoInteractivo() {
    println("=== Crear Auto ===")
    print("Nombre de la Concesionaria: ")
    val nombreConcesionaria = readlnOrNull() ?: return

    val concesionarias = CRUD.leerConcesionarias()
    if (concesionarias.none { it.nombre == nombreConcesionaria }) {
        println("Concesionaria no encontrada.")
        return
    }

    print("Marca: ")
    val marca = readlnOrNull() ?: return
    print("Modelo: ")
    val modelo = readlnOrNull() ?: return
    print("Año: ")
    val anio = readlnOrNull()?.toIntOrNull() ?: return
    print("Precio: ")
    val precio = readlnOrNull()?.toDoubleOrNull() ?: return
    print("¿Está disponible? (true/false): ")
    val disponible = readlnOrNull()?.toBoolean() ?: return

    val auto = Auto(marca, modelo, anio, precio, disponible)
    CRUD.crearAuto(nombreConcesionaria, auto)
    println("Auto creado exitosamente.")
}

fun listarAutosDeConcesionaria() {
    println("=== Listar Autos ===")
    print("Nombre de la Concesionaria: ")
    val nombreConcesionaria = readlnOrNull() ?: return

    val autos = CRUD.leerAutos(nombreConcesionaria)
    if (autos.isEmpty()) {
        println("No hay autos registrados para esta concesionaria.")
    } else {
        autos.forEach {
            println("Marca: ${it.marca}, Modelo: ${it.modelo}, Año: ${it.anio}, Precio: ${it.precio}, Disponible: ${it.disponible}")
        }
    }
}

fun actualizarConcesionariaInteractivo() {
    println("=== Modificar Concesionaria ===")
    print("Nombre actual de la concesionaria: ")
    val nombreActual = readlnOrNull() ?: return

    print("Nuevo nombre: ")
    val nuevoNombre = readlnOrNull() ?: return
    print("Nueva dirección: ")
    val nuevaDireccion = readlnOrNull() ?: return
    print("Nueva fecha de fundación (YYYY-MM-DD): ")
    val nuevaFecha = readlnOrNull()?.let { LocalDate.parse(it, DateTimeFormatter.ISO_DATE) } ?: return
    print("¿Está abierta? (true/false): ")
    val nuevaAbierta = readlnOrNull()?.toBoolean() ?: return

    val nuevaConcesionaria = Concesionaria(nuevoNombre, nuevaDireccion, 0, nuevaFecha, nuevaAbierta)
    CRUD.actualizarConcesionaria(nombreActual, nuevaConcesionaria)
    println("Concesionaria modificada exitosamente.")
}

fun eliminarConcesionariaInteractivo() {
    println("=== Eliminar Concesionaria ===")
    print("Nombre de la concesionaria a eliminar: ")
    val nombre = readlnOrNull() ?: return
    CRUD.eliminarConcesionaria(nombre)
    println("Concesionaria eliminada exitosamente.")
}

fun actualizarAutoInteractivo() {
    println("=== Modificar models.Auto ===")
    print("Nombre de la concesionaria: ")
    val concesionariaNombre = readlnOrNull() ?: return
    print("Modelo del auto a modificar: ")
    val modelo = readlnOrNull() ?: return

    print("Nueva marca: ")
    val nuevaMarca = readlnOrNull() ?: return
    print("Nuevo modelo: ")
    val nuevoModelo = readlnOrNull() ?: return
    print("Nuevo año: ")
    val nuevoAnio = readlnOrNull()?.toIntOrNull() ?: return
    print("Nuevo precio: ")
    val nuevoPrecio = readlnOrNull()?.toDoubleOrNull() ?: return
    print("¿Está disponible? (true/false): ")
    val nuevoDisponible = readlnOrNull()?.toBoolean() ?: return

    val nuevoAuto = Auto(nuevaMarca, nuevoModelo, nuevoAnio, nuevoPrecio, nuevoDisponible)
    CRUD.actualizarAuto(concesionariaNombre, modelo, nuevoAuto)
    println("Auto modificado exitosamente.")
}

fun eliminarAutoInteractivo() {
    println("=== Eliminar Auto ===")
    print("Nombre de la concesionaria: ")
    val concesionariaNombre = readlnOrNull() ?: return
    print("Modelo del auto a eliminar: ")
    val modelo = readlnOrNull() ?: return
    CRUD.eliminarAuto(concesionariaNombre, modelo)
    println("Auto eliminado exitosamente.")
}
