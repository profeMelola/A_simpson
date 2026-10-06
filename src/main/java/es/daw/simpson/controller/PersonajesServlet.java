package es.daw.simpson.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import es.daw.simpson.model.Personaje;
import es.daw.simpson.servicio.PersonajeServicio;
import es.daw.simpson.util.Utils;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/personajes")
public class PersonajesServlet extends HttpServlet {

    // Con Spring aprenderemos a inyectar los servicicios en los controladores @Autowired!!! y no usaremos new!!!
    private final PersonajeServicio servicio = new PersonajeServicio();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // mis cositas...
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,  ServletException {

        // -----------------------------------------------------
        // 1. LEER PARÁMETROS DEL REQUEST
        String lugar = request.getParameter("lugar");
        System.out.println("*** lugar: " + lugar);

        String ordenarPor = request.getParameter("ordenarPor");
        System.out.println("*** ordenarPor: " + ordenarPor);

        //boolean descendente = request.getParameter("descendente") != null ? Boolean.parseBoolean(request.getParameter("descendente")) : false;
        boolean descendente = request.getParameter("descendente") != null;        // si no se marca, viajará un nula
        System.out.println("*** descendente: " + descendente);

        String edadMax = request.getParameter("edadMax");
        String limite = request.getParameter("limite");

        // MEJORA 1
        String ocupacion = request.getParameter("ocupacion");

        // MEJORA 3
        boolean soloFamilia = request.getParameter("soloFamilia") != null; // si no marca, no viaja, es un nulo


        List<Personaje> personajes = new ArrayList<>(); // no es null. Es una lista vacía con 0 elementos. Está inicializada
        // ------------------------------------------------------
        // 2. TRATAR LOS PARÁMETROS. CONVERSIONES Y VALIDACIONES
        try {
            Integer edadMaxInt = Utils.leerEntero("Edad máxima", edadMax);
            Integer limiteInt = Utils.leerEntero("Número de personajes máximos a mostrar", limite);

            // 3. LÓGICA. Necesito obtener los personajes de los Simpson
            // PENDIENTE!!! enviar los parámetros de filtrado y ordenación al servicio
            personajes = servicio.buscar(lugar,edadMaxInt,ordenarPor,descendente,limiteInt, ocupacion, soloFamilia);

        }catch (Exception e){
            // Escribir un mensaje de error en personajes.jsp
            request.setAttribute("error", e.getMessage());
        }

        // 4. PASAR A LA VISTA TODO LO QUE NECESITE (MODELO)
        request.setAttribute("personajes", personajes);
        request.setAttribute("lugares", servicio.lugaresDisponibles());
        request.setAttribute("ocupaciones", servicio.ocupacionesDisponibles());

        // 5. REENVIAR A LA VISTA (plantilla JSP)
        request.getRequestDispatcher("/personajes.jsp").forward(request,response);




    }

}