package com.example.source;

import com.example.source.Tcp.TcpClient;
import com.example.source.Udp.UdpSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSyncSystemSource  implements UdpSocket.UdpSocketCallback , TcpClient.TcpClientCallBack {

    private static final int UDP_LISTEN_PORT =5556;
    private static final int TARGET_LISTEN_PORT =5554;
    private static final String TARGET_IP_ADDRESS ="192.168.175.206";
    private UdpSocket udpSocket;
    private TcpClient tcpClient;
    private ExecutorService exec = Executors.newCachedThreadPool();
    private Timer timer;
    private static final String FOLDER_PATH = "/storage/emulated/0/Workout";

    public FileSyncSystemSource ()
    {
        udpSocket =new UdpSocket(UDP_LISTEN_PORT,TARGET_IP_ADDRESS,TARGET_LISTEN_PORT,this);
        tcpClient=new TcpClient(TARGET_IP_ADDRESS,5555,this);
        exec.execute(tcpClient);
        exec.execute(udpSocket);
    }

    @Override
    public void getUdpSocketMessage(String message) {
        System.out.println( "Get message");
        String [] msgSplit =message .split(",");
        if (msgSplit[0].equals("Check"))
        {
            System.out.println( "Get Check");
            List <String> targetFileList=new ArrayList<String>();
            for (int i =1; i<msgSplit.length ;i++)
            {
                targetFileList.add(msgSplit[i]);
            }
            List<String> diffFile = getFolderDiff(targetFileList);

            for (int i =0; i<diffFile.size();i++)
            {

                System.out.println(diffFile.get(i));
            }

            if (diffFile.size()!= 0) {
                try {
                    byte [] fileAllByte =fullyReadFileToBytes(new File("/storage/emulated/0/Workout/"+diffFile.get(0)));
                    udpSocket.sendMsg("file,"+diffFile.get(0)+","+fileAllByte.length);
                    tcpClient.sendFile(fileAllByte);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    List<String> getFolderDiff (List<String> targetFileList)
    {
        File file =new File(FOLDER_PATH);
        String [] fileNames =file.list();
        List<String>diffFileList =new ArrayList<String>();
        for (int i =0;i<fileNames.length ;i++)
        {
            boolean isRepeat =false;
            for (int j=0;j<targetFileList.size() ;j++)
            {
                if (fileNames[i].equals(targetFileList.get(j)))
                {
                    isRepeat =true;
                    break;
                }
            }
            if (!isRepeat) {
                diffFileList.add(fileNames[i]);
            }
        }
        return diffFileList;
    }

    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);;
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }

    @Override
    public void socketIsCloseEvent() {

        tcpClient=new TcpClient(TARGET_IP_ADDRESS,5555,this);
        exec.execute(tcpClient);
    }




    @Override
    public void getMsg(String message) {

    }
}
