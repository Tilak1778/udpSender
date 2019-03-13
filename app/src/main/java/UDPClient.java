import android.annotation.SuppressLint;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class UDPClient {
    private InetAddress inetAddress=null;
    private String msg="hii";
    AsyncTask asyncTask;
    public String message;

    @SuppressLint({"NewApi", "StaticFieldLeak"})
    public void Send(){

        asyncTask=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                DatagramSocket ds=null;

                try {
                    inetAddress=InetAddress.getByName("192.168.43.111");
                    ds=new DatagramSocket(5000);
                    DatagramPacket dp=new DatagramPacket(message.getBytes(),message.getBytes().length,inetAddress,5000);
                    ds.setBroadcast(true);
                    ds.send(dp);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

        };

    }

}
