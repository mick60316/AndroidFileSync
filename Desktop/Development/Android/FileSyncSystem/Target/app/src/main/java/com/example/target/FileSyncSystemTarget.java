package com.example.target;

import android.os.Environment;
import android.util.Log;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class FileSyncSystemTarget implements MikeUdpSocket.UdpServerCallBack, TCPServer.TcpServerCallback {

    private  static  final String TAG ="FileSyncSystemTarget";
    private static final int UDP_LISTEN_PORT =5554;
    private static final int SOURCE_LISTEN_PORT =5556;
    private static final String SOURCE_IP_ADDRESS ="192.168.195.122";
    private static final String FOLDER_PATH = "/storage/emulated/0/Workout";
    private static final String FFMPEG_VIDEO_PATH ="/storage/emulated/0/Workout_FFmpeg";

    private int fileSize = 438741;
    private int currentFileIndex =0;
    private byte[] fileBuffer = new byte[fileSize];
    private String fileName ="testvideo.mp4";

    private MikeUdpSocket mikeUdpSocket;
    private TCPServer tcpServer;
    private Timer syncTimer;
    ExecutorService exec = Executors.newCachedThreadPool();
    public FileSyncSystemTarget()
    {
        mikeUdpSocket =new MikeUdpSocket(UDP_LISTEN_PORT, SOURCE_IP_ADDRESS, SOURCE_LISTEN_PORT,this);
        tcpServer =new TCPServer(5555,this);
        exec.execute(mikeUdpSocket);
        exec.execute(tcpServer);

    }
    public void syncFile()
    {
        mikeUdpSocket.sendFileListToRemote(getFileList());
    }

    private String getFileList ()
    {
        String s ="";
        File file = new File(FOLDER_PATH);

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
            Log.i(TAG,"Udp get "+msg);
            fileName =msgSplit[1];
            fileSize =Integer.valueOf(msgSplit[2]);
            fileBuffer =new byte[fileSize];
        }
    }

    @Override
    public void getFileBuffer(byte[] buffer, int length) {
        System.arraycopy(buffer,0,fileBuffer,currentFileIndex,length);
        currentFileIndex+=length;
        Log.i(TAG, "currentFileIndex "+ currentFileIndex +" "+ fileSize);


        if (currentFileIndex ==fileSize)
        {
            currentFileIndex =0;
            try {
                savaFileToSD("Workout/"+fileName,fileBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void savaFileToSD(String filename, byte[] filecontent) throws Exception {

        Log.i(TAG,String.format("Save file %s file size %d",filename,filecontent.length));

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            FileOutputStream output = new FileOutputStream(filename);
            output.write(filecontent);
            output.close();
            Timer timer=new Timer();
            final  String newFilename =filename;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    convertFFmpegConverVideo();
                }
            },5000);


        }
    }

    public void startSyncTimer (long repeatRate)
    {
        syncTimer =new Timer();
        syncTimer.schedule(syncTimerTask,0, repeatRate);
    }


    TimerTask syncTimerTask =new TimerTask() {
        @Override
        public void run() {
            syncFile();
        }
    };

    public void closeSocket ()
    {
        tcpServer.setIsListen(false);

    }

    void convertFFmpegConverVideo ()
    {

        String inputFile =FOLDER_PATH+"/"+fileName;
        String outputFile=FFMPEG_VIDEO_PATH +"/"+fileName;
        int rc = FFmpeg.execute(String .format("-i %s -vcodec libx264 -s 350x450 %s",inputFile,outputFile));

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }


    }








}
