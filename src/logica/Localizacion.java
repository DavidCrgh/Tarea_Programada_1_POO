package logica;

/**
 * Created by User on 12-Sep-16.
 */
public class Localizacion
{
    private double latitud;
    private double longitud;
    private String descripcion;
    private boolean esTerrestre;
    private String provincia;

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
