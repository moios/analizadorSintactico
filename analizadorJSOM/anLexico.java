
package analizadorJSOM;

import java.util.ArrayList;



public class anLexico implements IJson{
    public ArrayList<String> tokens;
    public ArrayList<String> mensaje_error;
    public anLexico(){
        
        Fuente fuente = new Fuente("C://file/fuente.txt");
        char c;
        
        
        int numLinea = 0; //para lanzar el numero de linea del ERROR posible
        int numCaracter = 0;//para lanzar el numero de caracter de ERROR posible
        int estado;
        boolean aceptacion;
        String cadena = "";
        
        
        tokens = new ArrayList<String>();
        mensaje_error = new ArrayList<String>();
        
        /*
        for(int x = 0 ; x < fuente.cant_token ; x++){
            System.out.print(fuente.obtenerToken());
        }
        System.out.println("----------------------------------");
        */
        while((c=fuente.obtenerToken()) != EOF){
            //System.out.print(c);
            if(c == ' ' || c == TABULADOR){
                numCaracter++;
                //no hacer nada
            }else if(c == SALTO_LINEA){
                numLinea++;
            }else if(c == '{'){
                tokens.add("L_LLAVE");
            }else if(c == '}'){
                tokens.add("R_LLAVE");
            }else if(c == '['){
                tokens.add("L_CORCHETE");
            }else if(c == ']'){
                tokens.add("R_CORCHETE");
            }else if(c == ','){
                tokens.add("COMA");
            }else if(c == ':'){
                tokens.add("DOS_PUNTOS");
                
              
            }else if(c == '\"'){
                //CONSUMIR EL STRING SIN CONSIDERAR SU CONSTRUCCION
                do{
                    c = fuente.obtenerToken();
                    numCaracter++;
                    
                    if(c == EOF){
                        mensaje_error.add("Error> Se esperaba \"\nlinea: "+ numLinea+" columna: "+numCaracter);
                        break;
                    }
                    
                }while(c != '\"');
                //se consumio hasta la ultima comilla
                tokens.add("LITERAL_CADENA");
                
            
            }else if(esCaracter(c)){
                cadena = "";
                do{
                    cadena += c;
                    c = fuente.obtenerToken();  
                }while(esCaracter(c));
                fuente.devolverToken();
                if(null == cadena.toUpperCase()){
                    mensaje_error.add(cadena + " -->Devuelto NULL. es una construccion no valida de caracteres. Linea: " + numLinea);
                }else //booleanos
                switch (cadena.toUpperCase()) {
                    case "TRUE":
                        tokens.add("TRUE");
                        break;
                    case "FALSE":
                        tokens.add("FALSE");
                        break;
                    case "NULL":
                        tokens.add("NULL");
                        break;
                    default:
                        mensaje_error.add(cadena + " -->es una construccion no valida de caracteres. Linea: " + numLinea);
                        break;
                }
                
            }//digitos en notacion cientifica sin signo inicial
            else if(esDigito(c)){
                cadena = "";
                estado = 0;
                //si el estado de aceptacion es falso saldra del ciclo
                aceptacion = true;
                while(aceptacion){
                    switch(estado){
                        case 0: //una secuencia de digitos
                            c = fuente.obtenerToken();
                            if(esDigito(c)){
                                cadena += c;
                                estado = 0;
                                
                            }else if('.' == c){
                                cadena += c;
                                estado = 1;
                            }else if(('e' == c) || ('E' == c)){
                                cadena += c;
                                estado = 3;
                            }else{
                                estado = -1;//error
                            }
                            break;
                        case 1://un punto, debe seguir un digito  o simbolo + -
                            c = fuente.obtenerToken();
                            if(esDigito(c)){
                                cadena+= c;
                                estado = 2;
                            }else if(('+' == c) || ('-' == c)){
                                cadena+=c;
                                estado = 2;
                            }else{
                                estado = -1; //error
                            }
                            break;
                        case 2:
                            c = fuente.obtenerToken();
                            if(esDigito(c)){
                                cadena+=c;
                                estado = 2;
                            }else if(('e' == c) || ('E' == c)){
                                cadena += c;
                                estado = 3;
                            }else{
                                estado = -1; //error
                            }
                            break;
                        case 3:
                            c = fuente.obtenerToken();
                            if(('+' == c) || ('-' == c)){
                                cadena += c;
                                estado = 4;
                            }else if(esDigito(c)){
                                cadena += c;
                                estado = 5;
                            }else{
                                estado = -1; //error
                            }
                            break;
                        
                        case 4:
                            c = fuente.obtenerToken();
                            if(esDigito(c)){
                                cadena += c;
                                estado = 5;
                            }else {
                                estado = -1; //error
                            }
                            break;
                        case 5:
                            c = fuente.obtenerToken();
                            if(esDigito(c)){
                                cadena += c;
                                estado = 5;
                            }else{
                                aceptacion = false;//terminara el ciclo
                                fuente.devolverToken();
                            }
                            break;
                        default:
                                aceptacion = false;
                                fuente.devolverToken();
                    }//fin switch
                    
                }//fin while control de digitos    
                    
                tokens.add("LITERAL_NUM");
            }//fin si ..es digito..
            else{
                mensaje_error.add("Error. no se reconoce el caracter: "+ c+ "Linea: "+numLinea);
            } 

        }//fin while leer fuente
        
        Archivo archivo = new Archivo(tokens);
        
        
    /*for(int k = 0 ; k < tokens.size() ; k++){
        System.out.print(' '+tokens.get(k));
    }
    
    for(int k = 0 ; k < mensaje_error.size() ; k++){
        System.out.print(mensaje_error.get(k));
    }*/
        System.out.println("SE HA TERMINADO DE ANALIZAR EL CODIGO");
        System.out.println("SE HA GENERADO EL ARCHIVO OUTPUT.TXT EN C://FILE");
    }
    
    /*public static void main(String[] args) {
        anLexico testLexer = new anLexico();
    }*/

    private boolean esCaracter(char c) {
        return Character.isLetter(c);
    }
    
    private boolean esDigito(char c){
        return Character.isDigit(c);
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }
    
    
}
