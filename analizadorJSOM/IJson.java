
package analizadorJSOM;

public interface IJson {
    static char SALTO_LINEA = '\n';
    static char EOF = '~';
    static char TABULADOR = '\t';
    
    String[] primero_json = {"L_CORCHETE", "L_LLAVE"};
    String[] primero_element = {"L_CORCHETE", "L_LLAVE"};
    String[] primero_array = {"L_CORCHETE"};
    String[] primero_array_prima = {"L_CORCHETE","R_CORCHETE", "L_LLAVE"};
    String[] primero_element_list = {"L_CORCHETE", "L_LLAVE"};
    String[] primero_element_list_prima = {"R_CORCHETE", "COMA"};
    String[] primero_object = {"L_CORCHETE"};
    String[] primero_object_prima = {"R_CORCHETE", "LITERAL_CADENA"};
    String[] primero_attribute_list = {"LITERAL_CADENA"};
    String[] primero_attribute_list_prima = {"R_LLAVE", "COMA"};
    String[] primero_attribute = {"LITERAL_CADENA"};
    String[] primero_attribute_name = {"LITERAL_CADENA"};
    String[] primero_attribute_value = {"L_CORCHETE", "L_LLAVE", "LITERAL_CADENA", "LITERAL_NUM","TRUE", "FALSE", "NULL"};
}
