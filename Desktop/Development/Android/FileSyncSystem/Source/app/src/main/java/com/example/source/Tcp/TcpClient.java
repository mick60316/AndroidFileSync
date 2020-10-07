package com.example.source.Tcp;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpClient  implements  Runnable{

    private String TAG = "TcpClient";
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


    public TcpClient (String serverIp ,int serverPort)
    {
            this.serverIp =serverIp;
            this.serverPort =serverPort;

    }


    public void socketInit ()
    {

    }

    @Override
    public void run() {
        try {

            socket=new Socket( serverIp,serverPort);
            socket.setSoTimeout(10000);
            // pw=new PrintWriter(socket.getOutputStream(),true);
            os = socket.getOutputStream();
            is =socket.getInputStream();
            dis=new DataInputStream(is);
        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

        while (isRun)
        {
            try{
                if (dis!=null) {
                    rcvMsgLen = dis.read(buffer);

                    rcvMsg = new String(buffer, 0, rcvMsgLen, "utf-8");
                    Log.i(TAG, "get message " + rcvMsg);
                    tcpClientCallBack.getMsg(rcvMsg);
                }
            }catch (IOException e)
            {

            }
        }
        try {
            pw.close();
            is.close();
            dis.close();
            socket.close();

        }catch (IOException e)
        {

        }

    }

    public void send (String sendMessage ) throws IOException {
        os.write(sendMessage.getBytes());
        os.flush();
        //pw.println(sendMessage);

        //pw.flush();
    }
    public interface TcpClientCallBack {
        void getMsg (String message);
    }

    public void sendFile (byte [] fileBuffer ) throws IOException {
        int packageSize =4096;
        int packageCount =fileBuffer.length /packageSize +1;
        byte  [] buf =new byte[packageSize];
        int count =0;
        for (int i =0;i<packageCount-1;i++)
        {
            System.arraycopy(fileBuffer,packageSize *i,buf,0,packageSize);
            os.write(buf);
            os.flush();
            count +=buf.length;
            System.out.println("Package "+ i +" Finish" +buf.length+" " +count);
        }
        buf =new byte[fileBuffer.length -(packageCount-1)*packageSize];
        System.arraycopy(fileBuffer,(packageCount-1)*packageSize,buf,0,fileBuffer.length -(packageCount-1)*packageSize);
        System.out.println("Package   Finish" +buf.length);
        os.write(buf);
        os.flush();
    }


}
