package com.example.target;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

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



            }
        });
    }

    @Override
    protected void onStop() {
        System.out.println("OnStop");
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