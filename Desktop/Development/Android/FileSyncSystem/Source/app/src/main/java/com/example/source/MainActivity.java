package com.example.source;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.example.source.Tcp.TcpClient;
import com.example.source.Udp.UdpSocket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    TcpClient tcpClient=null;
    UdpSocket udpClient=null;
    private Button bt;
    ExecutorService exec = Executors.newCachedThreadPool();
    private UdpSocket udpSocket;
    FileSyncSystemSource fileSyncSystemSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=findViewById(R.id.testButton);

        fileSyncSystemSource =new FileSyncSystemSource();

//        udpServer =new UdpServer(4321);
//        tcpClient=new TcpClient("192.168.187.122", 5555, new TcpClient.TcpClientCallBack() {
//            @Override
//            public void getMsg(String message) {
//
//            }
//        });
        //exec.execute(tcpClient);
//        exec.execute(udpServer);
        bt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {


//                String path = "/storage/emulated/0/Workout";
//                Log.d("Files", "Path: " + path);
//                File directory = new File(path);
//                File[] files = directory.listFiles();
//                Log.d("Files", "Size: "+ files.length);
//                for (int i = 0; i < files.length; i++)
//                {
//                    Log.d("Files", "FileName:" + files[i].getName());
//                }

//                try {
////                    byte bytes[] =fullyReadFileToBytes(new File("/storage/emulated/0/Workout/testvideo.mp4"));
////                    System.out.println(bytes.length);
////                    tcpClient.sendFile(bytes);
//                    //tcpClient.send("test");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }



//                File sdcard = Environment.getExternalStorageDirectory();
//                File file = new File(sdcard,"testvideo.mp4");
//                BufferedOutputStream bos = null;
//                try {
//                    bos = new BufferedOutputStream(new FileOutputStream("/storage/emulated/0/Workout/testvideo.mp4"));
//                    bos.write(index);
//                    bos.flush();
//                    bos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    System.out.println("fail");
//                }


//                exec.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        tcpClient.send("hello world");
//                        //udpServer.sendMsg("10.0.0.89",4321,"hello woooooorold");
//                    }
//                });


            }
        });

    }

    public String readFromSD(String filename) throws IOException {
        StringBuilder sb = new StringBuilder("");
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + "/" + filename;
            //開啟檔案輸入流
            FileInputStream input = new FileInputStream(filename);

            byte[] temp = new byte[1024];

            int len = 0;
            //讀取檔案內容:
            while ((len = input.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            //關閉輸入流
            input.close();
        }
        return sb.toString();
    }



}