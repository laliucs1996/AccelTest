package com.example.larry.acceltest;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class DisplayActivity extends Activity {

    SensorManager flipDetection;
    Sensor sensor;
    Boolean detection;
    TextView position;
    TextView activity;
    View Background;
    int Step;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Background = (View)findViewById(R.id.view);
        position = (TextView)findViewById(R.id.Position);
        activity  = (TextView)findViewById(R.id.Activity);
        Step = 0;
        flipDetection = (SensorManager)getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = flipDetection.getSensorList(Sensor.TYPE_ACCELEROMETER);

        if(sensorList.size() > 0)
        {
            sensor = sensorList.get(0);
            detection = true;

        }
        else
        {
            activity.setText("No Sensor Capable of detecting position or activities");
            detection = false;
        }
    }

    protected void onResume()
    {
        super.onResume();
        if(detection)
            flipDetection.registerListener(listen, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(detection)
            flipDetection.unregisterListener(listen);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(detection)
            flipDetection.unregisterListener(listen);
    }

    SensorEventListener listen =  new SensorEventListener()
    {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent)
        {

            float flippage = sensorEvent.values[2];

            if(flippage > 0)
                position.setText("The phone is facing upward!");
            else
                position.setText("The phone is facing downward");
            if (Math.abs(sensorEvent.values[0]) > Math.abs(sensorEvent.values[1]) && Math.abs(flippage) < 9)
            {
                activity.setText("Phone is horizontal.");
                Background.setBackgroundColor(Color.GREEN);
            }
            else if (Math.abs(sensorEvent.values[0]) < Math.abs(sensorEvent.values[1]) && Math.abs(flippage) < 9)
            {
                activity.setText("Phone is vertical.");
                Background.setBackgroundColor(Color.LTGRAY);
            }
            if(flippage > 9.8 || flippage < -9.8)
            {
                activity.setText("Phone is in a flat position");
                Background.setBackgroundColor(Color.CYAN);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i)
        {
            //Do Nothing
        }
    };
}
