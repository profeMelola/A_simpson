package es.daw.simpson.controller;

import java.io.*;
import java.util.List;

import es.daw.simpson.model.Personaje;
import es.daw.simpson.servicio.PersonajeServicio;
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

        // 1. LEER PARÁMETROS DEL REQUEST
        String lugar = request.getParameter("lugar");
        String ordernarPor = request.getParameter("ordernarPor");
        //boolean descendente = request.getParameter("descendente") != null ? Boolean.parseBoolean(request.getParameter("descendente")) : false;
        boolean descendente = request.getParameter("descendente") != null; // si no se marca, viajará un nula

        // PENDIENTE!!!! Deberíamos convertirlos a un entero
        String edadMax = request.getParameter("edadMax");
        int edadMaxInt = Integer.parseInt(edadMax);
        Integer edadMaxInteger = Integer.valueOf(edadMax);



        String limite = request.getParameter("limite");

        // 2. TRATAR LOS PARÁMETROS. CONVERSIONES Y VALIDACIONES


        // 3. LÓGICA. Necesito obtener los personajes de los Simpson
        // PENDIENTE!!! enviar los parámetros de filtrado y ordenación al servicio
        List<Personaje> personajes = servicio.buscar();

        // 4. PASAR A LA VISTA TODO LO QUE NECESITE
        request.setAttribute("personajes", personajes);

        // 5. REENVIAR A LA VISTA (plantilla JSP)
        request.getRequestDispatcher("/personajes.jsp").forward(request,response);




    }

}