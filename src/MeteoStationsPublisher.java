import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.Callable;

public class MeteoStationsPublisher implements Callable<Boolean> {
    IMqttClient client;
    String mensaje;
    String topic;

    public MeteoStationsPublisher(IMqttClient client, String mesaje, String topic) {
        this.client = client;
        this.mensaje = mesaje;
        this.topic = topic;
    }

    @Override
    public Boolean call() throws Exception {
        Thread.sleep(5000); // Only to test futures
        if ( !client.isConnected()) {
            return false;
        }
        byte[] payload = mensaje.getBytes();
        MqttMessage msg = new MqttMessage(payload);
        msg.setQos(0);
        msg.setRetained(true);
        client.publish(topic, msg);
        return true;
    }

}
