package utilitarias;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import logica.Localizacion;
import logica.PersonaInteresada;
import logica.Sismo;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Utilitaria {

    public boolean crearCSV() throws IOException{
        try{
            File archivoCSV = new File("tablasSismos.csv");

            if(!(archivoCSV.exists())){
                archivoCSV.createNewFile();
            }

            return true;
        }catch (Exception error){
            return false;
        }
    }


    public ArrayList<Sismo> crearListaSismos() throws IOException{
        ArrayList<Sismo> listaSismos = new ArrayList<>();
        CSVReader lectorCSV = new CSVReader(new FileReader("tablasSismos.csv"));
        String[] lineaActual;

        while((lineaActual = lectorCSV.readNext()) != null){
            /*
            * 0: Fecha          5: Latitud
            * 1: Hora           6: Longitud
            * 2: Profundidad    7: Descripcion
            * 3: Origen         8: esTerrestre
            * 4: Magnitud       9: Provincia
            * */
            Localizacion localizacionTemporal = new Localizacion(Double.parseDouble(lineaActual[5]), Double.parseDouble(lineaActual[6]),
                    lineaActual[7], lineaActual[8], lineaActual[9]);
            Sismo sismoTemporal = new Sismo(lineaActual[0], lineaActual[1], lineaActual[2],
                    lineaActual[3], lineaActual[4], localizacionTemporal, lineaActual[10]);

            listaSismos.add(sismoTemporal);
        }

        return listaSismos;
    }

    public ArrayList<PersonaInteresada> crearListaPersonas(File archivo) throws IOException{

        ArrayList<PersonaInteresada> listaPersonas = new ArrayList<>();
        CSVReader lectorCSV = new CSVReader(new FileReader(archivo));
        ArrayList<String> provinciasInteres = new ArrayList<>();
        String[] lineaActual;

        while((lineaActual = lectorCSV.readNext()) != null){
            provinciasInteres.clear();
            /*
            * 0: Nombre
            * 1: Correo
            * 2: Numero
            * 3: Provincias
            *
            * */
            provinciasInteres.add(lineaActual[3]); provinciasInteres.add(lineaActual[4]); provinciasInteres.add(lineaActual[5]);
            provinciasInteres.add(lineaActual[6]); provinciasInteres.add(lineaActual[7]); provinciasInteres.add(lineaActual[8]);
            provinciasInteres.add(lineaActual[9]);

            PersonaInteresada personaTemporal = new PersonaInteresada(lineaActual[0], lineaActual[1],
                    lineaActual[2],provinciasInteres);

            listaPersonas.add(personaTemporal);
        }

        return listaPersonas;
    }

    public void escribirCSV (ArrayList<String> infoFormulario) throws IOException{
        FileWriter lectorArchivos = new FileWriter("tablasSismos.csv", true);
        CSVWriter escritorCSV = new CSVWriter(lectorArchivos);
        String[] arregloLinea = new String[11];
        arregloLinea[10] = ""+contarLineasCSV();

        int indice = 0;

        while(indice < infoFormulario.size()){
            arregloLinea[indice] = infoFormulario.get(indice);
            indice++;
        }

        escritorCSV.writeNext(arregloLinea);
        escritorCSV.close();
    }

    public void editarCSV(String[] sismoEditado) throws IOException{
        CSVReader lectorCSV = new CSVReader(new FileReader("tablasSismos.csv"));
        List<String[]> arregloCSV = lectorCSV.readAll();
        ArrayList<String[]> edicionCSV = new ArrayList<>();

        int indice = 0;

        while(indice < arregloCSV.size()){
            String[] lineaActual = arregloCSV.get(indice);

            if(lineaActual[10].equals(sismoEditado[10])){
                edicionCSV.add(sismoEditado);
            }else{
                edicionCSV.add(lineaActual);
            }

            indice++;
        }

        lectorCSV.close();

        CSVWriter escritorCSV = new CSVWriter(new FileWriter("tablasSismos.csv"));
        escritorCSV.writeAll(edicionCSV);
        escritorCSV.close();

    }

    private int contarLineasCSV() throws IOException{
        CSVReader lectorCSV = new CSVReader(new FileReader("tablasSismos.csv"));
        return lectorCSV.readAll().size();
    }

    public String verificarInfo(Object hora,Object minutos,Object segundos,String espaciador, boolean esFecha){
        String tiempo= "";
        int indice=0;
        Object [] infoTiempo={hora,minutos,segundos};
        while(indice<infoTiempo.length){

            if(infoTiempo[indice]==null & indice==2){
                if (esFecha) {
                    tiempo+="0000";
                } else {
                    tiempo+="00";
                }
            }
            else if(infoTiempo[indice]==null){
                tiempo+="00"+espaciador;
            }
            else if(indice==2){
                tiempo+=infoTiempo[indice];
            }
            else {
                tiempo += infoTiempo[indice] + espaciador;
            }
            indice++;

        }
        return tiempo;
    }

    public String verificarOrigenProvincia(Object origenProvincia){
        String origenSismo="";

        if(origenProvincia==null)
            origenSismo+="Sin definir";
        else{
            origenSismo+= origenProvincia;
        }
        return origenSismo;
    }

    public void mandarCorreo(String correo,String provincia) throws Exception {

        String correoEnvia = "proyectopoo2016@hotmail.com";
        String claveCorreo = "123jeje#";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.live.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.user", correoEnvia);
        properties.put("mail.password", claveCorreo);

        try {
            Session session = Session.getInstance(properties, null);

            MimeMessage mimeMessage = new MimeMessage(session);

            mimeMessage.setFrom(new InternetAddress(correoEnvia, "POO Proyecto"));

            mimeMessage.setRecipients(Message.RecipientType.TO,correo);

            mimeMessage.setSubject("Notificación de Sismo");


            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText("Se le notifica sobre un sismo ocurrido en la provincia de: " + provincia);


            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            mimeMessage.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia, claveCorreo);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean validarDoubles(String doubleString){
        try{
            Double.parseDouble(doubleString);
            return false;
        } catch (Exception error){
            return true;
        }
    }

    public boolean validarFormulario(String[] info){
        return !(info[0].equals("00/00/0000") || info[1].equals("00:00:00") || info[2].equals("") ||
                info[3].equals("Sin definir") || info[4].equals("") || info[5].equals("") ||
                info[6].equals("") || info[7].equals("") || info[9].equals("Sin definir") ||
                validarDoubles(info[2]) || validarDoubles(info[4]) || validarDoubles(info[5]) ||
                validarDoubles(info[6]));
    }

    public boolean validarFormulario(ArrayList<String> info){
        return !(info.get(0).equals("00/00/0000") || info.get(1).equals("00:00:00") || info.get(2).equals("") ||
                info.get(3).equals("Sin definir") || info.get(4).equals("") || info.get(5).equals("") ||
                info.get(6).equals("") || info.get(7).equals("") || info.get(9).equals("Sin definir") ||
                validarDoubles(info.get(2)) || validarDoubles(info.get(4)) || validarDoubles(info.get(5)) ||
                validarDoubles(info.get(6)));
    }

    public int contadorOrigenProvincia(String buscado, ArrayList<Sismo> listaSismos, boolean esProvincia){
        int cantidad = 0;
        int indice = 0;

        while(indice < listaSismos.size()){
            if (esProvincia) {
                if(listaSismos.get(indice).getLocalizacion().getProvincia().equals(buscado)){
                    cantidad++;
                }
            } else {
                if(listaSismos.get(indice).getOrigenFalla().equals(buscado)){
                    cantidad++;
                }
            }
            indice++;
        }

        return cantidad;
    }

    public int contarMagnitud(double inferior, double superior, ArrayList<Sismo> listasSismos){
        int indice = 0;
        int cantidad = 0;
        double magnitudActual;

        while(indice < listasSismos.size()){
            magnitudActual = Double.parseDouble(listasSismos.get(indice).getMagnitud());
            if(inferior <= magnitudActual & magnitudActual <= superior){
                cantidad++;
            }
            indice++;
        }
        return cantidad;
    }

    public int contarSismosMes(String mes, String anno, ArrayList<Sismo> listaSismos){
        int indice = 0;
        int cantidad = 0;
        String annoActual;
        String mesActual;

        while(indice < listaSismos.size()){
            annoActual = listaSismos.get(indice).getFecha().substring(6, 10);
            if(anno.equals(annoActual)){
                mesActual = listaSismos.get(indice).getFecha().substring(3,5);
                if(mes.equals(mesActual)){
                    cantidad++;
                }
            }
            indice++;
        }

        return cantidad;
    }

    public int contarSismosRango(String fechaMin, String fechaMax, ArrayList<Sismo> listaSismos) throws Exception{
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaMinima = formateador.parse(fechaMin);
        Date fechaMaxima = formateador.parse(fechaMax);

        int i = 0;
        int contador = 0;

        while(i < listaSismos.size()){
            Date fechaActual = formateador.parse(listaSismos.get(i).getFecha());
            System.out.println(fechaMinima.before(fechaActual));
            if((fechaMinima.before(fechaActual)|fechaMinima.equals(fechaMinima)) & (fechaMaxima.after(fechaActual)|fechaMaxima.equals(fechaActual))){
                contador++;
            }
            i++;
        }

        return contador;
    }

}