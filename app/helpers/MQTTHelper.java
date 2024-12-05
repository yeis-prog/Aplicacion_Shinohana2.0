package aja.example.aplicacion_shinohana20.helpers;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTHelper {
    private static final String TAG = "MQTTHelper";
    private MqttAndroidClient mqttClient;
    private static MQTTHelper instance;
    private final String serverUri = "tcp://broker.hivemq.com:1883";
    private final String clientId = "AndroidClient" + System.currentTimeMillis();
    private MqttCallback mqttCallback;

    private MQTTHelper() {}

    public static synchronized MQTTHelper getInstance() {
        if (instance == null) {
            instance = new MQTTHelper();
        }
        return instance;
    }

    public void init(Context context, MqttCallback callback) {
        this.mqttCallback = callback;
        mqttClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttClient.setCallback(mqttCallback);
        connect();
    }

    private void connect() {
        try {
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);

            mqttClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Conexión exitosa");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Error de conexión: " + exception.getMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "Error al conectar: " + e.getMessage());
        }
    }

    public void suscribirse(String topic) {
        try {
            mqttClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Suscripción exitosa a: " + topic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Error en suscripción: " + exception.getMessage());
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribirse: " + e.getMessage());
        }
    }

    public void publicar(String topic, String mensaje) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(mensaje.getBytes());
            mqttClient.publish(topic, message);
        } catch (MqttException e) {
            Log.e(TAG, "Error al publicar: " + e.getMessage());
        }
    }

    public void desconectar() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            Log.e(TAG, "Error al desconectar: " + e.getMessage());
        }
    }

    public boolean estaConectado() {
        return mqttClient != null && mqttClient.isConnected();
    }
} 