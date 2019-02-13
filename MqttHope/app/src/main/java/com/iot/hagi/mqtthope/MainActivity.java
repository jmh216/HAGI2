package com.iot.hagi.mqtthope;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import helpers.MqttHelper;
import helpers.ChartHelper;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.Math.*;

public class MainActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    ChartHelper mChart;
    LineChart chart;

    TextView dataReceived;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataReceived = (TextView) findViewById(R.id.dataReceived);
        chart = (LineChart) findViewById(R.id.chart);
        mChart = new ChartHelper(chart);

        startMqtt();
    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

//                String JSON_STRING  =  mqttMessage.toString();
//
//                Log.w("received", JSON_STRING);
//
//                dataReceived.setText(JSON_STRING);
//
//                mChart.addEntry(Float.valueOf(JSON_STRING));

                String jsonString = mqttMessage.toString();

                JSONObject objson = new JSONObject(jsonString);
                String dist = objson.getString("distance");

                Log.w("JSON", jsonString);
//                Log.w("JSON", dist);

                float fdist = Math.min(Float.valueOf(dist), 1300.0f);

                if( fdist == 1300f)
                {
//                    String fdist = " out of range ";
                    dataReceived.setText("Distance out of range.");
                }
                else
                {
                    dataReceived.setText("distance : " + fdist);

                }
                Log.w("JSON", Float.toString(fdist));
                mChart.addEntry(fdist);



            }

            @Override

            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}





