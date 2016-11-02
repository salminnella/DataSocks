package com.example.salminnella.ansockets;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.net.Socket;

public class ClientSocketActivity extends AppCompatActivity {
    private static final String TAG_CLIENT_ACTIVITY = "ClientSocketActivity";
    public static final int SERVERPORT = 9999;
    public static String SERVERIP = "127.0.0.1";

    private FloatingActionButton clientFab;
    private TextView clientTextView;
    private TextView clientServerStatusTextView;
//    private TextView clientIpTextView;
    private Handler handler = new Handler();
    private Socket clientSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_socket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        initFab();



    }

    private void initViews() {
        clientTextView = (TextView) findViewById(R.id.client_hello_text_view);
        clientServerStatusTextView = (TextView) findViewById(R.id.client_server_status_text_view);
//        clientIpTextView = (TextView) findViewById(R.id.client_this_ip_text_view);
        clientFab = (FloatingActionButton) findViewById(R.id.fab);

    }

    private void initFab() {
        clientFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class ClientThread implements Runnable {

        @Override
        public void run() {
            try {

            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        clientServerStatusTextView.setText("Error");
                    }
                });
                e.printStackTrace();
            }
        }
    }


}
