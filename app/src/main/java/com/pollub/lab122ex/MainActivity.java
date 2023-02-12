package com.pollub.lab122ex;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import static java.lang.Math.round;
import static java.lang.Math.toDegrees;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        SensorEventListener {
    TextView tv1, tv2, tv3, tv4,tv5;
    SensorManager sensorManager;
    Sensor accelerometer;
    float [] values;
    float x, y, z,rotate;
    long lastUpdate;
    int count =0;
    int count_ag=0;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.textView);
        tv2 = findViewById(R.id.textView2);
        tv3 = findViewById(R.id.textView3);
        tv4 = findViewById(R.id.textView4);
        tv5 = findViewById(R.id.textView5);
        lastUpdate = System.currentTimeMillis();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            values = event.values;
            x = values[0];
            y = values[1];
            z = values[2];
            tv1.setText("X: " + String.valueOf(x)+ " m/s2");
            tv2.setText("Y: " + String.valueOf(y) + " m/s2");
            tv3.setText("Z: " + String.valueOf(z) + " m/s2");
            rotate=(x * x + y * y + z * z)*100;
            float acceleration = (x * x + y * y + z * z) /
                    (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            if (acceleration < 1.5) {
                tv4.setText("Low Acceleration");
                tv4.setBackgroundColor(Color.GREEN);
            } else if ((acceleration >= 1.5) && (acceleration < 3)) {
                tv4.setText("Medium Acceleration");
                tv4.setBackgroundColor(Color.YELLOW);
            } else {
                tv4.setText("High Acceleration");
                tv4.setBackgroundColor(Color.RED);
            }

            long actualTime = event.timestamp;
            if (x>5||y>5||z>5){

                count++;

                // Accidental movement
                return;
            }

            if (count>20){//sensitive setting
                count_ag++;
            }
            count=0;

            tv5.setText( "Rotate Count: "+String.valueOf(count_ag));
            // Counting shaking

        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
        accelerometer =
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }
}
