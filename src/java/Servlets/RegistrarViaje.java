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
import com.mongodb.client.model.Sorts;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.simple.JSONObject;

/**
 *
 * @author Julian
 */
public class RegistrarViaje extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String destino=request.getParameter("destino");
        String user=request.getParameter("usuario");
        System.out.println(request.getParameter("asientos"));
        int asientos=Integer.parseInt(request.getParameter("asientos"));
        
        int carro=getDatosCarro(user);
        boolean ok=registrar(user, destino, asientos,carro);
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.print(ok);

        }
    }
    
    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("viajes");
    }

    public boolean registrar(String usuario, String destino, int asientos,int carro) {
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
            doc1.append("destino", destino);
            doc1.append("asientos", asientos);
            doc1.append("idCarro",carro);
            col.insertOne(doc1);
            ok=true;
        }catch(Exception e){
            ok=false;
        }
        return ok;
    }
    
    public int getDatosCarro(String id) {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("autos");
        
        int data=0;
        try {
            Document doc = col.find(eq("idUsuario",id)).first();
            JSONObject o=new JSONObject();
            data=doc.getInteger("_id");                        
        } catch (NullPointerException e) {
            data=0;
        }
        return data;

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
