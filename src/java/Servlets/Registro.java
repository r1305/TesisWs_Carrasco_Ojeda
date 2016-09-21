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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import prueba.aes.AES;
import prueba.alumno.Alumno;

public class Registro extends HttpServlet {

    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {

        AES aes=new AES();
        
        String nombre = aes.encrypt(request.getParameter("nombre"));
        String correo = request.getParameter("correo");
        int ciclo = Integer.parseInt(request.getParameter("ciclo"));
        String sexo = aes.encrypt(request.getParameter("sexo"));
        int edad =Integer.parseInt(request.getParameter("edad"));
        String carrera = aes.encrypt(request.getParameter("carrera"));
        String clave=aes.encrypt(request.getParameter("clave"));
        

        Alumno a = new Alumno();
        a.setNombres(nombre);
        a.setCorreo(correo);
        a.setCiclo(ciclo);
        a.setSexo(sexo);
        a.setEdad(edad);
        a.setCarrera(carrera);
        a.setClave(clave);
        
        registrar(a);

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.print("ok");

        }
    }

    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("usuarios");

    }

    public void registrar(Alumno a) {
        getConnection();
        int id;
        try {
            Document doc = col.find().sort(Sorts.orderBy(Sorts.descending("_id"))).first();
            id = doc.getInteger("_id");
        } catch (Exception e) {
            id = 0;
        }

        Document doc1 = new Document();
        doc1.append("_id", id + 1);
        doc1.append("nombre", a.getNombres());
        doc1.append("edad", a.getEdad());
        doc1.append("sexo", a.getSexo());
        doc1.append("carrera", a.getCarrera());
        doc1.append("ciclo", a.getCiclo());
        doc1.append("clave", a.getClave());
        doc1.append("correo", a.getCorreo());
        doc1.append("userFbId", "");
        doc1.append("comun", 0);

        col.insertOne(doc1);
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
        }
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
