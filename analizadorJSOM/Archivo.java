
package analizadorJSOM;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Archivo {
   
    FileWriter fichero = null;
    PrintWriter pw = null;
    String codigo = "";
    public Archivo(ArrayList tokens){
        try {
            fichero = new FileWriter("C://file/output.txt");
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
}
