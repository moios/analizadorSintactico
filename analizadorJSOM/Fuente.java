
package analizadorJSOM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Fuente implements IJson{
    
    ArrayList fuente;
    private File archivo;
    private FileReader fr;
    private BufferedReader br;
    private String linea_leida;
    char unCaracter;
    private int size;
    public int cant_token;
    
    public Fuente(String ruta_archivo){
        this.fuente = new ArrayList();
        size = 0;
        cant_token = 0;
        cargar_token(ruta_archivo);
    }

    private void cargar_token(String ruta_archivo) {
        archivo = new File(ruta_archivo);
        
        try {
            fr = new FileReader(archivo);
        } catch (FileNotFoundException ex) {
            System.exit(0);
        }
 
       //en caso de encontrar el archivo
       //se lee la primera linea del archivo
       br = new BufferedReader(fr);
       
       
        try {
            while((linea_leida=br.readLine())!= null){
                
                for(int x = 0 ; x < linea_leida.length() ; x++){
                    fuente.add(linea_leida.charAt(x));
                    cant_token++;
                }
                fuente.add(SALTO_LINEA);//salto de linea
                cant_token++;
                
                
            }
            fuente.add(EOF);//final de archivo EOF
            cant_token++;
        } catch (IOException ex) {
            System.out.println("Error:"+ ex);
        }
       
       
    }
    
    public char obtenerToken(){
           return (char) this.fuente.get(size++);
           
       }
    
    public char devolverToken(){
           return (char) this.fuente.get(--size);
       }
    
    public boolean finalArchivo(){
        return size >= cant_token;
    }
    
}
