package interfazgrafica.mapas;

import processing.core.PApplet;

import javax.swing.*;
import java.awt.*;

/**
 * Created by User on 15-Sep-16.
 */
public class FrameMapa extends JFrame {
    Mapa dibujo;
    public FrameMapa(double latitud, double longitud) throws Exception{
        this.setSize(400,400);
        this.setResizable(false);
        Dimension dimensionPantalla = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dimensionPantalla.width/2-this.getSize().width/2, dimensionPantalla.height/2-this.getSize().height/2);
        JPanel panel = new JPanel();
        panel.setBounds(20, 20, 300, 300);
        try {
            dibujo = null;
            dibujo = new Mapa();
        } catch (Exception e) {
            throw e;
        }
        dibujo.setCoordenadas(latitud, longitud);
        panel.add(dibujo);
        this.add(panel);
        dibujo.init();
        this.setVisible(true);
    }
}
