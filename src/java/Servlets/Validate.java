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

/**
 *
 * @author Julian
 */
public class Validate extends HttpServlet {

    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String correo = request.getParameter("correo");
        String rpta = validar(correo);

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.print
        (rpta);

        }
    }

    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("usuarios");
    }

    public String validar(String correo) {
        getConnection();
        String co = "";
        String[] dominio = correo.split("@");
        
        System.out.println(dominio[0]);
        System.out.println(dominio[1]);
        if (dominio[1].equalsIgnoreCase("aloe.ulima.edu.pe")) {
            
            try {
                Document doc = col.find(eq("correo", correo)).first();
                if (!doc.isEmpty()) {
                    //mandara a la pantalla inicial
                    co = doc.getString("correo");
                }
            } catch (NullPointerException e) {
                //mandara al registro
                co = "error";
            }
            
        } else {
            //pedira otro correo
            co = "fail";
        }

        return co;

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
