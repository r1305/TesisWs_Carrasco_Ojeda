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

public class LoginFb extends HttpServlet {

    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        String fbId=request.getParameter("fb");
        String correo=request.getParameter("correo");
        int comun=Integer.parseInt(request.getParameter("comun"));
        String foto=request.getParameter("foto");
        String ok=update(correo, comun, foto, fbId);

        
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.print(ok);            

        }
    }

    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("usuarios");

    }
    public String update(String id,int comun,String foto,String fb){
        String rpta="";
        try{
            getConnection();
            updateComun(id, comun);
            updateFbId(id, fb);
            updateFoto(id, foto);
            rpta="ok";
        }catch(Exception e){
            rpta="fail";
        }
        return rpta;
    }

    public String updateComun(String id,int comun) {
        
        String rpta="";
        try {
            Document doc = col.find(eq("correo", id)).first();        
            col.updateOne(doc, new Document("$set", new Document("comun", comun)));
            rpta="ok";
        } catch (Exception e) {
            System.out.println(e);
            rpta="fail";           
        }
        return rpta;
    }
    
    public String updateFoto(String id,String foto){
        
        String rpta="";
        try {
            Document doc = col.find(eq("correo", id)).first();
            col.updateOne(doc, new Document("$set", new Document("foto", foto)));
            rpta="ok";
        } catch (Exception e) {
            System.out.println(e);
            rpta="fail";            
        }
        
        return rpta;
    }
    
    public String updateFbId(String id,String fb){
        
        String rpta="";
        try {
            Document doc = col.find(eq("correo", id)).first();
            col.updateOne(doc, new Document("$set", new Document("userFbId", fb)));
            rpta="ok";
        } catch (Exception e) {
            System.out.println(e);
            rpta="fail";            
        }
        return rpta;
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
