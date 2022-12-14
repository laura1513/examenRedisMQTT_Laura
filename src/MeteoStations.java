import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MeteoStations {
    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        IMqttClient publisher = null;
        try {
            publisher = new MqttClient("tcp://54.166.107.43:1883",publisherId);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        try {
            publisher.connect(options);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);

        //Valores aleatorios para la fecha
        int dia;
        int mes;
        int anyo;
        String fecha;

        //Valores aleatorios para la hora
        int h;
        int min;
        String hora;

        //Valores aleatorios pata la temperatura
        double temperatura;
        String temp;

        String mensaje;
        String topic;

        while (true) {
            topic = "/LAU/METEO/"+ String.valueOf((int) (1 + (Math.random()*10)))+"/MEASUREMENTS";

            dia = (int) (1 + (Math.random()*29));
            mes = (int) (1 + (Math.random()*12));
            anyo = (int) (2000 + (Math.random()*2022));
            fecha = String.valueOf(dia) + "/" + String.valueOf(mes) + "/" + String.valueOf(anyo);

            h = (int) (Math.random()*23);
            min = (int) (Math.random()*59);
            hora = String.valueOf(h) +  ":" + String.valueOf(min);

            temperatura = -10 + (Math.random()*40);
            temp = temperatura + "ºC";

            mensaje = fecha + " " + hora+ " " + temp;

            System.out.println(mensaje);

            try {
                ExecutorService executorService = Executors.newFixedThreadPool(5);
                Future<Boolean> result = executorService.submit(new MeteoStationsPublisher(publisher, mensaje, topic));
                if (result.get()) {

                    System.out.println("MQTT message sended");
                } else {
                    System.out.println("MQTT message NOT sended");
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                publisher.disconnect();
                publisher.close();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
