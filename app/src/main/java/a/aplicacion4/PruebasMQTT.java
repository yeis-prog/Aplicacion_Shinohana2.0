package a.aplicacion4;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import aja.example.aplicacion_shinohana20.helpers.MQTTHelper;

public class PruebasMQTT extends AppCompatActivity {
    private MQTTHelper mqttHelper;
    private EditText editTextMessage;
    private TextView textViewStatus, textViewReceived;
    private Button buttonPublish;
    private static final String TOPIC = "shinohana/test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_mqtt);

        // Inicializar vistas
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonPublish = findViewById(R.id.buttonPublish);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewReceived = findViewById(R.id.textViewReceived);

        // Configurar MQTT
        setupMQTT();

        // Configurar botón de publicación
        buttonPublish.setOnClickListener(v -> publicarMensaje());
    }

    private void setupMQTT() {
        mqttHelper = MQTTHelper.getInstance();
        mqttHelper.init(this, new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                runOnUiThread(() -> 
                    textViewStatus.setText("Estado: Desconectado"));
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String msg = new String(message.getPayload());
                runOnUiThread(() -> 
                    textViewReceived.setText("Recibido: " + msg));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                runOnUiThread(() -> 
                    textViewStatus.setText("Estado: Mensaje enviado"));
            }
        });

        // Suscribirse al topic
        mqttHelper.suscribirse(TOPIC);
    }

    private void publicarMensaje() {
        String mensaje = editTextMessage.getText().toString().trim();
        if (!mensaje.isEmpty()) {
            mqttHelper.publicar(TOPIC, mensaje);
            editTextMessage.setText("");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttHelper != null) {
            mqttHelper.desconectar();
        }
    }
}