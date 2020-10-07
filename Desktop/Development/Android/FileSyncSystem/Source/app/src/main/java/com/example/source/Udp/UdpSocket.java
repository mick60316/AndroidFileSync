package com.example.source.Udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpSocket implements Runnable{
    private int listenPort;
    private int targetPort;
    private String targetIp;


    DatagramSocket ds = null;
    private UdpSocketCallback udpSocketCallback;


    public UdpSocket(int listenPort,String targetIp,int targetPort,UdpSocketCallback udpSocketCallback)
    {
        this.listenPort =listenPort;
        this.udpSocketCallback =udpSocketCallback;
        this.targetIp=targetIp;
        this.targetPort=targetPort;
    }
    @Override
    public void run() {
        try {
            String data;
            byte[] recevieData =new byte[1024];
            DatagramPacket dp =new DatagramPacket(recevieData,recevieData.length);
            ds=new DatagramSocket(listenPort);
            while (true)
            {
                Thread.sleep(100);
                ds.receive(dp);
                data =new String(recevieData,0,dp.getLength());
                System.out.println(data);
                udpSocketCallback.getUdpSocketMessage(data);

            }
        }catch (Throwable t)
        {


        }
    }
    public void sendMsg (String message)
    {
        try {
            InetAddress serverAddr = InetAddress.getByName(targetIp);
            DatagramPacket dp;
            dp=new DatagramPacket(message.getBytes(),message.length(),serverAddr,targetPort);
            try{
                ds.send(dp);
            }catch (IOException e)
            {
                System.out.println("fail to send");

            }
        }
        catch (UnknownHostException e)
        {

        }

    }
    public interface UdpSocketCallback
    {
        void getUdpSocketMessage (String message);


    }
}
