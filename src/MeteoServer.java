import org.eclipse.paho.client.mqttv3.*;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MeteoServer {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("54.166.107.43", 6000);
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
        try {
            publisher.connect(options);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        publisher.setCallback(new MqttCallback() {

            public void messageArrived(String topic, MqttMessage message) throws Exception {
                lastmeasure(jedis, message);
                temperatures(jedis, message);
            }

            public void connectionLost(Throwable cause) {
                System.out.println("Connection to Solace broker lost!" + cause.getMessage());
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
            }

        });

        jedis.close();
    }
    public static void lastmeasure(Jedis jedis, MqttMessage message) {
        String[] spl = message.toString().split(" ");

        jedis.del("LAU:LASTMEASUREMENT:ID");

        jedis.hset("LAU:LASTMEASUREMENT:ID", String.valueOf(LocalTime.now()), String.valueOf(spl [2]));

        Set<String> keys =jedis.hkeys("LAU:LASTMEASUREMENT:ID");

        for (String c :keys) {
            System.out.println(c +": "+jedis.hget("LAU:LASTMEASUREMENT:ID", c));
        }
        jedis.del("LAU:LASTMEASUREMENT:ID");
    }

    public static void temperatures(Jedis jedis, MqttMessage message) {
        String[] spl = message.toString().split(" ");

        jedis.del("LAU:TEMPERATURES:ID");

        jedis.rpush("LAU:TEMPERATURES:ID", String.valueOf(spl[2]));

        List<String> values2 = jedis.lrange("LAU:TEMPERATURES:ID", 0, -1);
        for (String v : values2) {
            System.out.println(v);
        }

        jedis.del("ABC:TEMPERATURES:ID");
    }
}
