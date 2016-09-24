package logica;

import java.util.ArrayList;

public class PersonaInteresada
{
    private String nombre;
    private String correoElectronico;
    private String numero;
    private ArrayList<String> provinciasInteres;

    public PersonaInteresada(String pNombre, String pCorreo,String pNumero,ArrayList<String> pProvinciasInteres){
        this.nombre=pNombre;
        this.correoElectronico=pCorreo;
        this.numero=pNumero;
        provinciasInteres= new ArrayList<>();
        provinciasInteres=pProvinciasInteres;

    }
//HOLA MUNDO
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

