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
import com.mongodb.client.model.Sorts;
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
public class RegistrarCarro extends HttpServlet {

    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String placa = request.getParameter("placa");
        String modelo = request.getParameter("modelo");
        String marca = request.getParameter("marca");
        int asientos = Integer.parseInt(request.getParameter("asientos"));
        System.out.println("*****************");
        System.out.println(usuario+"-"+placa+"-"+modelo+"-"+marca+"-"+asientos);

        boolean ok=registrar(usuario, marca, modelo, placa, asientos);
        System.out.println(ok);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.print(ok);
        }
    }

    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("autos");
    }

    public boolean registrar(String usuario, String marca, String modelo, String placa, int asientos) {
        getConnection();
        boolean ok=false;
        int id;
        try {
            Document doc = col.find().sort(Sorts.orderBy(Sorts.descending("_id"))).first();
            id = doc.getInteger("_id");
        } catch (Exception e) {
            id = 0;
        }
        try {
            Document doc1 = new Document();
            doc1.append("_id", id + 1);
            doc1.append("idUsuario", usuario);
            doc1.append("marca", marca);
            doc1.append("modelo", modelo);
            doc1.append("placa", placa);
            doc1.append("asientos", asientos);

            col.insertOne(doc1);
            ok=true;
        }catch(Exception e){
            ok=false;
        }
        return ok;
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
