package logica;

import java.util.ArrayList;

/**
 * Clase que encapsula la información de contacto de una persona dada. Alberga el correo número
 * telefónico. Ademas posee un arreglo dinámico de las provincias sobre las cuales el contacto
 * desea recibir una notificación en caso de un sismo.
 */
public class PersonaInteresada
{
    private String nombre;
    private String correoElectronico;
    private String numero;
    private ArrayList<String> provinciasInteres;

    /**
     * Constructor clase PersonaInteresada
     * @param pNombre nombre de la persona
     * @param pCorreo correo de la persona
     * @param pNumero numero de la persona
     * @param pProvinciasInteres provincia/s en que esta interesada la persona
     */
    public PersonaInteresada(String pNombre, String pCorreo,String pNumero,ArrayList<String> pProvinciasInteres){
        this.nombre=pNombre;
        this.correoElectronico=pCorreo;
        this.numero=pNumero;
        provinciasInteres= new ArrayList<>();
        provinciasInteres=pProvinciasInteres;

    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getNumero() {
        return numero;
    }

    public ArrayList<String> getProvinciasInteres() {
        return provinciasInteres;
    }

    boolean Notificar(String epicentro){
        for(int i=0;i<provinciasInteres.size();i++){
            if(epicentro==provinciasInteres.get(i))
                return true;
        }
        return false;
    }
}

