
package analizadorJSOM;

import java.util.ArrayList;

public class anSintactico implements IJson{
    String token_actual;
    ArrayList<String> mensaje_error = new ArrayList<String>();
    ArrayList<String> tokens = new ArrayList<String>();
    ArrayList<String> conjunto_primero;
    int tam;
    public anSintactico(){
        anLexico lexico = new anLexico();
        tokens = lexico.getTokens();
        tam = tokens.size();
        json(); 
        if(mensaje_error.isEmpty()){
            System.out.println("EL FUENTE ES SINTACTICAMENTE CORRECTO.");
        }else{
            System.out.println("EL FUENTE ES SINTACTICAMENTE INCORRECTO.");
        }
        System.out.println(mensaje_error);
        
    
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
    
    /*panic mode, consumir tokens de las entradas hasta encontrar una condicion de seguridad*/
    public void panic_mode(String[] conjunto_primero){
        /*recorrer y compara con la lista enviada en caso de encontrar
        un token igual se sale ciclo while retornando a la funcion que lo ha invocado
        */
        while( !tokens.isEmpty()){
            for(int x = 0 ; x < conjunto_primero.length ; x++){
                if(token_actual == conjunto_primero[x]){
                    return;
                }
            }
            tokens.remove(0);
            if(!tokens.isEmpty()){
                token_actual = tokens.get(0);
            }else{
                mensaje_error.add("PanicMode: No se sha podido encontrar una condicion de seguridad");
                System.exit(0);
            }
        }
    }
    
    
    public void json(){
        System.out.println("Se ha llamado a json");
        token_actual = tokens.get(0);
        System.out.println(tokens.size());
        if(token_actual == "L_CORCHETE" || token_actual == "L_LLAVE"){
            element();
        }else{
            panic_mode(primero_json);
        }
        System.out.println(tokens.size());
    }//fin json
    
    public void element(){
        System.out.println("Se ha llamado a element");
        switch(token_actual){
            case "L_CORCHETE":
                array();
                break;
            case "L_LLAVE":
                object();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba { o [");
                panic_mode(primero_element);
        }
    
    }//fin element
    
    public void array(){
        System.out.println("Se ha llamado a array");
        switch(token_actual){
            case "L_CORCHETE":
                match(token_actual);
                array_prima();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual + " se esperaba [");
                panic_mode(primero_array);
        }
    }//fin array
    
    public void array_prima(){
        System.out.println("Se ha llamado a array_prima");
        switch(token_actual){
            case "L_CORCHETE":
                element_list();
                match(token_actual);//machea con L_CORCHETE
                break;
            case "L_LLAVE":
                element_list();
                match(token_actual);
                break;
            case "R_CORCHETE":
                match(token_actual);
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba { o [ o ]");
                panic_mode(primero_array_prima);
        }
    
    }//fin array_prima
    
    public void element_list(){
        System.out.println("Se ha llamado a element_list");
        switch(token_actual){
            
            case "L_CORCHETE":
                element();
                element_list_prima();
                break;
            case "L_LLAVE":
                element();
                element_list_prima();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual + ", se esperaba [ o { (ELEMENT_LIST)");
                panic_mode(primero_element_list);
                
        }
    }//fin element_list
    
    public void element_list_prima(){
        System.out.println("Se ha llamado a element_list_prima");
        switch(token_actual){
            case "R_CORCHETE":
                break;
            case "COMA":
                match(token_actual);//machea con coma
                element();
                element_list_prima();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba , o ] (ELEMENT_LIST_PRIMA)");
                panic_mode(primero_element_list_prima);
        }
    }//fin element_list_prima
    
    public void object(){
        System.out.println("Se ha llamado a object");
        switch(token_actual){
            case "L_LLAVE":
                match(token_actual);//machea con L_LLAVE
                object_prima();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba {");
                panic_mode(primero_object);
        
        }
    
    }//fin_object
    
    public void object_prima(){
        System.out.println("Se ha llamado a object_prima");
        switch(token_actual){
            case "R_LLAVE":
                match(token_actual);
                break;
            case "LITERAL_CADENA":
                attribute_list();
                match(token_actual);//MACHEA CON R_LLAVE
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba ] o LITERAL_CADENA");
                panic_mode(primero_object_prima);
        }
    
    }//fin object_prima
    
    public void attribute_list(){
        System.out.println("Se ha llamado a attribute_list");
        switch(token_actual){
            case "LITERAL_CADENA":
                attribute();
                attribute_prima();
                break;
            
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba LITERAL_CADENA");    
                panic_mode(primero_attribute_list);
        
        }
    
    }//fin attribute_list
    
    public void attribute_prima(){
        System.out.println("Se ha llamado a attribute_prima");
        switch(token_actual){
            case "R_LLAVE":
                break;
            case "COMA":
                match(token_actual);//machea con COMA
                attribute();
                attribute_prima();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba , o ]");
                panic_mode(primero_attribute_list_prima);
        }
    
    }//fin_attribute_prima
    
    public void attribute(){
        System.out.println("Se ha llamado a attribute");
        switch(token_actual){
            case "LITERAL_CADENA":
                attribute_name();//nombre MACHEA CON LITERAL_CADENA
                match(token_actual);//dos puntos
                attribute_value();
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba LITERAL_CADENA (ATTRIBUTE)");
                panic_mode(primero_attribute);
                
        }
    
    }//fin attribute
    
    public void attribute_name(){
        System.out.println("Se ha llamado a attribute_name");
        switch(token_actual){
            case "LITERAL_CADENA":
                match(token_actual);
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual+ " se esperaba LITERAL_CADENA");
                panic_mode(primero_attribute_name);
                
        }
    
    }//fin attribute_name
    
    public void attribute_value(){ //ELEMENT O MACHEA CON LITERAL_CADENA, NUMBER, TRUE, FALSE, NULL
        System.out.println("Se ha llamado a attribute_value");
        switch(token_actual){
            case "L_CORCHETE":
                element();
                break;
            case "L_LLAVE":
                element();
                break;
            case "LITERAL_CADENA":
                match(token_actual);
                break;
            case "LITERAL_NUM":
                match(token_actual);
                break;
            case "TRUE":
                match(token_actual);
                break;
            case "FALSE":
                match(token_actual);
                break;
            case "NULL":
                match(token_actual);
                break;
            default:
                mensaje_error.add("Error, token encontrado "+ token_actual + "se esperaba { o { o LITERAL_CADENA o NULL o TRUE o FALSE o NUMERO");
                panic_mode(primero_attribute_value);
        }
    }//fin attribute_value
    
    
    public static void main(String[] args) {
        
        anSintactico anSintactico = new anSintactico();
        
    }
}
