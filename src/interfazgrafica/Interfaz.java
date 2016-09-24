package interfazgrafica;

import interfazgrafica.mapas.FrameMapa;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import logica.PersonaInteresada;
import logica.Sismo;
import utilitarias.Utilitaria;

import java.io.File;
import java.util.ArrayList;

public class Interfaz extends Application {

    private final Utilitaria objetoUtilitario = new Utilitaria();

    private ArrayList<Sismo> listaObjetosSismo;
    private ArrayList<PersonaInteresada> listaPersonas;

    private VBox contenedorVerticalSismos;
    private VBox contenedorVerticalBotones;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception
    {
        objetoUtilitario.crearCSV();
        listaObjetosSismo = objetoUtilitario.crearListaSismos();

        escenarioPrincipal.setTitle("Registro de Sismos");
        escenarioPrincipal.setResizable(false);

        Text encabezadoAplicacion = new Text("Sismos Recientes");
        encabezadoAplicacion.setId("encabezado-aplicacion");

        int altoBarra = 50;
        int anchoBarra = 115;
        String IdBarraBotones = "botones-barra";

        Button botonAgregarSismo = new Button("Agregar sismo");
        botonAgregarSismo.setPrefSize(anchoBarra,altoBarra);
        botonAgregarSismo.setId(IdBarraBotones);
        botonAgregarSismo.setOnAction(esEdicion->clickBotonAgregarSismo(false, null));

        Button botonCargarContactos = new Button("Cargar Contactos");
        botonCargarContactos.setPrefSize(anchoBarra,altoBarra);
        botonCargarContactos.setId(IdBarraBotones);
        botonCargarContactos.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        FileChooser fileChooser = new FileChooser();
                        configurarFileChooser(fileChooser);

                        File contactos = fileChooser.showOpenDialog(escenarioPrincipal);
                        if (contactos != null) {
                            try{
                                listaPersonas=objetoUtilitario.crearListaPersonas(contactos);
                            } catch(Exception error){
                                mensajeError(5);
                            }
                        }
                    }
                });

        Button botonMenuReportes = new Button("Menu de Reportes");
        botonMenuReportes.setPrefSize(anchoBarra,altoBarra);
        botonMenuReportes.setId(IdBarraBotones);
        botonMenuReportes.setOnAction(evento->clickBotonMenuReportes());

        Rectangle rectanguloRelleno = new Rectangle(610-(anchoBarra * 3), altoBarra);
        rectanguloRelleno.setId("rectangulo-relleno");

        HBox contenedorHorizontalSuperior = new HBox();
        contenedorHorizontalSuperior.getChildren().addAll(botonAgregarSismo,botonCargarContactos,
                botonMenuReportes, rectanguloRelleno);

        VBox contenedorVerticalSuperior = new VBox();
        contenedorVerticalSuperior.getChildren().addAll(encabezadoAplicacion, contenedorHorizontalSuperior);

        String idEtiquetas= "etiquetas";
        Label magnitud= new Label("Magnitud");
        magnitud.setId(idEtiquetas);
        Label fecha = new Label("Fecha");
        fecha.setId(idEtiquetas);
        Label hora = new Label("Hora");
        hora.setId(idEtiquetas);
        Label localizacion = new Label("Localizacion");
        localizacion.setId(idEtiquetas);
        Label espacio = new Label("");

        contenedorVerticalSismos = new VBox();
        contenedorVerticalSismos.setSpacing(20);
        contenedorVerticalBotones = new VBox();
        contenedorVerticalBotones.setSpacing(12);

        HBox contenedorHorizontalSismos = new HBox();
        contenedorHorizontalSismos.setSpacing(30);
        contenedorHorizontalSismos.getChildren().addAll(contenedorVerticalSismos, contenedorVerticalBotones);

        ScrollPane listaSismos = new ScrollPane();
        listaSismos.setPrefSize(525, 275);
        listaSismos.setContent(contenedorHorizontalSismos);

        construirInterfazSismos(contenedorVerticalSismos,contenedorVerticalBotones,listaObjetosSismo);

        GridPane cuadrillaCentral= new GridPane();
        cuadrillaCentral.setHgap(52);
        cuadrillaCentral.setVgap(0);
        cuadrillaCentral.add(fecha,0,0);
        cuadrillaCentral.add(espacio,1,0);
        cuadrillaCentral.add(hora,2,0);
        cuadrillaCentral.add(magnitud,3,0);
        cuadrillaCentral.add(localizacion,4,0);
        cuadrillaCentral.add(listaSismos,0,1,5,1);
        cuadrillaCentral.setPrefWidth(600);
        cuadrillaCentral.setAlignment(Pos.CENTER);

        VBox contenedorPrincipal= new VBox();
        contenedorPrincipal.getChildren().addAll(contenedorVerticalSuperior,cuadrillaCentral);

        Scene escenaPrincipal = new Scene(contenedorPrincipal,575,400);
        escenarioPrincipal.setScene(escenaPrincipal);
        escenaPrincipal.getStylesheets().add
                (Interfaz.class.getResource("Estilos.css").toExternalForm());
        escenarioPrincipal.show();
    }

    private static void configurarFileChooser(FileChooser fileChooser) {
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
    }

    private void clickBotonAgregarSismo(boolean esEdicion, Sismo pSismo)
    {
        Stage escenarioFormulario = new Stage();

        Text encabezadoFormulario = new Text();
        encabezadoFormulario.setId("encabezado-sub");

        Label enunciado = new Label("Ingrese los detalles del sismo y presione 'Guardar'.");

        String etiquetasSub= "etiquetas-sub";

        Label profundidad = new Label("Profundidad:");
        profundidad.setId(etiquetasSub);
        Label origen = new Label("Origen:");
        origen.setId(etiquetasSub);
        Label localizacionFormulario = new Label("Localizacion");
        localizacionFormulario.setId("etiqueta-localizacion");
        Label latitud = new Label("Latitud:");
        latitud.setId(etiquetasSub);
        Label longitud = new Label("Longitud:");
        longitud.setId(etiquetasSub);
        Label pronvincia = new Label("Provincia:");
        pronvincia.setId(etiquetasSub);
        Label descripcionLocalizacion = new Label("Descripcion:");
        descripcionLocalizacion.setId(etiquetasSub);
        Label terrestre_marititmo = new Label("Tipo:");
        terrestre_marititmo.setId(etiquetasSub);
        Label magnitudFormulario = new Label("Magnitud:");
        magnitudFormulario.setId(etiquetasSub);
        Label tiempoFecha= new Label("Fecha:");
        tiempoFecha.setId(etiquetasSub);
        Label tiempoHora= new Label("Hora:");
        tiempoHora.setId(etiquetasSub);

        TextField entradaProfundidad = new TextField();
        entradaProfundidad.setPrefWidth(60);
        Label etiquetaKm = new Label("Km");
        ChoiceBox entradaOrigen = new ChoiceBox(FXCollections.observableArrayList("Subduccion",
                "Choque de placas","Tectonico por falla local","Intra placa","Deformacion interna"));
                ChoiceBox entradaDia= new ChoiceBox(FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"));
                ChoiceBox entradaMes= new ChoiceBox(FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12"));
                ChoiceBox entradaAnno= new ChoiceBox(FXCollections.observableArrayList("2016","2015","2014","2013","2012","2011","2010","2009","2008",
                "2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996","1995","1994","1993","1992","1991","1990",
                "1989","1988","1987","1986","1985","1984","1983","1982","1981","1980","1979","1978","1977","1976","1975","1974","1973","1972",
                "1971","1970","1969","1968","1967","1966","1965","1964","1963","1962","1961","1960","1959","1958","1957","1956","1955","1954",
                "1953","1952","1951","1950","1949","1948","1947","1946","1945","1944","1943","1942","1941","1940","1939","1938","1937","1936"
                ,"1935","1934","1933","1932","1931","1930","1929","1928","1927","1926","1925","1924","1923","1922","1921","1920","1919","1918"
                ,"1917","1916","1915","1914","1913","1912","1911","1910","1909","1908","1907","1906","1905","1904","1903","1902","1901","1900"));
        ChoiceBox entradaHora = new ChoiceBox(FXCollections.observableArrayList("00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16",
                "17","18","19","20","21","22","23"));
        ChoiceBox entradaMinutos = new ChoiceBox(FXCollections.observableArrayList("00","01","02","03","04","05","06","07","08","09","10","11","12","13",
                "14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38"
                ,"39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"));
        ChoiceBox entradaSegundos = new ChoiceBox(FXCollections.observableArrayList("00","01","02","03","04","05","06","07","08","09","10","11","12","13",
                "14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36","37","38"
                ,"39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"));

        TextField entradaLatitud = new TextField();
        TextField entradaLongitud = new TextField();
        TextField entradaMagnitud= new TextField();
        TextArea entradaDescripcion = new TextArea();
        entradaDescripcion.setPrefHeight(100);
        entradaDescripcion.setWrapText(true);

        ChoiceBox entradaProvincia = new ChoiceBox(
                FXCollections.observableArrayList("Guanacaste","Alajuela","Heredia","Cartago","San José","Limón","Puntarenas"));

        ToggleGroup grupoBotonesRadio = new ToggleGroup();

        RadioButton terrestre= new RadioButton();
        RadioButton maritimo= new RadioButton();
        terrestre.setToggleGroup(grupoBotonesRadio);
        maritimo.setToggleGroup(grupoBotonesRadio);
        terrestre.setUserData("Terrestre");
        maritimo.setUserData("Maritimo");
        terrestre.setText("Terrestre");
        maritimo.setText("Maritimo");

        grupoBotonesRadio.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(grupoBotonesRadio.getSelectedToggle().getUserData().toString() == "Maritimo"){
                    entradaProvincia.setDisable(true);
                } else {
                    entradaProvincia.setDisable(false);
                }
            }
        });

        Button botonGuardar = new Button("Guardar");
        botonGuardar.setId("boton-guardar");

        HBox contenedorBotonGuardar = new HBox();
        contenedorBotonGuardar.setAlignment(Pos.BOTTOM_RIGHT);
        contenedorBotonGuardar.getChildren().add(botonGuardar);

        HBox contenedorFecha= new HBox();
        contenedorFecha.setAlignment((Pos.BOTTOM_LEFT));
        contenedorFecha.setSpacing(10);
        contenedorFecha.getChildren().addAll(entradaDia,entradaMes,entradaAnno);

        HBox contenedorHora= new HBox();
        contenedorHora.setAlignment(Pos.BOTTOM_LEFT);
        contenedorHora.setSpacing(10);
        contenedorHora.getChildren().addAll(entradaHora,entradaMinutos,entradaSegundos);

        HBox contenedorProfundidad = new HBox();

        contenedorProfundidad.setSpacing(10);
        contenedorProfundidad.setAlignment(Pos.BOTTOM_CENTER);
        contenedorProfundidad.getChildren().addAll(entradaProfundidad, etiquetaKm);

        GridPane cuadrillaFormulario = new GridPane();
        cuadrillaFormulario.setAlignment(Pos.CENTER);
        cuadrillaFormulario.setVgap(10);
        cuadrillaFormulario.setHgap(10);
        cuadrillaFormulario.setPadding(new Insets(10,10,10,10));

        cuadrillaFormulario.add(origen,0,0);
        cuadrillaFormulario.add(entradaOrigen, 1, 0);
        cuadrillaFormulario.add(profundidad,2,0);
        cuadrillaFormulario.add(contenedorProfundidad, 3,0);
        cuadrillaFormulario.add(localizacionFormulario,0,1);

        cuadrillaFormulario.add(tiempoFecha,0,2);
        cuadrillaFormulario.add(tiempoHora,2,2);
        cuadrillaFormulario.add(contenedorFecha,1,2);
        cuadrillaFormulario.add(contenedorHora,3,2);

        cuadrillaFormulario.add(latitud,0,3);
        cuadrillaFormulario.add(entradaLatitud, 1, 3);
        cuadrillaFormulario.add(longitud,2,3);
        cuadrillaFormulario.add(entradaLongitud, 3, 3);
        cuadrillaFormulario.add(pronvincia,0,4);
        cuadrillaFormulario.add(entradaProvincia,1 ,4);
        cuadrillaFormulario.add(descripcionLocalizacion,0,5);
        cuadrillaFormulario.add(entradaDescripcion, 0, 6,4,1);
        cuadrillaFormulario.add(terrestre_marititmo,0,7);
        cuadrillaFormulario.add(terrestre,0,8);
        cuadrillaFormulario.add(maritimo,0,9);
        cuadrillaFormulario.add(magnitudFormulario,2,7);
        cuadrillaFormulario.add(entradaMagnitud,3,7);
        cuadrillaFormulario.add(contenedorBotonGuardar, 3, 9);
        cuadrillaFormulario.setGridLinesVisible(false);

        if(esEdicion){
            encabezadoFormulario.setText("Editar Sismo");
            entradaDescripcion.setText(pSismo.getLocalizacion().getDescripcion());
            entradaProfundidad.setText(pSismo.getProfundidadKm());
            entradaMagnitud.setText(pSismo.getMagnitud());
            entradaLatitud.setText(""+pSismo.getLocalizacion().getLatitud());
            entradaLongitud.setText(""+pSismo.getLocalizacion().getLongitud());
            entradaOrigen.getSelectionModel().select(pSismo.getOrigenFalla());
            entradaDia.getSelectionModel().select(pSismo.getFecha().substring(0,2));
            entradaMes.getSelectionModel().select(pSismo.getFecha().substring(3,5));
            entradaAnno.getSelectionModel().select(pSismo.getFecha().substring(6,10));
            entradaHora.getSelectionModel().select(pSismo.getTiempo().substring(0,2));
            entradaMinutos.getSelectionModel().select(pSismo.getTiempo().substring(3,5));
            entradaSegundos.getSelectionModel().select(pSismo.getTiempo().substring(6,8));
            entradaProvincia.getSelectionModel().select(pSismo.getLocalizacion().getProvincia());

            if(pSismo.getLocalizacion().isEsTerrestre()){
                terrestre.setSelected(true);
            } else {
                maritimo.setSelected(true);
            }

            botonGuardar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Object dia= entradaDia.getSelectionModel().getSelectedItem();
                    Object mes= entradaMes.getSelectionModel().getSelectedItem();
                    Object anno = entradaAnno.getSelectionModel().getSelectedItem();
                    String fecha= objetoUtilitario.verificarInfo(dia,mes,anno,"/",true);

                    Object hora= entradaHora.getSelectionModel().getSelectedItem();
                    Object minutos = entradaMinutos.getSelectionModel().getSelectedItem();
                    Object segundos= entradaSegundos.getSelectionModel().getSelectedItem();
                    String tiempo = objetoUtilitario.verificarInfo(hora,minutos,segundos,":",false);

                    String profundidad = entradaProfundidad.getText();

                    Object origenSismo= entradaOrigen.getSelectionModel().getSelectedItem();
                    String origen = objetoUtilitario.verificarOrigenProvincia(origenSismo);

                    String provincia;
                    Object provinciaSismo=entradaProvincia.getSelectionModel().getSelectedItem();
                    provincia=objetoUtilitario.verificarOrigenProvincia(provinciaSismo);

                    String magnitud = entradaMagnitud.getText();
                    String latitud = entradaLatitud.getText();
                    String longitud = entradaLongitud.getText();
                    String descripcion = entradaDescripcion.getText();

                    String esTerrestre = "";
                    if(terrestre.isSelected()){
                        esTerrestre += "Terrestre";
                    } else {
                        esTerrestre += "Maritimo";
                    }

                    String[] infoFormulario = {fecha, tiempo, profundidad, origen, magnitud, latitud, longitud, descripcion, esTerrestre, provincia, pSismo.getIndice()};
                    if (objetoUtilitario.validarFormulario(infoFormulario)) {
                        try{
                            objetoUtilitario.editarCSV(infoFormulario);
                        } catch (Exception error){
                            mensajeError(4);//System.out.println("Error en CSV. Edición.");
                        }

                        try{
                            listaObjetosSismo = objetoUtilitario.crearListaSismos();

                            construirInterfazSismos(contenedorVerticalSismos,contenedorVerticalBotones,listaObjetosSismo);
                        }
                        catch(Exception error) {

                            mensajeError(4);
                        }

                        escenarioFormulario.close();
                    } else {
                        mensajeError(0);
                    }
                }
            });

        } else{
            encabezadoFormulario.setText("Registro de Sismo Nuevo");
            terrestre.setSelected(true);
            botonGuardar.setOnAction(
                    new EventHandler<ActionEvent>()  {
                        @Override
                        public void handle(final ActionEvent e)  {
                            ArrayList<String> infoFormulario= new ArrayList<String>();
                            Object dia= entradaDia.getSelectionModel().getSelectedItem();
                            Object mes= entradaMes.getSelectionModel().getSelectedItem();
                            Object anno = entradaAnno.getSelectionModel().getSelectedItem();
                            String fecha= objetoUtilitario.verificarInfo(dia,mes,anno,"/",true);

                            Object hora= entradaHora.getSelectionModel().getSelectedItem();
                            Object minutos = entradaMinutos.getSelectionModel().getSelectedItem();
                            Object segundos= entradaSegundos.getSelectionModel().getSelectedItem();
                            String tiempo = objetoUtilitario.verificarInfo(hora,minutos,segundos,":",false);

                            String profundidad = entradaProfundidad.getText();

                            Object origenSismo= entradaOrigen.getSelectionModel().getSelectedItem();
                            String origen = objetoUtilitario.verificarOrigenProvincia(origenSismo);

                            String provincia;
                            Object provinciaSismo=entradaProvincia.getSelectionModel().getSelectedItem();
                            provincia=objetoUtilitario.verificarOrigenProvincia(provinciaSismo);

                            String magnitud = entradaMagnitud.getText();
                            String latitud = entradaLatitud.getText();
                            String longitud = entradaLongitud.getText();
                            String descripcion = entradaDescripcion.getText();

                            String esTerrestre = "";
                            if(terrestre.isSelected()){
                                esTerrestre += "Terrestre";
                            } else {
                                esTerrestre += "Maritimo";
                            }

                            infoFormulario.add(fecha); infoFormulario.add(tiempo); infoFormulario.add(profundidad); infoFormulario.add(origen); infoFormulario.add(magnitud);
                            infoFormulario.add(latitud); infoFormulario.add(longitud); infoFormulario.add(descripcion); infoFormulario.add(esTerrestre); infoFormulario.add(provincia);
                            System.out.println(objetoUtilitario.validarFormulario(infoFormulario));
                            if (objetoUtilitario.validarFormulario(infoFormulario)) {
                                try{
                                    objetoUtilitario.escribirCSV(infoFormulario);
                                } catch(Exception error){
                                    mensajeError(4);
                                }

                                try{
                                    listaObjetosSismo= objetoUtilitario.crearListaSismos();

                                    construirInterfazSismos(contenedorVerticalSismos,contenedorVerticalBotones,listaObjetosSismo);}
                                catch(Exception error){

                                    mensajeError(4);
                                }

                                try{
                                    envioCorreo(provincia);
                                }
                                catch(Exception error){
                                    mensajeError(3);
                                }

                                escenarioFormulario.close();
                            } else {
                                mensajeError(0);
                            }
                        }
                    });
        }

        VBox contenedorVerticalFormulario = new VBox();
        contenedorVerticalFormulario.getChildren().addAll(encabezadoFormulario,enunciado,cuadrillaFormulario);
        contenedorVerticalFormulario.setAlignment(Pos.CENTER);

        Scene escenaFormulario=new Scene(contenedorVerticalFormulario);
        escenaFormulario.getStylesheets().add
                (Interfaz.class.getResource("Estilos.css").toExternalForm());
        escenarioFormulario.setScene(escenaFormulario);
        escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
        escenarioFormulario.setResizable(false);
        escenarioFormulario.showAndWait();
    }

    private void clickBotonMenuReportes(){

        ChoiceBox entradaDiaInicio = new ChoiceBox(FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"));
        ChoiceBox entradaMesInicio = new ChoiceBox(FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12"));
        ChoiceBox entradaAnnoInicio= new ChoiceBox(FXCollections.observableArrayList("2016","2015","2014","2013","2012","2011","2010","2009","2008",
                "2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996","1995","1994","1993","1992","1991","1990",
                "1989","1988","1987","1986","1985","1984","1983","1982","1981","1980","1979","1978","1977","1976","1975","1974","1973","1972",
                "1971","1970","1969","1968","1967","1966","1965","1964","1963","1962","1961","1960","1959","1958","1957","1956","1955","1954",
                "1953","1952","1951","1950","1949","1948","1947","1946","1945","1944","1943","1942","1941","1940","1939","1938","1937","1936"
                ,"1935","1934","1933","1932","1931","1930","1929","1928","1927","1926","1925","1924","1923","1922","1921","1920","1919","1918"
                ,"1917","1916","1915","1914","1913","1912","1911","1910","1909","1908","1907","1906","1905","1904","1903","1902","1901","1900"));

        ChoiceBox entradaDiaFin = new ChoiceBox(FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"));
        ChoiceBox entradaMesFin = new ChoiceBox(FXCollections.observableArrayList("01","02","03","04","05","06","07","08","09","10","11","12"));
        ChoiceBox entradaAnnoFin = new ChoiceBox(FXCollections.observableArrayList("2016","2015","2014","2013","2012","2011","2010","2009","2008",
                "2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996","1995","1994","1993","1992","1991","1990",
                "1989","1988","1987","1986","1985","1984","1983","1982","1981","1980","1979","1978","1977","1976","1975","1974","1973","1972",
                "1971","1970","1969","1968","1967","1966","1965","1964","1963","1962","1961","1960","1959","1958","1957","1956","1955","1954",
                "1953","1952","1951","1950","1949","1948","1947","1946","1945","1944","1943","1942","1941","1940","1939","1938","1937","1936"
                ,"1935","1934","1933","1932","1931","1930","1929","1928","1927","1926","1925","1924","1923","1922","1921","1920","1919","1918"
                ,"1917","1916","1915","1914","1913","1912","1911","1910","1909","1908","1907","1906","1905","1904","1903","1902","1901","1900"));

        ChoiceBox entradaAnnoMes = new ChoiceBox(FXCollections.observableArrayList("2016","2015","2014","2013","2012","2011","2010","2009","2008",
                "2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996","1995","1994","1993","1992","1991","1990",
                "1989","1988","1987","1986","1985","1984","1983","1982","1981","1980","1979","1978","1977","1976","1975","1974","1973","1972",
                "1971","1970","1969","1968","1967","1966","1965","1964","1963","1962","1961","1960","1959","1958","1957","1956","1955","1954",
                "1953","1952","1951","1950","1949","1948","1947","1946","1945","1944","1943","1942","1941","1940","1939","1938","1937","1936"
                ,"1935","1934","1933","1932","1931","1930","1929","1928","1927","1926","1925","1924","1923","1922","1921","1920","1919","1918"
                ,"1917","1916","1915","1914","1913","1912","1911","1910","1909","1908","1907","1906","1905","1904","1903","1902","1901","1900"));

        Stage escenarioGraficos = new Stage();
        escenarioGraficos.setTitle("Gráficos");
        GridPane cuadrillaGraficos = new GridPane();

        ToggleGroup grupoBotonesGraficos = new ToggleGroup();

        cuadrillaGraficos.setVgap(10);
        cuadrillaGraficos.setHgap(10);
        cuadrillaGraficos.setPadding(new Insets(20,20,20,20));

        String detallesBotonGraficos= "boton-graficos";
        Button botonGraficos= new Button();
        botonGraficos.setId(detallesBotonGraficos);
        botonGraficos.setText("Generar Reporte");

        HBox contenedorBotonReporte = new HBox();
        contenedorBotonReporte.getChildren().add(botonGraficos);
        contenedorBotonReporte.setAlignment(Pos.CENTER);

        VBox contenedorSismosAnno= new VBox();

        VBox contenedorSismosFecha= new VBox();
        HBox casetillasFecha = new HBox();
        casetillasFecha.getChildren().addAll(entradaDiaInicio,entradaMesInicio,entradaAnnoInicio,
                new Label("-"), entradaDiaFin, entradaMesFin, entradaAnnoFin);
        casetillasFecha.setSpacing(10);

        RadioButton sismosProvincia = new RadioButton();
        sismosProvincia.setToggleGroup(grupoBotonesGraficos);
        sismosProvincia.setText("Sismos por provincia");
        sismosProvincia.setUserData("Sismos por provincia");
        sismosProvincia.setSelected(true);

        RadioButton sismosOrigen = new RadioButton();
        sismosOrigen.setToggleGroup(grupoBotonesGraficos);
        sismosOrigen.setText("Sismos por origen");
        sismosOrigen.setUserData("Sismos por origen");

        RadioButton sismosFechas = new RadioButton();
        sismosFechas.setText("Sismos por rango de fecha");
        sismosFechas.setToggleGroup(grupoBotonesGraficos);
        sismosFechas.setUserData("Sismos por rango de fecha");

        RadioButton sismosAnno = new RadioButton();
        sismosAnno.setText("Sismos por mes");
        sismosAnno.setToggleGroup(grupoBotonesGraficos);
        sismosAnno.setUserData("Sismos por mes");

        RadioButton sismosMagnitud = new RadioButton();
        sismosMagnitud.setText("Sismos por magnitud");
        sismosMagnitud.setToggleGroup(grupoBotonesGraficos);
        sismosMagnitud.setUserData("Sismos por magnitud");

        entradaDiaInicio.setDisable(true);
        entradaAnnoMes.setDisable(true);
        entradaAnnoInicio.setDisable(true);
        entradaMesInicio.setDisable(true);

        entradaDiaFin.setDisable(true);
        entradaMesFin.setDisable(true);
        entradaAnnoFin.setDisable(true);

        grupoBotonesGraficos.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue){

                if(grupoBotonesGraficos.getSelectedToggle().getUserData().toString()=="Sismos por provincia"|
                        grupoBotonesGraficos.getSelectedToggle().getUserData().toString()=="Sismos por origen"|
                        grupoBotonesGraficos.getSelectedToggle().getUserData().toString()=="Sismos por magnitud") {

                    entradaDiaInicio.setDisable(true);
                    entradaAnnoMes.setDisable(true);
                    entradaAnnoInicio.setDisable(true);
                    entradaMesInicio.setDisable(true);

                    entradaDiaFin.setDisable(true);
                    entradaMesFin.setDisable(true);
                    entradaAnnoFin.setDisable(true);
                }
                else if(grupoBotonesGraficos.getSelectedToggle().getUserData().toString()=="Sismos por rango de fecha"){
                    entradaDiaInicio.setDisable(false);
                    entradaMesInicio.setDisable(false);
                    entradaAnnoMes.setDisable(true);

                    entradaDiaFin.setDisable(false);
                    entradaMesFin.setDisable(false);
                    entradaAnnoFin.setDisable(false);

                    entradaAnnoInicio.setDisable(false);
                }
                else{
                    entradaDiaInicio.setDisable(true);
                    entradaAnnoMes.setDisable(false);
                    entradaAnnoInicio.setDisable(true);
                    entradaMesInicio.setDisable(true);

                    entradaDiaFin.setDisable(true);
                    entradaMesFin.setDisable(true);
                    entradaAnnoFin.setDisable(true);
                }
            }});

        contenedorSismosAnno.getChildren().addAll(sismosAnno,entradaAnnoMes);
        contenedorSismosAnno.setSpacing(10);

        contenedorSismosFecha.getChildren().addAll(sismosFechas,casetillasFecha);
        contenedorSismosFecha.setSpacing(10);
        contenedorSismosFecha.setAlignment(Pos.CENTER);

        cuadrillaGraficos.add(sismosProvincia,0,0);
        cuadrillaGraficos.add(sismosOrigen,0,1);
        cuadrillaGraficos.add(contenedorSismosFecha,1,0,1,2);
        cuadrillaGraficos.add(contenedorSismosAnno,1,2,1,2);
        cuadrillaGraficos.add(sismosMagnitud,0,2);
        cuadrillaGraficos.add(contenedorBotonReporte,1,4);
        cuadrillaGraficos.setGridLinesVisible(false);

        botonGraficos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String selecionBotonesRadio = grupoBotonesGraficos.getSelectedToggle().getUserData().toString();
                int modalidad;
                ArrayList<String> fechas = new ArrayList<String>();

                if(selecionBotonesRadio.equals("Sismos por provincia")){
                    modalidad = 0;
                } else if(selecionBotonesRadio.equals("Sismos por origen")){
                    modalidad = 1;
                } else if(selecionBotonesRadio.equals("Sismos por magnitud")){
                    modalidad = 2;
                } else if(selecionBotonesRadio.equals("Sismos por rango de fecha")){
                    modalidad = 3;
                    Object dia= entradaDiaInicio.getSelectionModel().getSelectedItem();
                    Object mes= entradaMesInicio.getSelectionModel().getSelectedItem();
                    Object anno = entradaAnnoMes.getSelectionModel().getSelectedItem();
                    String fechaPrimera= objetoUtilitario.verificarInfo(dia,mes,anno,"/",true);

                    Object diaSegundo=entradaDiaFin.getSelectionModel().getSelectedItem();
                    Object mesSegundo=entradaMesFin.getSelectionModel().getSelectedItem();
                    Object annoSegundo=entradaAnnoFin.getSelectionModel().getSelectedItem();
                    String fechaSegunda=objetoUtilitario.verificarInfo(diaSegundo,mesSegundo,annoSegundo,"/",true);
                    fechas.add(fechaPrimera);
                    fechas.add(fechaSegunda);
                } else {
                    modalidad = 4;
                    if(entradaAnnoMes.getSelectionModel().getSelectedItem() == null){
                        fechas.add("0000");
                    } else {
                        fechas.add(entradaAnnoMes.getSelectionModel().getSelectedItem()+"");
                    }
                }
                clickBotonGenerarReporte(modalidad, fechas);
            }
        });

        Scene escenaGraficos= new Scene(cuadrillaGraficos);
        escenaGraficos.getStylesheets().add
                (Interfaz.class.getResource("Estilos.css").toExternalForm());
        escenarioGraficos.setScene(escenaGraficos);
        escenarioGraficos.initModality(Modality.APPLICATION_MODAL);
        escenarioGraficos.setResizable(false);
        escenarioGraficos.showAndWait();

    }

    private void clickBotonDetallesSismo(Sismo pSismo)
    {
        Stage escenarioDetalles = new Stage();

        Text encabezadoDetalles = new Text("Información del Sismo");
        encabezadoDetalles.setId("encabezado-sub");

        String etiquetasSub= "etiquetas-sub";
        int espaciadoContenedores = 10;

        HBox contenedorFecha = new HBox();
        Label etiquetaFecha = new Label("Fecha:");
        etiquetaFecha.setId(etiquetasSub);
        contenedorFecha.getChildren().addAll(etiquetaFecha, new Label(pSismo.getFecha()));
        contenedorFecha.setSpacing(espaciadoContenedores);

        HBox contenedorTiempo = new HBox();
        Label etiquetaTiempo = new Label("Tiempo:");
        etiquetaTiempo.setId(etiquetasSub);
        contenedorTiempo.getChildren().addAll(etiquetaTiempo, new Label(pSismo.getTiempo()));
        contenedorTiempo.setSpacing(espaciadoContenedores);

        HBox contenedorOrigen = new HBox();
        Label etiquetaOrigen = new Label("Origen:");
        etiquetaOrigen.setId(etiquetasSub);
        contenedorOrigen.getChildren().addAll(etiquetaOrigen, new Label(pSismo.getOrigenFalla()));
        contenedorOrigen.setSpacing(espaciadoContenedores);

        HBox contenedorProfundidad = new HBox();
        Label etiquetaProfundidad = new Label("Profundidad:");
        etiquetaProfundidad.setId(etiquetasSub);
        contenedorProfundidad.getChildren().addAll(etiquetaProfundidad, new Label(pSismo.getProfundidadKm() + "Km"));
        contenedorProfundidad.setSpacing(espaciadoContenedores);

        HBox contenedorMagnitud = new HBox();
        Label etiquetaMagnitud = new Label("Magnitud:");
        etiquetaMagnitud.setId(etiquetasSub);
        contenedorMagnitud.getChildren().addAll(etiquetaMagnitud, new Label(pSismo.getMagnitud()));
        contenedorMagnitud.setSpacing(espaciadoContenedores);

        HBox contenedorLatitud = new HBox();
        Label etiquetaLatitud = new Label("Latitud:");
        etiquetaLatitud.setId(etiquetasSub);
        contenedorLatitud.getChildren().addAll(etiquetaLatitud, new Label("" + pSismo.getLocalizacion().getLatitud()));
        contenedorLatitud.setSpacing(espaciadoContenedores);

        HBox contenedorLongitud = new HBox();
        Label etiquetaLongitud = new Label("Longitud:");
        etiquetaLongitud.setId(etiquetasSub);
        contenedorLongitud.getChildren().addAll(etiquetaLongitud, new Label("" + pSismo.getLocalizacion().getLongitud()));
        contenedorLongitud.setSpacing(espaciadoContenedores);

        HBox contenedorTerrestre = new HBox();
        Label etiquetaTerrestre = new Label("Tipo:");
        etiquetaTerrestre.setId(etiquetasSub);
        String tipo;
        if(pSismo.getLocalizacion().isEsTerrestre()){
            tipo = "Terrestre";
        } else {
            tipo = "Maritimo";
        }
        contenedorTerrestre.getChildren().addAll(etiquetaTerrestre, new Label(tipo));
        contenedorTerrestre.setSpacing(espaciadoContenedores);

        HBox contenedorProvincia = new HBox();
        Label etiquetaProvincia = new Label("Provincia:");
        etiquetaProvincia.setId(etiquetasSub);
        contenedorProvincia.getChildren().addAll(etiquetaProvincia, new Label(pSismo.getLocalizacion().getProvincia()));
        contenedorProvincia.setSpacing(espaciadoContenedores);

        VBox contenedorDescripcion = new VBox();
        Label etiquetaDescripcion = new Label("Descripción:");
        etiquetaDescripcion.setId(etiquetasSub);
        Label etiquetaCuerpoDescripcion = new Label(pSismo.getLocalizacion().getDescripcion());
        etiquetaCuerpoDescripcion.setPrefWidth(300);
        etiquetaCuerpoDescripcion.setWrapText(true);
        contenedorDescripcion.getChildren().addAll(etiquetaDescripcion, etiquetaCuerpoDescripcion);
        contenedorDescripcion.setSpacing(espaciadoContenedores);

        Button botonCerrar = new Button("Cerrar");
        botonCerrar.setId("botones-detalles");
        botonCerrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                escenarioDetalles.close();
            }
        });

        Button botonEditar = new Button("Editar");
        botonEditar.setId("botones-detalles");
        botonEditar.setOnAction(evento->clickBotonAgregarSismo(true,pSismo));

        Button botonVerMapa = new Button("Ver Mapa");
        botonVerMapa.setId("botones-detalles");
        botonVerMapa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    new FrameMapa(pSismo.getLocalizacion().getLatitud(),pSismo.getLocalizacion().getLongitud()).setVisible(true);
                } catch (Exception e) {
                    mensajeError(1);
                }
            }
        });

        HBox contenedorBotones = new HBox();
        contenedorBotones.setSpacing(5);
        contenedorBotones.setAlignment(Pos.BOTTOM_RIGHT);
        contenedorBotones.getChildren().addAll(botonVerMapa, botonEditar, botonCerrar);

        GridPane cuadrillaDetalles = new GridPane();
        cuadrillaDetalles.setVgap(10);
        cuadrillaDetalles.setHgap(40);
        cuadrillaDetalles.setPadding(new Insets(20,20,20,20));
        cuadrillaDetalles.setGridLinesVisible(false);

        cuadrillaDetalles.add(encabezadoDetalles, 0, 0, 2, 1);
        cuadrillaDetalles.add(contenedorFecha, 0, 1);
        cuadrillaDetalles.add(contenedorTiempo, 1, 1);
        cuadrillaDetalles.add(contenedorOrigen, 0, 2, 2, 1);
        cuadrillaDetalles.add(contenedorProfundidad, 0, 3);
        cuadrillaDetalles.add(contenedorMagnitud, 1, 3);
        cuadrillaDetalles.add(contenedorLatitud, 0, 4);
        cuadrillaDetalles.add(contenedorLongitud, 0, 5);
        cuadrillaDetalles.add(contenedorProvincia, 1, 4);
        cuadrillaDetalles.add(contenedorTerrestre, 1, 5);
        cuadrillaDetalles.add(contenedorDescripcion, 0, 6, 4, 3);
        cuadrillaDetalles.add(contenedorBotones, 1, 9, 3, 1);

        Scene escenaDetalles = new Scene(cuadrillaDetalles);
        escenaDetalles.getStylesheets().add
                (Interfaz.class.getResource("Estilos.css").toExternalForm());
        escenarioDetalles.setScene(escenaDetalles);
        escenarioDetalles.initModality(Modality.APPLICATION_MODAL);
        escenarioDetalles.setResizable(false);
        escenarioDetalles.showAndWait();

    }

    public void construirInterfazSismos(VBox contenedorVerticalSismos,VBox contenedorVerticalBotones, ArrayList<Sismo>listaSismos) {
        int indice = 0;
        contenedorVerticalSismos.getChildren().clear();
        contenedorVerticalBotones.getChildren().clear();

        while (indice < listaSismos.size()) {
            Sismo sismoActual = listaSismos.get(indice);
            HBox contenedorHorizontalTemporal = new HBox();
            Button botonTemporal = new Button("Detalles");
            botonTemporal.setOnAction(evento->clickBotonDetallesSismo(sismoActual));
            contenedorHorizontalTemporal.getChildren().addAll(
                    new Label(sismoActual.getFecha()),
                    new Label(sismoActual.getTiempo()),
                    new Label(sismoActual.getMagnitud()),
                    new Label(sismoActual.getLocalizacion().getProvincia()));
            contenedorVerticalSismos.getChildren().add(contenedorHorizontalTemporal);
            contenedorVerticalBotones.getChildren().add(botonTemporal);
            contenedorHorizontalTemporal.setSpacing(75);
            indice++;
        }
    }

    public void envioCorreo(String provincia)throws Exception{
        int indice=0;

        for(PersonaInteresada persona : listaPersonas){
            ArrayList<String> provincias= persona.getProvinciasInteres();
            while(indice< provincias.size()){

                if(provincias.get(indice).equals(provincia)){
                    String correo = persona.getCorreoElectronico();
                    objetoUtilitario.mandarCorreo(correo,provincia);
                    break;
                }
                indice++;
            }
            indice=0;
        }
    }

    public void mensajeError(int modalidad){
        Stage escenarioError = new Stage();
        escenarioError.setTitle("Error");

        Label cuerpoError = new Label();
        cuerpoError.setId("mensaje-error");

        switch (modalidad){
            case 0: cuerpoError.setText("Error en la entrada de datos.");
                break;
            case 1: cuerpoError.setText("Error en la conexión");
                break;
            case 2: cuerpoError.setText("Debe ingresar todos los datos.");
                break;
            case 3: cuerpoError.setText("Error el enviar correo. Por favor revise su conexión.");
                break;
            case 4: cuerpoError.setText("Error en el CSV. Por favor verifique que el archivo tablasSismos.csv se " +
                    "encuentre en el directorio de instalación.");
                break;
            case 5: cuerpoError.setText("Error al cargar los contactos.");
                break;
            default:
                cuerpoError.setText("Error desconocido.");
                break;
        }

        GridPane cuadrillaError = new GridPane();
        cuadrillaError.add(cuerpoError, 0, 0, 4, 4);
        cuadrillaError.setAlignment(Pos.CENTER);
        cuadrillaError.setPadding(new Insets(20, 20, 20, 20));
        cuadrillaError.setVgap(10);

        Scene escenaError = new Scene(cuadrillaError);

        escenarioError.setScene(escenaError);
        escenarioError.initModality(Modality.APPLICATION_MODAL);
        escenarioError.setResizable(false);
        escenarioError.showAndWait();
    }

    public void clickBotonGenerarReporte(int modalidad, ArrayList<String> fechas){

        switch (modalidad){
            case 0: crearHistograma();
                break;
            case 1: crearGraficoPastel();
                break;
            case 2: crearGraficoTabularMagnitud();
                break;
            case 3: System.out.println("Por hacer"); //TODO
                break;
            case 4: crearGraficoBarras(fechas.get(0));

        }

    }

    public void crearHistograma(){
        Stage escenarioHistograma = new Stage();
        escenarioHistograma.setTitle("Histograma");

        String sanJose = "San José";
        String alajuela = "Alajuela";
        String heredia = "Heredia";
        String cartago = "Cartago";
        String limon = "Limón";
        String puntarenas = "Puntarenas";
        String guanacaste = "Guanacaste";

        int cantSanJose = objetoUtilitario.contadorOrigenProvincia("San José", listaObjetosSismo, true);
        int cantAlajuela = objetoUtilitario.contadorOrigenProvincia("Alajuela", listaObjetosSismo, true);
        int cantHeredia = objetoUtilitario.contadorOrigenProvincia("Heredia", listaObjetosSismo, true);
        int cantCartago = objetoUtilitario.contadorOrigenProvincia("Cartago", listaObjetosSismo, true);
        int cantLimon = objetoUtilitario.contadorOrigenProvincia("Limón", listaObjetosSismo, true);
        int cantPuntarenas = objetoUtilitario.contadorOrigenProvincia("Puntarenas", listaObjetosSismo, true);
        int cantGuanacaste = objetoUtilitario.contadorOrigenProvincia("Guanacaste", listaObjetosSismo, true);

        VBox contenedorGrafico = new VBox();
        CategoryAxis ejeX = new CategoryAxis();
        NumberAxis ejeY = new NumberAxis();
        BarChart<String, Number> graficoBarra = new BarChart<String, Number>(ejeX, ejeY);
        graficoBarra.setTitle("Sismos por Provincias");
        ejeX.setLabel("Provincia");
        ejeY.setLabel("Total de Sismos");

        XYChart.Series serieProvincias = new XYChart.Series();
        serieProvincias.setName("Cantidad de Provincias");
        serieProvincias.getData().addAll(
                new XYChart.Data(sanJose, cantSanJose),
                new XYChart.Data(alajuela, cantAlajuela),
                new XYChart.Data(heredia, cantHeredia),
                new XYChart.Data(cartago, cantCartago),
                new XYChart.Data(limon, cantLimon),
                new XYChart.Data(puntarenas, cantPuntarenas),
                new XYChart.Data(guanacaste, cantGuanacaste)
        );
        graficoBarra.getData().add(serieProvincias);

        contenedorGrafico.getChildren().add(graficoBarra);

        Scene escenaHistograma = new Scene(contenedorGrafico);
        escenarioHistograma.setScene(escenaHistograma);
        escenarioHistograma.initModality(Modality.APPLICATION_MODAL);
        escenarioHistograma.setResizable(false);
        escenarioHistograma.showAndWait();
    }

    public void crearGraficoPastel(){
        Stage escenarioPastel = new Stage();
        escenarioPastel.setTitle("Grafico por Origen");

        int subduccion = objetoUtilitario.contadorOrigenProvincia("Subduccion", listaObjetosSismo,false);
        int choquePlacas = objetoUtilitario.contadorOrigenProvincia("Choque de placas", listaObjetosSismo,false);
        int tectonico = objetoUtilitario.contadorOrigenProvincia("Tectonico por falla local", listaObjetosSismo,false);
        int intraPlaca = objetoUtilitario.contadorOrigenProvincia("Intra placa", listaObjetosSismo,false);
        int deformacion = objetoUtilitario.contadorOrigenProvincia("Deformacion interna", listaObjetosSismo,false);

        VBox contenedorGrafico = new VBox();
        ObservableList<PieChart.Data> datosGrafico =
                FXCollections.observableArrayList(
                        new PieChart.Data("Subducción\n" + subduccion, subduccion),
                        new PieChart.Data("Choque de placas\n" + choquePlacas, choquePlacas),
                        new PieChart.Data("Tectonico por falla local\n" + tectonico, tectonico),
                        new PieChart.Data("Intra placa\n" + intraPlaca, intraPlaca),
                        new PieChart.Data("Deformacion interna\n" + deformacion, deformacion)
                );

        final PieChart graficoPastel = new PieChart(datosGrafico);
        graficoPastel.setTitle("Sismos por Origen");
        graficoPastel.setStartAngle(350);

        contenedorGrafico.getChildren().add(graficoPastel);

        Scene escenaPastel = new Scene(contenedorGrafico);
        escenarioPastel.setScene(escenaPastel);
        escenarioPastel.initModality(Modality.APPLICATION_MODAL);
        escenarioPastel.setResizable(false);
        escenarioPastel.showAndWait();
    }

    public void crearGraficoBarras(String anno){
        Stage escenarioHistograma = new Stage();
        escenarioHistograma.setTitle("Grafico de Barras");



        VBox contenedorGrafico = new VBox();
        CategoryAxis ejeX = new CategoryAxis();
        NumberAxis ejeY = new NumberAxis();
        BarChart<String, Number> graficoBarra = new BarChart<String, Number>(ejeX, ejeY);
        graficoBarra.setTitle("Sismos por Mes");
        ejeX.setLabel("Año: " + anno);
        ejeY.setLabel("Total de Sismos");

        int cantEnero= objetoUtilitario.contarSismosMes("01",anno,listaObjetosSismo);
        int cantFebrero= objetoUtilitario.contarSismosMes("02",anno,listaObjetosSismo);
        int cantMarzo= objetoUtilitario.contarSismosMes("03",anno,listaObjetosSismo);
        int cantAbril= objetoUtilitario.contarSismosMes("04",anno,listaObjetosSismo);
        int cantMayo= objetoUtilitario.contarSismosMes("05",anno,listaObjetosSismo);
        int cantJunio= objetoUtilitario.contarSismosMes("06",anno,listaObjetosSismo);
        int cantJulio= objetoUtilitario.contarSismosMes("07",anno,listaObjetosSismo);
        int cantAgosto= objetoUtilitario.contarSismosMes("08",anno,listaObjetosSismo);
        int cantSetiembre= objetoUtilitario.contarSismosMes("09",anno,listaObjetosSismo);
        int cantOctubre= objetoUtilitario.contarSismosMes("10",anno,listaObjetosSismo);
        int cantNoviembre= objetoUtilitario.contarSismosMes("11",anno,listaObjetosSismo);
        int cantDiciembre= objetoUtilitario.contarSismosMes("12",anno,listaObjetosSismo);


        XYChart.Series serieMeses = new XYChart.Series();
        serieMeses.setName("Sismos por Mes");
        serieMeses.getData().addAll(
                new XYChart.Data("Enero", cantEnero),
                new XYChart.Data("Febrero", cantFebrero),
                new XYChart.Data("Marzo", cantMarzo),
                new XYChart.Data("Abril", cantAbril),
                new XYChart.Data("Mayo", cantMayo),
                new XYChart.Data("Junio", cantJunio),
                new XYChart.Data("Julio", cantJulio),
                new XYChart.Data("Agosto", cantAgosto),
                new XYChart.Data("Setiembre", cantSetiembre),
                new XYChart.Data("Octubre", cantOctubre),
                new XYChart.Data("Noviembre", cantNoviembre),
                new XYChart.Data("Diciembre", cantDiciembre)
        );
        graficoBarra.getData().add(serieMeses);

        contenedorGrafico.getChildren().add(graficoBarra);

        Scene escenaHistograma = new Scene(contenedorGrafico);
        escenarioHistograma.setScene(escenaHistograma);
        escenarioHistograma.initModality(Modality.APPLICATION_MODAL);
        escenarioHistograma.setResizable(false);
        escenarioHistograma.showAndWait();
    }

    public void crearGraficoTabularMagnitud(){
        Stage escenarioTabla = new Stage();
        escenarioTabla.setTitle("Grafico por Origen");

        int micro = objetoUtilitario.contarMagnitud(0.0, 1.9, listaObjetosSismo);
        int menor = objetoUtilitario.contarMagnitud(2.0, 3.9, listaObjetosSismo);
        int ligero = objetoUtilitario.contarMagnitud(4.0, 4.9, listaObjetosSismo);
        int moderado = objetoUtilitario.contarMagnitud(5.0, 5.9, listaObjetosSismo);
        int fuerte = objetoUtilitario.contarMagnitud(6.0, 6.9, listaObjetosSismo);
        int mayor = objetoUtilitario.contarMagnitud(7.0, 7.9, listaObjetosSismo);
        int gran = objetoUtilitario.contarMagnitud(8.0, 9.9, listaObjetosSismo);
        int epico = objetoUtilitario.contarMagnitud(10.0, 99.9, listaObjetosSismo);

        HBox contenedorHorizontal = new HBox();
        VBox columnaNombre = new VBox();
        VBox columnaCantidad = new VBox();

        columnaNombre.getChildren().addAll(
                new Label("Categoria"),
                new Label("Micro"),
                new Label("Menor"),
                new Label("Ligero"),
                new Label("Moderado"),
                new Label("Fuerte"),
                new Label("Mayor"),
                new Label("Gran"),
                new Label("Epico")
        );

        columnaCantidad.getChildren().addAll(
                new Label("Cantidad"),
                new Label(micro+""),
                new Label(menor+""),
                new Label(ligero+""),
                new Label(moderado+""),
                new Label(fuerte+""),
                new Label(mayor+""),
                new Label(gran+""),
                new Label(epico+"")
        );
        columnaCantidad.setAlignment(Pos.TOP_CENTER);

        contenedorHorizontal.getChildren().addAll(columnaNombre, columnaCantidad);
        contenedorHorizontal.setPadding(new Insets(10,10,10,10));
        contenedorHorizontal.setSpacing(20);

        Scene escenaTabla = new Scene(contenedorHorizontal);
        escenarioTabla.setScene(escenaTabla);
        escenarioTabla.initModality(Modality.APPLICATION_MODAL);
        escenarioTabla.setResizable(false);
        escenarioTabla.showAndWait();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
