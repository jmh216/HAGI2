package com.iot.hagi.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;

import java.io.*;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set connection parameters
        String clientId = MqttClient.generateClientId();
        final MqttAndroidClient client =
//                new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com:1883",
                new MqttAndroidClient(MainActivity.this, "tcp://test.mosquitto.org:1883",
                        clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1);

        try {
//            make the connection
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("myTag", "onSuccess");
                    Toast.makeText(MainActivity.this,
                            "Connected", Toast.LENGTH_LONG).show();
//                    string mqrrRes =
                    TextView textviewlabel1 = (TextView)findViewById(R.id.textView);
                    textviewlabel1.setText("MQTT connected!");

                    String topic = "IC.embedded/HAGI/#";
                    int qos = 2;
                    try {
                        IMqttToken subToken = client.subscribe(topic, qos);

                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                // The message was published
                    TextView textviewlabel3 = (TextView)findViewById(R.id.textView3);
                    textviewlabel3.setText("Subscribed!");
//                    Toast.makeText(MainActivity.this,
//                            "Subscribed!", Toast.LENGTH_LONG).show();
                            client.setCallback(new MqttCallback() {
                                @Override
                                public void connectionLost(Throwable cause) {

                                }

                                @Override
                                public void messageArrived(String topic, MqttMessage message) throws Exception {
                                    Log.d("myTag", new String(message.getPayload()));
                                    TextView textviewlabel3 = (TextView)findViewById(R.id.textView3);
                                    textviewlabel3.setText(new String(message.getPayload()));
                                }

                                @Override
                                public void deliveryComplete(IMqttDeliveryToken token) {

                                }
                            });

                            }
                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
//                    TextView textviewlabel3 = (TextView)findViewById(R.id.textView3);
//                    textviewlabel3.setText("NOT Subscribed!");
                                Toast.makeText(MainActivity.this,
                                        "NOT Subscribed", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (MqttException e) {
//            TextView textviewlabel3 = (TextView)findViewById(R.id.textView3);
//            textviewlabel3.setText("Subscribed! failed!");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
//                    Log.d("myTag", "onFailure");
                    Toast.makeText(MainActivity.this,
                            "Connect fail", Toast.LENGTH_LONG).show();

                    TextView textviewlabel1 = (TextView)findViewById(R.id.textView);
                    textviewlabel1.setText("MQTT failed!");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
}
