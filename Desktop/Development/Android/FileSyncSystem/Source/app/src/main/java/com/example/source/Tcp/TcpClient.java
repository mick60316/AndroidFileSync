package com.example.source.Tcp;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class TcpClient  implements  Runnable{
    private static final String TAG ="Tcp Client";
    private String serverIp ="192.168.171.177";
    private int serverPort = 5555;
    private PrintWriter pw;
    private InputStream is;
    private DataInputStream dis;
    private boolean isRun =true;
    private Socket socket= null;
    private String rcvMsg;
    private int rcvMsgLen;
    private TcpClientCallBack tcpClientCallBack;
    private  OutputStream os;



    byte [] buffer = new byte[4096];


    public TcpClient (String serverIp ,int serverPort,TcpClientCallBack tcpClientCallBack)
    {
        this.serverIp =serverIp;
        this.serverPort =serverPort;
        this.tcpClientCallBack=tcpClientCallBack;
        printLog("Init");
    }

    @Override
    public void run() {
        isRun =false;
        try {
            printLog("Socket Init");
            socket = new Socket(serverIp, serverPort);
            socket.setSoTimeout(10000);
            // pw=new PrintWriter(socket.getOutputStream(),true);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            isRun =true;
            printLog("Connet successful");
        } catch (IOException e) {
            e.printStackTrace();

        }

        while (isRun) {
            try {
                if (dis != null) {

                    rcvMsgLen = dis.read(buffer);

                    rcvMsg = new String(buffer, 0, rcvMsgLen, "utf-8");
                    Log.i(TAG, "get message " + rcvMsg);
                    tcpClientCallBack.getMsg(rcvMsg);
                }
            } catch (IOException e) {

            } catch (StringIndexOutOfBoundsException e) {
                break;
            }
        }
        try {
            printLog("Close Socket");
            if(is!=null) is.close();
            if(dis!=null) dis.close();
            if(socket!=null) socket.close();
            try {
                Thread.sleep(3000);
                tcpClientCallBack.socketIsCloseEvent();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {

        }


    }

    public void send (String sendMessage ) throws IOException {
        os.write(sendMessage.getBytes());
        os.flush();
    }
    public interface TcpClientCallBack {
        void getMsg (String message);
        void socketIsCloseEvent ();
    }

    public void sendFile (byte [] fileBuffer ) throws IOException {
        printLog("Start send file");

        int packageSize =4096;
        int packageCount =fileBuffer.length /packageSize +1;
        byte  [] buf =new byte[packageSize];


        printLog(String.format("Start send file, Pagckage Count =%d Package Size =%d",packageCount,packageSize));

        int count =0;
        for (int i =0;i<packageCount-1;i++)
        {
            System.arraycopy(fileBuffer,packageSize *i,buf,0,packageSize);
            os.write(buf);
            os.flush();
            count +=buf.length;
            printLog (String.format("Package %d Finish ",i));

        }
        buf =new byte[fileBuffer.length -(packageCount-1)*packageSize];
        System.arraycopy(fileBuffer,(packageCount-1)*packageSize,buf,0,fileBuffer.length -(packageCount-1)*packageSize);

        printLog("Send file Finish" );
        os.write(buf);
        os.flush();
    }
    private void printLog (String msg)
    {
        Log.i(TAG,msg);
    }


}
