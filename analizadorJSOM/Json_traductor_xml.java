
package analizadorJSOM;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Json_traductor_xml implements IJson{
    String token_actual;
    ArrayList<String> mensaje_error = new ArrayList<String>();
    ArrayList<String> tokens = new ArrayList<String>();
    ArrayList<String> tokens_trad = new ArrayList<String>();
    int tam;
    public Json_traductor_xml(){
        anLexico lexico = new anLexico();
        tokens = lexico.getTokens();
        tam = tokens.size();
        json_trad(); 
        if(mensaje_error.isEmpty()){
            System.out.println("EL FUENTE ES SINTACTICAMENTE CORRECTO.");
        }else{
            System.out.println("EL FUENTE ES SINTACTICAMENTE INCORRECTO.");
        }
        System.out.println(mensaje_error);
        System.out.println(tokens_trad);
        archivo(tokens_trad);
    
    }
    /*funcion que consume cada tokens*/
    public void match(String token_esperado){
        /*no era necesario recibir un token para machear...
        pero hice asi para ir comprobando que viene en caso de errores*/
        if(token_esperado == token_actual){
            tokens.remove(0);
            if(!tokens.isEmpty()){
                token_actual = tokens.get(0);
            }
        }else{
            mensaje_error.add("Error, token esperado "+ token_actual);
        }
    }
    
    
    public void json_trad(){
        System.out.println("Se ha llamado a json");
        token_actual = tokens.get(0);
        if(token_actual == "L_CORCHETE" || token_actual == "L_LLAVE"){
            element_trad();
        }
    }//fin json
    
    public void element_trad(){
        
        switch(token_actual){
            case "L_CORCHETE":
                array_trad();
                break;
            case "L_LLAVE":
                object_trad();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba { o [");
        
        }
    
    }//fin element
    
    public void array_trad(){
        
        switch(token_actual){
            case "L_CORCHETE":
                match(token_actual);
                tokens_trad.add("<item>");
                array_prima_trad();
                tokens_trad.add("</item>");
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual + " se esperaba [");
        
        }
    }//fin array
    
    public void array_prima_trad(){
        
        switch(token_actual){
            case "L_CORCHETE":
                element_list_trad();
                tokens_trad.add("<item>");
                match(token_actual);//matchea con L_CORCHETE
                break;
            case "L_LLAVE":
                element_list_trad();
                match(token_actual);
                break;
            case "R_CORCHETE":
                tokens_trad.add("</item>");
                match(token_actual);
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba { o [ o ]");
        
        }
    
    }//fin array_prima
    
    public void element_list_trad(){
        
        switch(token_actual){
            
            case "L_CORCHETE":
                tokens_trad.add("<item>");
                element_trad();
                element_list_prima_trad();
                break;
            case "L_LLAVE":
                element_trad();
                element_list_prima_trad();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual + ", se esperaba [ o { (ELEMENT_LIST)");
        
                
        }
    }//fin element_list
    
    public void element_list_prima_trad(){
        
        switch(token_actual){
            case "R_CORCHETE":
                break;
            case "COMA":
                match(token_actual);//machea con coma
                element_trad();
                element_list_prima_trad();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba , o ] (ELEMENT_LIST_PRIMA)");
        
        }
    }//fin element_list_prima
    
    public void object_trad(){
        
        switch(token_actual){
            case "L_LLAVE":
                
                match(token_actual);//machea con L_LLAVE
                object_prima_trad();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba {");
        
        
        }
    
    }//fin_object
    
    public void object_prima_trad(){
        
        switch(token_actual){
            case "R_LLAVE":
                match(token_actual);
                break;
            case "LITERAL_CADENA":
                attribute_list_trad();
                match(token_actual);//MACHEA CON R_LLAVE
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba ] o LITERAL_CADENA");
        
        }
    
    }//fin object_prima
    
    public void attribute_list_trad(){
        
        switch(token_actual){
            case "LITERAL_CADENA":
                attribute_trad();
                attribute_prima_trad();
                break;
            
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba LITERAL_CADENA");    
        
        
        }
    
    }//fin attribute_list
    
    public void attribute_prima_trad(){
        
        switch(token_actual){
            case "R_LLAVE":
                break;
            case "COMA":
                match(token_actual);//machea con COMA
                attribute_trad();
                attribute_prima_trad();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba , o ]");
        
        }
    
    }//fin_attribute_prima
    
    public void attribute_trad(){
        String token_aux = token_actual;
        switch(token_actual){
            
            case "LITERAL_CADENA":
                tokens_trad.add("<"+token_aux+">");
                attribute_name_trad();//nombre MACHEA CON LITERAL_CADENA
                match(token_actual);//dos puntos
                attribute_value_trad();
                tokens_trad.add("</"+token_aux+">");
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba LITERAL_CADENA (ATTRIBUTE)");
        
                
        }
    
    }//fin attribute
    
    public void attribute_name_trad(){
        
        switch(token_actual){
            case "LITERAL_CADENA":
                match(token_actual);
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba LITERAL_CADENA");
        
                
        }
    
    }//fin attribute_name
    
    public void attribute_value_trad(){ //ELEMENT O MACHEA CON LITERAL_CADENA, NUMBER, TRUE, FALSE, NULL
        String token_aux = token_actual;
        switch(token_actual){
            case "L_CORCHETE":
                element_trad();
                break;
            case "L_LLAVE":
                element_trad();
                break;
            
            default:
            tokens_trad.add(token_aux);
            match(token_actual);
        
        }
    }//fin attribute_value
    
   
    FileWriter fichero = null;
    PrintWriter pw = null;
    String codigo = "";
    public void archivo(ArrayList tokens){
        try {
            fichero = new FileWriter("C://file/output.xml");
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);
        
        for (int i = 0; i < tokens.size(); i++){
                codigo += " " + tokens.get(i);
        }
        pw.append(codigo);
        pw.close();
        
    }
    
    
    public static void main(String[] args) {
        
        Json_traductor_xml json_xml = new Json_traductor_xml();
        
    }
}
