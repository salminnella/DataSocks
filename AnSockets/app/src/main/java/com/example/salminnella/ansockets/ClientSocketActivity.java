package com.example.salminnella.ansockets;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

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
    private String clientIP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_socket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        initFab();

        clientIP = getLocalIpAddress(true);

        Thread streamThread = new Thread(new ClientThread());
        streamThread.start();

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
                clientSocket = new Socket(SERVERIP, SERVERPORT);
                clientServerStatusTextView.setText("ip is: " + clientIP);
                InputStream is = clientSocket.getInputStream();

                byte[] buffer = new byte[25];
                int read = is.read(buffer);
                while(read != -1){
                    clientTextView.setText(read);
                    read = is.read(buffer);
                }

                is.close();
                clientSocket.close();

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
    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
