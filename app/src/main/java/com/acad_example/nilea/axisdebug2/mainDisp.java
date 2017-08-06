package com.acad_example.nilea.axisdebug2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;

public class mainDisp extends Activity implements SensorEventListener {

    float xAxis=0,yAxis=0,zAxis=0;
    int fileCtr= 1 ;
    Switch logControl;
    TextView xAxisDat,yAxisDat,zAxisDat,main_disp,loggingStat,info;
    SensorManager sm;
    Sensor S;
    boolean logPer = false,filestat,newStat = true,isOpen = false;
    File file;
    FileOutputStream fOS;
    String basePath;
    Intent intent;
    ArrayList fileNames = new ArrayList();
    public static final String FILE_NAMES = "com.acad_example.nilea.axisdebug2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_disp);

        //THis is the linking of components

        xAxis = 0;
        yAxis = 0;
        zAxis = 0;
        logControl = (Switch) findViewById(R.id.enableLog);
        xAxisDat = (TextView) findViewById(R.id.xAxisData);
        yAxisDat = (TextView) findViewById(R.id.yAxisData);
        zAxisDat = (TextView) findViewById(R.id.zAxisData);
        main_disp = (EditText) findViewById(R.id.simTitle);
        loggingStat = (TextView) findViewById(R.id.toggleLog);
        info = (TextView) findViewById(R.id.mainDisplay);
        //Doing SensorManager stuff

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            S = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sm.registerListener(this, S, SensorManager.SENSOR_DELAY_NORMAL);
        }

        logControl.setChecked(false);
        logControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    loggingStat.setText("Data is being Logged");
                    logPer = true;
                    newStat = true;
                }
                else {
                    loggingStat.setText("Data is not being Logged");
                    logPer = false;
                    //newStat = true;
                    fileCtr++;
                }
            }
        });


        //        file = new File(getCacheDir(),"DebugData.txt");
        basePath = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(basePath)){
            filestat =true;
        }
        /*
        if(filestat){
            info.setText("File can be set up in external Directory");
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/Acc_sensorData" + String.valueOf(fileCtr)+".txt");
            info.setText(file.toString());
            fileNames.add(file.toString());
            try {
                fOS = new FileOutputStream(file,true); // this constructor appends the data
                isOpen = true;
                //fOS.flush(); fOS.close();
            } catch (IOException ioe) {
                loggingStat.setText("File Not Created");
                ioe.printStackTrace();
            }
            filestat = false;
            newStat = false;
        //    if(!file.mkdirs()) main_disp.setText("Directory Not created");
            */
        }

        /*try {
            fOS = new FileOutputStream(file);
            fOS.write(("Does it works ??? \n").getBytes());
            fOS.flush(); fOS.close();
        } catch (IOException ioe) {
            loggingStat.setText("File Not Created");

            ioe.printStackTrace();
        }*/


    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        sm.registerListener(this, S, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    public void onSensorChanged(SensorEvent arg0){

        Sensor mySensor = arg0.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER ){
            xAxis = arg0.values[0];
            yAxis = arg0.values[1];
            zAxis = arg0.values[2];

            xAxisDat.setText(""+ xAxis);
            yAxisDat.setText(""+ yAxis);
            zAxisDat.setText(""+ zAxis);

            if(logPer){

                if(newStat) {
                    newStat = false;
                    basePath = Environment.getExternalStorageState();
                    if(Environment.MEDIA_MOUNTED.equals(basePath)){
                        filestat =true;
                    }
                    if(filestat){
                        info.setText("File can be set up in external Directory");
                        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "/Acc_sensorData" + String.valueOf(fileCtr)+".txt");
                        info.setText(file.toString());
                        fileNames.add(file.toString());
                        //    if(!file.mkdirs()) main_disp.setText("Directory Not created");
                        }

                }
                if(isOpen == false){
                    try {
                        fOS = new FileOutputStream(file,true); // this constructor appends the data
                        fOS.write((" " + xAxis + " " + yAxis + " " + zAxis + "\n").getBytes());
                        isOpen = true;
                        //fOS.flush(); fOS.close();
                    } catch (IOException ioe) {
                        loggingStat.setText("File Not Created");
                        ioe.printStackTrace();
                    }
                }
                else{
                    try {
                        fOS.write((" " + xAxis + " " + yAxis + " " + zAxis + "\n").getBytes());
                    } catch (IOException ioe) {
                        loggingStat.setText("File Not Created");
                        ioe.printStackTrace();
                    }

                }

            }
            else if(logPer==false & isOpen==true){
                try {
                    fOS.flush();fOS.close();
                    isOpen = false;
                } catch (IOException ioe) {
                    loggingStat.setText("File Not Created");
                    ioe.printStackTrace();

                }
            }


        }
    }

    public void start_analysis(View view){
        intent = new Intent(this,dataAnalysis.class);
        intent.putExtra(FILE_NAMES,fileNames);
        startActivity(intent);

    }

}

