package es.daw.simpson.servicio;

import es.daw.simpson.model.Personaje;
import es.daw.simpson.repository.PersonajeRepository;

import java.util.List;

/**
 * Aquí vive TODA la lógica de los streams
 * El servlet no filtra ni ordena
 */
public class PersonajeServicio {

    // En Spring aprenderemos a usar inyección de dependencia y no usar new...
    private final PersonajeRepository repositorio = new PersonajeRepository();

    public List<Personaje> buscar() {
        return repositorio.findAll();
    }


}
