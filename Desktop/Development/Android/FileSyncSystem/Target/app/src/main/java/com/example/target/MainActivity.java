package com.example.target;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bt;
    FileSyncSystemTarget fileSyncSystemTarget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=findViewById(R.id.sync_button);
        fileSyncSystemTarget=new FileSyncSystemTarget();
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                fileSyncSystemTarget.startSyncTimer(10000);
            }
        });
    }

    @Override
    protected void onStop() {
        System.out.println("OnStop");
        fileSyncSystemTarget.closeSocket();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //fileSyncSystemTarget.closeSocket();
        super.onDestroy();

    }
}