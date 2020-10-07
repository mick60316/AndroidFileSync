package com.example.target;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileSyncSystemTarget implements MikeUdpSocket.UdpServerCallBack, TCPServer.TcpServerCallback {


    private static final int UDP_LISTEN_PORT =5554;
    private static final int SOURCE_LISTEN_PORT =5556;
    private static final String SOURCE_IP_ADDRESS ="192.168.171.206";
    private static final String FOLDER_PATH = "/storage/emulated/0/Workout";
    private int fileSize = 438741;
    private int currentFileIndex =0;
    private byte[] fileBuffer = new byte[fileSize];
    private String fileName ="testvideo.mp4";

    private MikeUdpSocket mikeUdpSocket;
    private TCPServer tcpServer;
    private Timer timer;





    ExecutorService exec = Executors.newCachedThreadPool();
    public FileSyncSystemTarget()
    {
        mikeUdpSocket =new MikeUdpSocket(UDP_LISTEN_PORT, SOURCE_IP_ADDRESS, SOURCE_LISTEN_PORT,this);
        tcpServer =new TCPServer(5555,this);
        exec.execute(mikeUdpSocket);
        exec.execute(tcpServer);

    }
    public void syncFolder()
    {
        mikeUdpSocket.sendFileListToRemote(getFileList());
    }

    private String getFileList ()
    {
        String s ="";
        File file =new File(FOLDER_PATH);
        String []filesName = file.list();
        for (int i =0;i<filesName.length ;i++)
        {
            s+=filesName [i] +",";
        }
        return  s;
    }
    @Override
    public void getUdpMessage(String msg) {
        String [] msgSplit =msg.split(",");

        if (msgSplit[0].equals("file"))
        {
            System.out.println("Udp get "+msg);
            fileName =msgSplit[1];
            fileSize =Integer.valueOf(msgSplit[2]);
            fileBuffer =new byte[fileSize];

        }
    }

    @Override
    public void getFileBuffer(byte[] buffer, int length) {
        System.arraycopy(buffer,0,fileBuffer,currentFileIndex,length);
        currentFileIndex+=length;
        if (currentFileIndex ==fileSize)
        {
            currentFileIndex =0;
            try {
                savaFileToSD("/Workout/"+fileName,fileBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void savaFileToSD(String filename, byte[] filecontent) throws Exception {

        System.out.println("Save file "+ filename +" file size "+ filecontent.length);

        //如果手機已插入sd卡,且app具有讀寫sd卡的許可權
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;

            //這裡就不要用openFileOutput了,那個是往手機記憶體中寫資料的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(filecontent);
            //將String字串以位元組流的形式寫入到輸出流中
            output.close();
            //關閉輸出流
        }
    }

    public void startSyncTimer (long repeatRate)
    {
        timer=new Timer();
        timer.schedule(timerTask,0, repeatRate);
    }


    TimerTask timerTask =new TimerTask() {
        @Override
        public void run() {
            syncFolder();
        }
    };




}
