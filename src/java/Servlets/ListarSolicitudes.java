/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Julian
 */
public class ListarSolicitudes extends HttpServlet {

    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String user=request.getParameter("user");
        String lista=listar(user);
        response.setContentType("application/json");
        
        try (PrintWriter out = response.getWriter()) {

            out.print(lista);            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("solicitudes");
    }
    
    public String listar(String usuario) {
        getConnection();
        String id;
        List<JSONObject> l=new ArrayList<>();
        MongoCursor<Document> cursor=col.find(eq("idChofer",usuario)).iterator();
        Document doc;
        JSONObject ob=new JSONObject();
        
        JSONArray ja=new JSONArray();
        try {
            while (cursor.hasNext()) {
                doc = cursor.next();
                JSONObject o=new JSONObject();                
                o.put("_id",doc.getInteger("_id"));
                o.put("idUsuario",doc.getString("idUsuario"));
                o.put("idChofer",doc.getString("idChofer"));
                o.put("idViaje",doc.getInteger("idViaje"));
                o.put("estado",doc.getString("estado"));
                //String nombre = doc.getString("destino");
                ja.add(o);
            }
            ob.put("solicitudes", ja);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            cursor.close();
        }
        return ob.toString();

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
