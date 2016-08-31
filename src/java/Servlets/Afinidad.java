/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import prueba.alumno.Alumno;
import prueba.modelo.Modelo;

public class Afinidad extends HttpServlet {

    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo1 = request.getParameter("correo1");
        String correo2 = request.getParameter("correo2");
        int comun=Integer.parseInt(request.getParameter("comun"));
        
        double rpta=afinidad(correo1, correo2,comun);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println(rpta);
        }
    }

    public double afinidad(String correo, String fbId,int comun) {
        double afinidad = 0;
        Alumno a = getDatos(correo);
        Alumno b = getDatos2(fbId);

        Modelo m = new Modelo();
        try {
            if (a != null && b != null) {
                afinidad = m.calcularAfinidadTotal(a, b,comun);
            } else {
                afinidad = 0;
            }
        } catch (Exception e) {
            afinidad = 0;
        }

        return afinidad;
    }

    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("usuarios");

    }
    public Alumno getDatos2(String fbId){
        getConnection();

        Alumno a;

        try {
            a = new Alumno();
            Document doc = col.find(eq("userFbId", fbId)).first();
            a.setCarrera(doc.getString("carrera"));
            a.setCiclo(doc.getInteger("ciclo"));
            a.setSexo(doc.getString("sexo"));
            a.setEdad(doc.getInteger("edad"));
            a.setUserFbId(doc.getString("userFbId"));
        } catch (NullPointerException e) {
            a = null;
        }

        return a;
    }

    public Alumno getDatos(String correo) {
        getConnection();

        Alumno a;

        try {
            a = new Alumno();
            Document doc = col.find(eq("correo", correo)).first();
            a.setCarrera(doc.getString("carrera"));
            a.setCiclo(doc.getInteger("ciclo"));
            a.setSexo(doc.getString("sexo"));
            a.setEdad(doc.getInteger("edad"));
            a.setUserFbId(doc.getString("userFbId"));
        } catch (NullPointerException e) {
            a = null;
        }

        return a;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
