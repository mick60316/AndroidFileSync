package com.example.target;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MikeUdpSocket implements Runnable{
    private int listenPort;
    private int sourcePort;
    private String sourceIpAddress;
    DatagramSocket ds = null;

    private UdpServerCallBack udpServerCallBack;

    public MikeUdpSocket(int listenPort, String sourceIpAddress, int remotePort, UdpServerCallBack udpServerCallBack)
    {
        this.listenPort = listenPort;
        this.sourcePort =remotePort;
        this.sourceIpAddress = sourceIpAddress;
        this.udpServerCallBack=udpServerCallBack;






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

                udpServerCallBack.getUdpMessage(data);

                System.out.println(data);
            }

        }catch (Throwable t)
        {


        }
    }

    public void sendFileListToRemote (String fileList)
    {
        sendMsg("Check,"+fileList);
    }

    private void sendMsg (String message)
    {
        try {
            InetAddress serverAddr = InetAddress.getByName(sourceIpAddress);
            DatagramPacket dp;
            dp=new DatagramPacket(message.getBytes(),message.length(),serverAddr, sourcePort);
            try{
                ds.send(dp);
            }catch (IOException e)
            {
                System.out.println("fail to send");

            }
        }
        catch (UnknownHostException e)
        {
            System.out.println("Error");
        }

    }

    public interface UdpServerCallBack {
        void getUdpMessage(String msg);
    }


}
