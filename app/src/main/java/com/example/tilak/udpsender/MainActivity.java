package com.example.tilak.udpsender;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;



public class MainActivity extends AppCompatActivity implements SensorEventListener {

    EditText ip,port;
    Button connect,stop;
    Sensor sensor;
    SensorManager SM;
    TextView tf1;
    DatagramSocket socket;
    DatagramPacket packet;
    String ipaddr;
    int p;
    static float value0,value1,value2;
    Thread mthread;
    float aPitch = 0; //angle from Pitch
    float aRoll = 0; //angle from Roll
    float aYaw = 0;// angle from Yaw

    double dt= (float) 0.01;


    public void send(final String ip, final int port, final String data){

        mthread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    byte[] msg=data.getBytes();
                    InetAddress inetAddress=InetAddress.getByName(ip);
                    socket=new DatagramSocket(port);
                    packet=new DatagramPacket(msg,msg.length,inetAddress,port);
                    socket.send(packet);
                    socket.close();


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        mthread.start();
        }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip=findViewById(R.id.edittext_ip);
        port=findViewById(R.id.edittext_port);
        connect=findViewById(R.id.button_connect);
        stop=findViewById(R.id.button_stop);
        tf1=findViewById(R.id.tf1);



        SM= (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor=SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.registerListener(MainActivity.this,sensor,SensorManager.SENSOR_DELAY_FASTEST);


                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {

                         ipaddr=ip.getText().toString();
                        p=Integer.parseInt(port.getText().toString());



                    }
                });

                thread.start();




            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SM.unregisterListener(MainActivity.this);
                tf1.setText("");
                socket.close();
                aPitch=0;
                aRoll=0;
                aYaw=0;

            }
        });


    }

    @Override
    public void onSensorChanged(final SensorEvent event) {


        //What we have
        float dPitch = event.values[2];// angular rate from Pitch
        float dRoll = event.values[1];// angular rate from Roll
        float dYaw = event.values[0]; //angular rate from Yaw

//What we want


//Calculating the angles:
        aPitch += dPitch*dt; // or aPitch = aPitch + dPitch*dt;
        aRoll += dRoll*dt; // or aRoll = aRoll + dRoll*dt;
        aYaw += dYaw*dt; // or aYaw = aYaw + dYaw*dt;


                String ori="yaw:"+Math.round(aYaw*(-30))+"    roll:"+Math.round(aPitch*(-30))+"  pitch: "+Math.round(aRoll*(-30));
                tf1.setText(ori);
                send(ipaddr,p,ori);


                }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
