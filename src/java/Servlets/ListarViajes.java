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
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Julian
 */
public class ListarViajes extends HttpServlet {

    private MongoClient client;
    private final MongoClientURI clientURI = new MongoClientURI("mongodb://root:root@ds147995.mlab.com:47995/tesis_ojeda_carrasco");
    private MongoDatabase database;
    private MongoCollection<Document> col;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String lista=listar();
        response.setContentType("application/json");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            //out.print(lista);
            //JSONParser p=new JSONParser();
            //JSONObject o=(JSONObject)p.parse(lista);
            //JSONArray rawName=(JSONArray)o.get("viajes");
            /*for(int i=0;i<lista.size();i++){
                out.println(lista.get(i));
            }*/
            out.print(lista);            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void getConnection() {
        client = new MongoClient(clientURI);
        database = client.getDatabase("tesis_ojeda_carrasco");
        col = database.getCollection("viajes");
    }
    
    public String listar() {
        getConnection();
        String id;
        List<JSONObject> l=new ArrayList<>();
        MongoCursor<Document> cursor=col.find().iterator();
        Document doc;
        JSONObject ob=new JSONObject();
        
        JSONArray ja=new JSONArray();
        try {
            while (cursor.hasNext()) {
                doc = cursor.next();
                JSONObject o=new JSONObject();
                o.put("nombre",doc.getString("destino"));
                o.put("asientos",doc.getInteger("asientos"));
                o.put("destino",doc.getString("destino"));
                o.put("placa",getPlaca(doc.getInteger("idCarro")));
                //String nombre = doc.getString("destino");
                ja.add(o);
            }
            ob.put("viajes", ja);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            cursor.close();
        }
        return ob.toString();

    }
    
    public String getPlaca(int idCarro){
        String placa="";
        col = database.getCollection("autos");
        
        try {
            Document doc = col.find(eq("_id",idCarro)).first();            
            placa=doc.getString("placa");
            
        } catch (NullPointerException e) {
            placa="error";
        }
        
        return placa;
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
