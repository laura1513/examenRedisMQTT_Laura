import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.UUID;

public class MeteoClient {
    public static void main(String[] args) {
        String publisherId = UUID.randomUUID().toString();
        IMqttClient publisher = null;
        try {
            publisher = new MqttClient("tcp://54.166.107.43:1883", publisherId);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);

        String command = "";
        Scanner scanner = new Scanner(System.in);
        while (!command.equals("exit")) {
            System.out.print("Enter command: ");
            String rawCommand = scanner.nextLine() + " ";
            String[] commandSplit = rawCommand.split(" ", 3);
            command = commandSplit[0];
            if (command.equals("lastid")) {

            } else if (command.equals("maxtempid")) {

            } else if (command.equals("maxtempall")) {

            } else if (command.equals("alerts")) {

            } else {
                System.out.println("COMANDO NO VÁLIDO");
            }
        }
        try {
            publisher.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
