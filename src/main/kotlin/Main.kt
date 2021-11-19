import java.lang.NumberFormatException

class Modulo(maxAlumnos: Int) {
    var nMax = maxAlumnos
        set(value) {
            if (value > 0) field = value
            else throw NumberFormatException("El número de alumnos no puede ser 0 o menor que 0")
        }

    var notas: Array<FloatArray> = Array(4){FloatArray(nMax){-1.0f} }
    var alumnos: Array<Alumno?> = arrayOfNulls<Alumno?>(nMax)

    companion object {
        const val EV_PRIMERA = 0
        const val EV_SEGUNDA = 1
        const val EV_TERCERA = 2
        const val EV_FINAL = 3
    }

    /**
     * Metodos
     */

    fun matriculaAlumno(addedAlumno: Alumno): Boolean {
        return try {
            val posAlumno: Int = alumnos.indexOfFirst { it == null }

            alumnos[posAlumno] = addedAlumno
            for (i in EV_PRIMERA..EV_FINAL) {
                notas[i][posAlumno] = 0.0f
            }
            true
        } catch (_: ArrayIndexOutOfBoundsException) {
            println("No se pueden añadir más alumnos")
            false
        }
    }

    fun bajaAlumno(idAlumno: String): Boolean {
        return try {
            val posAlumno: Int = alumnos.indexOfFirst { it?.id == idAlumno }

            alumnos[alumnos.indexOfFirst { it?.id == idAlumno }] = null
            for (i in EV_PRIMERA..EV_FINAL) {
                notas[i][posAlumno] = -1.0f
            }
            true
        } catch (_: NoSuchElementException) {
            println("No existe ese alumno")
            false
        }
    }

    fun establecerNota(idAlumno: String, evaluacion: String, nota: Float): Boolean {
        return try {
            val posAlumno: Int = alumnos.indexOfFirst { it?.id == idAlumno }
            //val posAlumno1: List<Alumno?> = alumnos.filter { it?.id == idAlumno }

            when(evaluacion.uppercase()) {
                "primera".uppercase() -> notas[EV_PRIMERA][posAlumno] = nota
                //"1" -> notas[EV_PRIMERA][posAlumno] = nota
                "segunda".uppercase() -> notas[EV_SEGUNDA][posAlumno] = nota
                //"2" -> notas[EV_SEGUNDA][posAlumno] = nota
                "tercera".uppercase() -> notas[EV_TERCERA][posAlumno] = nota
                //"3" -> notas[EV_TERCERA][posAlumno] = nota
                "final".uppercase() -> notas[EV_FINAL][posAlumno] = nota
                //"4" -> notas[EV_FINAL][posAlumno] = nota
                else -> println("Por favor, introduzca una evaluación válida")
            }
            true
        } catch (_: NoSuchElementException) {
            println("No existe tal alumno")
            false
        }
    }

    fun calcularEvaluacionFinal() {
        for (i in 0 until this.nMax - 1) {
            notas[EV_FINAL][i] = (notas[EV_PRIMERA][i] + notas[EV_SEGUNDA][i] + notas[EV_TERCERA][i]) / 3.0f
        }
    }

    fun listaNotas(evaluacion: String = "final"): List<Pair<Alumno, Float>> {
        val listaNotas = ArrayList<Pair<Alumno, Float>>()
        var ev = 0

        when (evaluacion) {
            "primera".uppercase() -> ev = 0
            //"1".uppercase() -> ev = 0
            "segunda".uppercase() -> ev = 1
            //"2".uppercase() -> ev = 1
            "tercera".uppercase() -> ev = 2
            //"3".uppercase() -> ev = 2
        }
        if (ev in 0..2) {
            alumnos.forEachIndexed { i, alumno ->
                if (alumno != null) {listaNotas.add(Pair(alumno,notas[ev.toInt()][i]))}
            }
        }
        return listaNotas
    }

