import cats.effect.{IO, IOApp}
import cats.implicits._
import doobie._
import doobie.implicits._
import scala.io.Source
import scala.util.Try

// --- MODELO ---
case class EventoPolitico(
                           id: Int,
                           candidato: String,
                           partido: String,
                           evento: String,
                           fecha: String,
                           ubicacion: String,
                           asistentes: Int,
                           activa: Boolean
                         )

object ExamenFuncional extends IOApp.Simple {

  // --- CONFIGURACIÓN ---
  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
    "com.mysql.cj.jdbc.Driver",
    "jdbc:mysql://localhost:3306/elecciones_db?createDatabaseIfNotExist=true",
    "root",
    "1234",
    None
  )

  // --- DEFINICIÓN DE LA TABLA (DDL) ---
  val crearTabla: ConnectionIO[Int] = {
    sql"""
      CREATE TABLE IF NOT EXISTS eventos_politicos (
        id INT PRIMARY KEY,
        candidato VARCHAR(100),
        partido VARCHAR(100),
        evento VARCHAR(100),
        fecha VARCHAR(50),
        ubicacion VARCHAR(100),
        asistentes INT,
        activa BOOLEAN
      )
    """.update.run
  }

  // --- QUERY DE INSERCIÓN ---
  def insertarEvento(e: EventoPolitico): ConnectionIO[Int] = {
    sql"""
      INSERT INTO eventos_politicos (id, candidato, partido, evento, fecha, ubicacion, asistentes, activa)
      VALUES (${e.id}, ${e.candidato}, ${e.partido}, ${e.evento}, ${e.fecha}, ${e.ubicacion}, ${e.asistentes}, ${e.activa})
    """.update.run
  }

  // --- LECTURA CSV ---
  def leerCsvFuncional(ruta: String): IO[List[EventoPolitico]] = IO {
    val source = Source.fromFile(ruta)
    try {
      source.getLines().drop(1).toList.flatMap { linea =>
        val cols = linea.split(",").map(_.trim)
        if (cols.length >= 8) {
          Some(EventoPolitico(
            id = Try(cols(0).toInt).getOrElse(0),
            candidato = cols(1),
            partido = cols(2),
            evento = cols(3),
            fecha = cols(4),
            ubicacion = cols(5),
            asistentes = Try(cols(6).toInt).getOrElse(0),
            activa = Try(cols(7).toBoolean).getOrElse(false)
          ))
        } else None
      }
    } finally {
      source.close()
    }
  }

  // --- PROGRAMA PRINCIPAL ---
  override def run: IO[Unit] = {
    for {
      _     <- IO.println("Iniciando sistema...")

      // 1. EJECUTAR CREACIÓN DE TABLA
      _     <- crearTabla.transact(xa)
      _     <- IO.println("Tabla verificada/creada correctamente.")

      // 2. LECTURA DE DATOS
      datos <- leerCsvFuncional("src/main/resources/politica.csv")
      _     <- IO.println(s"Datos leídos del CSV: ${datos.length}")

      // 3. INSERTAR EN BASE DE DATOS
      count <- datos.traverse(insertarEvento).transact(xa)

      _     <- IO.println(s"Se guardaron ${count.sum} registros.")

    } yield ()
  }
}