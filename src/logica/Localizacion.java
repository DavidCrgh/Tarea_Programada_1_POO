package logica;

/**
 * Clase para la representacion de una localizacion por su latitud y longitud entre otros atributos.
 * Tipicamente pertenece a una instancia de la clase Sismo.
 *
 * @see logica.Sismo Sismo
 */
public class Localizacion
{
    private double latitud;
    private double longitud;
    private String descripcion;
    private boolean esTerrestre;
    private String provincia;

    /**
     * Constructor de la clase Locatilazion
     * @param latitud latitud donde ocurrio
     * @param longitud longitud donde ocurrio
     * @param descripcion descripcion
     * @param esTerrestre si el sismo es terreste o maritimo
     * @param pProvincia provincia donde ocurrio
     */
    public Localizacion(double latitud, double longitud, String descripcion, String esTerrestre, String pProvincia) {
        setLatitud(latitud);
        setLongitud(longitud);
        setDescripcion(descripcion);
        setEsTerrestre(esTerrestre);
        setProvincia(pProvincia);
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEsTerrestre() {
        return esTerrestre;
    }

    public void setEsTerrestre(String pEsTerrestre) {
        this.esTerrestre = pEsTerrestre.equals("Terrestre");
    }

    public String getProvincia(){
        return provincia;
    }

    public void setProvincia(String pProvincia){
        if(isEsTerrestre()){
            provincia = pProvincia;
        } else{
            provincia = "Maritimo";
        }
    }

    @Override
    public String toString() {
        return "Localizacion{" +
                "latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", esTerrestre=" + esTerrestre +
                ", provincia='" + provincia + '\'' +
                '}';
    }
}
