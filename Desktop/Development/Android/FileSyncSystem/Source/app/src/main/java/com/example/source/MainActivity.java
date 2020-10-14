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

    FileSyncSystemSource fileSyncSystemSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File f =new File("/storage/emulated/0/Workout");
        String []  index =f.list();
        for (int i =0;i<index.length ;i++)
        {
            System.out.println(index[i]);

        }
        fileSyncSystemSource =new FileSyncSystemSource();
    }

}