    fun numeroAprobados(evaluacion: String = "final"): Int {
        var ev = 0
        var aprobados = 0

        when(evaluacion) {
            "primera".uppercase() -> ev = 0
            //"1".uppercase() -> ev = 0
            "segunda".uppercase() -> ev = 1
            //"2".uppercase() -> ev = 1
            "tercera".uppercase() -> ev = 2
            //"3".uppercase() -> ev = 2
        }

        for (i in 0 until this.nMax - 1) {
            if (notas[ev][i] >= 5.0f) {
                aprobados++
            }
        }
        return aprobados
    }

    fun notaMasBaja(evaluacion: String = "final"): Float {
        var ev = 0
        var masBaja = 11.0f

        when(evaluacion.uppercase()) {
            "primera".uppercase() -> ev = 0
            //"1".uppercase() -> ev = 0
            "segunda".uppercase() -> ev = 1
            //"2".uppercase() -> ev = 1
            "tercera".uppercase() -> ev = 2
            //"3".uppercase() -> ev = 2
        }

        masBaja = notas[ev].filter { it >= 0 }.minOf { it }

        return masBaja
    }

    fun notaMasAlta(evaluacion: String = "final"): Float {
        var ev = 0
        var masAlta = -1.0f

        when(evaluacion.uppercase()) {
            "primera".uppercase() -> ev = 0
            //"1".uppercase() -> ev = 0
            "segunda".uppercase() -> ev = 1
            //"2".uppercase() -> ev = 1
            "tercera".uppercase() -> ev = 2
            //"3".uppercase() -> ev = 2
        }

        masAlta = notas[ev].maxOf { it }
        return masAlta
    }

    fun notaMedia(evaluacion: String = "final"): Float {
        var ev = 0
        var media = -1.0f

        when(evaluacion.uppercase()) {
            "primera".uppercase() -> ev = 0
            //"1".uppercase() -> ev = 0
            "segunda".uppercase() -> ev = 1
            //"2".uppercase() -> ev = 1
            "tercera".uppercase() -> ev = 2
            //"3".uppercase() -> ev = 2
        }

        media =  notas[ev].filter { it >= 0 }.average().toFloat()
        return media
    }

    fun listaNotasOrdenados(evaluacion: String = "final"): List<Pair<Alumno, Float>> {
        return this.listaNotas().sortedBy { it.second }
    }
}

data class Alumno(var id: String, var nom: String, var ap1: String, var ap2: String){
    init {
        require(id.isNotEmpty())
        require(nom.isNotEmpty())
        require(ap1.isNotEmpty())
        require(ap2.isNotEmpty())
    }
}

fun main() {
    val alumno1 = Alumno("1JMCL", "Juan Manuel", "Cumbrera", "López")
    val alumno2 = Alumno("RAB", "Regulus Arcturus", "Black", "Unknown")
    val modulo1 = Modulo(20).apply {
        matriculaAlumno(alumno1)
        matriculaAlumno(alumno2)
    }
    println(modulo1)

    modulo1.establecerNota("1JMCL", "primera", 9.0f)
    modulo1.establecerNota("1JMCL", "segunda", 8.0f)
    modulo1.establecerNota("1JMCL", "tercera", 10.0f)
    modulo1.establecerNota("RAB", "primera", 6.0f)
    modulo1.establecerNota("RAB", "segunda", 9.0f)
    modulo1.establecerNota("RAB", "tercera", 5.0f)

    modulo1.calcularEvaluacionFinal()

    println(modulo1.listaNotas("primera"))
    println(modulo1.listaNotas("final"))
    println(modulo1.numeroAprobados("primera"))
    println(modulo1.numeroAprobados("segunda"))
    println(modulo1.numeroAprobados("tercera"))
    println(modulo1.notaMasBaja("tercera"))
    println(modulo1.notaMasAlta("tercera"))
    println(modulo1.notaMedia("tercera"))
}
