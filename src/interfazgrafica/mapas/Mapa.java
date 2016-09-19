package interfazgrafica.mapas;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.core.Coordinate;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;

import java.io.IOException;

/**
 * Created by User on 15-Sep-16.
 */

public class Mapa extends PApplet{

    private double latitud;
    private double longitud;
    UnfoldingMap mapa;

    public void setCoordenadas(double pLatitud, double pLongitud){
        latitud = pLatitud;
        longitud = pLongitud;
    }

    public void setup(){
        try {
            size(400,400,OPENGL);

            mapa = new UnfoldingMap(this, new Microsoft.RoadProvider());
            Location ubicacionSismo = new Location(latitud, longitud);
            SimplePointMarker marcadorSismo = new SimplePointMarker(ubicacionSismo);
            mapa.addMarker(marcadorSismo);
            mapa.zoomAndPanTo(10, ubicacionSismo);
            mapa.setPanningRestriction(ubicacionSismo, 0);

            MapUtils.createDefaultEventDispatcher(this, mapa);
        } catch (Exception e) {
            throw e;
        }
    }

    public void draw(){
        try {
            background(0);
            mapa.draw();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void main(String args[]){
        PApplet.main(new String[] {"Mapa"});
    }
}
