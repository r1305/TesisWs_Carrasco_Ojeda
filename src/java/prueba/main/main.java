/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.main;

import prueba.aes.AES;
import prueba.alumno.Alumno;

/**
 *
 * @author usuario
 */
public class main {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
       String key="Bar12345Bar12345";
       String initVector = "RandomInitVector";
       String textoCifrado;
       String textoDescifrado;
       String textoPlano;
        
       Alumno alumno;
       alumno= new Alumno("Fernando","Ojeda","Ingenieria de Sistemas","masculino",21,9);
       
       //cifrar,
       textoPlano=alumno.getCarrera();
       textoCifrado=AES.cifrar(key, initVector, textoPlano);
       
       //descifrar
       textoDescifrado=AES.descifrar(key, initVector, textoCifrado);
       
       //mostrar resultados
       System.out.println("Texto plano: " + textoPlano+ "\n" +
       "Texto cifrado: " + textoCifrado + "\n" +
       "Texto descifrado: " + textoDescifrado);
    }
    
}
