package com.example.target;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    TCPServer tcpServer=null;
    //ExecutorService exec = Executors.newCachedThreadPool();
    Button bt;
    FileSyncSystemTarget fileSyncSystemTarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=findViewById(R.id.sync_button);
//        tcpServer=new TCPServer(5555, new TCPServer.TcpServerCallback() {
//            @Override
//            public void getMsg(String msg) {
//
//            }
//        });
//        exec.execute(tcpServer);
        fileSyncSystemTarget=new FileSyncSystemTarget();
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                fileSyncSystemTarget.startSyncTimer(10000);
            }
        });
    }
}