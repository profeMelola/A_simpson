package es.daw.simpson.servicio;

import es.daw.simpson.clasico.ComparadorPorEdad;
import es.daw.simpson.model.Personaje;
import es.daw.simpson.model.PersonajeTradicional;
import es.daw.simpson.repository.PersonajeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aquí vive TODA la lógica de los streams
 * El servlet no filtra ni ordena
 */
public class PersonajeServicio {

    // En Spring aprenderemos a usar inyección de dependencia y no usar new...
    private final PersonajeRepository repositorio = new PersonajeRepository();

    /**
     *
     * @param lugar
     * @param edadMax
     * @param ordernaPor
     * @param descendente
     * @param limite
     * @return
     */
    public List<Personaje> buscar(String lugar,
                                  Integer edadMax,
                                  String ordernaPor, // pendiente
                                  boolean descendente, // pendiente
                                  Integer limite,
                                  String ocupacion,
                                  boolean soloFamilia
                                  ) {

        // -----------------------------------------------------------
        // --------------------- REPASO DE PRIMERO ------------------
        // Trasteando con ordenaciones de un ArrayList
//        List<Personaje> personajes = repositorio.findAll();
//
//        List<PersonajeTradicional> personajesTrad = new ArrayList<>();
//        personajesTrad.add(new PersonajeTradicional("Homer",    "Simpson",    39, "Inspector de seguridad", "Central Nuclear",   true));
//        personajesTrad.add(new PersonajeTradicional("Marge",    "Simpson",    36, "Ama de casa",            "Casa Simpson",      true));
//        personajesTrad.add(new PersonajeTradicional("Bart",     "Simpson",    10, "Estudiante",             "Escuela Primaria",  true));
//
//        System.out.println("*** personajesTrad SIN ORDENAR: " + personajesTrad);
//
//        Collections.sort(personajesTrad);
//        //personajesTrad.sort(Comparator.naturalOrder());
//
//        System.out.println("*** personajesTrad CON ORDENACIÓN NATURAL: " + personajesTrad);
//
//        personajesTrad.sort(new ComparadorPorEdad());
//
//        System.out.println("*** personajesTrad CON COMPARATOR POR EDAD: " + personajesTrad);



//        PersonajeTradicional personajeTradicional = new PersonajeTradicional();
//        System.out.println(personajeTradicional); // si no tiene toString sale la traza chunga....

        // -------------------------------------
//        List<Personaje> personajes2 = repositorio.findAll();
//        personajes2.sort(Comparator.comparing(Personaje::nombre));
//        System.out.println("************** personajes2:"+personajes2);

        // -----------------------------------------------------------

        return repositorio.findAll().stream()

                // filter() deja pasar solo los que cumplen la condición
                .filter( p -> lugar == null || lugar.isBlank() || p.lugar().equalsIgnoreCase(lugar))
                .filter(p -> edadMax == null || p.edad() <= edadMax)

                .filter( p -> ocupacion == null || ocupacion.isBlank() || p.ocupacion().equalsIgnoreCase(ocupacion))

                .filter( p -> !soloFamilia || p.principal())

                //.sorted( (p1, p2) -> p1.nombre().compareTo(p2.nombre()))
                //.sorted(Comparator.comparing(Personaje::nombre))

                .sorted(crearComparador(ordernaPor, descendente))
                .limit(limite == null? Integer.MAX_VALUE : limite)
                .toList();
    }

    /**
     *
     * @return
     */
    public List<String> lugaresDisponibles(){
        // FORMA 1: funcional
        return repositorio.findAll().stream()
                //.map(p -> p.lugar())
                .map(Personaje::lugar)
                .distinct()
                .sorted()
                .toList();
                //.collect(Collectors.toList());
        // FORMA 2: IMPERATIVO

        // FORMA 3: convierto a Set (conjunto no repetido..)

    }


    /**
     *
     * @return
     */
    public List<String> ocupacionesDisponibles(){
        return repositorio.findAll().stream()
                .map(Personaje::ocupacion)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     *
     * @param ordenaPor
     * @param descendente
     * @return
     */
    private Comparator<Personaje> crearComparador(String ordenaPor, boolean descendente){
        Comparator<Personaje> comparador = switch( ordenaPor == null ? "" : ordenaPor){

            // Por edad y, si dos tienen la misma edad, por nombre;
            //case "edad" -> Comparator.comparingInt(p -> p.edad()); // con lambda
            case "edad" -> Comparator.comparingInt(Personaje::edad).thenComparing(Personaje::nombre); // con referencia a método

            // por apellido y, si no coinciden, por nombre
            case "apellido" -> Comparator.comparing(Personaje::apellido).thenComparing(Personaje::nombre);

            // MEJORA 2
            case "lugar" -> Comparator.comparing(Personaje::lugar).thenComparing(Personaje::nombre);

            default -> Comparator.comparing(Personaje::nombre); // por descarte, ha seleccionado nombre

        };

        return descendente ? comparador.reversed() : comparador;
    }



}
