package logica;

public class Sismo
{
    private String  magnitud;
    private String fecha;
    private String tiempo;
    private String profundidadKm;
    private String origenFalla;
    private Localizacion localizacion;
    private String indice;

    public Sismo(String pFecha, String pTiempo, String pProfundidadKm, String pOrigenFalla, String pMagnitud, Localizacion pLocalizacion, String pIndice){
        setFecha(pFecha);
        setTiempo(pTiempo);
        setProfundidadKm(pProfundidadKm);
        setOrigenFalla(pOrigenFalla);
        setMagnitud(pMagnitud);
        setLocalizacion(pLocalizacion);
        setIndice(pIndice);
    }

    public String getFecha()
    {
        return fecha;
    }

    private void setFecha(String fecha)
    {
        this.fecha = fecha;
    }

    public String getTiempo() {
        return tiempo;
    }

    private void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getProfundidadKm() {
        return profundidadKm;
    }

    private void setProfundidadKm(String profundidadKm) {
        this.profundidadKm = profundidadKm;
    }

    public String getOrigenFalla() {
        return origenFalla;
    }

    private void setOrigenFalla(String origenFalla) {
        this.origenFalla = origenFalla;
    }

    public String getMagnitud() {
        return magnitud;
    }

    private void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }

    public Localizacion getLocalizacion(){
        return localizacion;
    }

    private void setLocalizacion(Localizacion pLocalizacion){
        localizacion = pLocalizacion;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    @Override
    public String toString() {
        return "Sismo{" +
                "magnitud='" + magnitud + '\'' +
                ", fecha='" + fecha + '\'' +
                ", tiempo='" + tiempo + '\'' +
                ", profundidadKm='" + profundidadKm + '\'' +
                ", origenFalla='" + origenFalla + '\'' +
                localizacion.toString() +
                '}';
    }
}
