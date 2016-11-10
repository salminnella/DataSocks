package com.example.salminnella.ansockets;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class ClientSocketActivity extends AppCompatActivity {
    private static final String TAG_CLIENT_ACTIVITY = "ClientSocketActivity";
    public static final int SERVERPORT = 6667;
    public static String SERVERIP = "127.0.0.1";

    private FloatingActionButton clientFab;
    private TextView clientTextView;
    private TextView clientServerStatusTextView;
//    private TextView clientIpTextView;
    private Handler handler = new Handler();
    private Socket clientSocket;
    private String clientIP;

    private EditText serverIp;
    private Button connectPhones;
//    private String serverIpAddress = "172.17.58.30";
    private String serverIpAddress = "127.0.0.1";
    private boolean connected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_socket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        initFab();

        clientIP = getLocalIpAddress(true);

//        Thread streamThread = new Thread(new ClientThread());
//        streamThread.start();

    }

    private void initViews() {
        clientTextView = (TextView) findViewById(R.id.client_hello_text_view);
        clientServerStatusTextView = (TextView) findViewById(R.id.client_server_status_text_view);
//        clientIpTextView = (TextView) findViewById(R.id.client_this_ip_text_view);
        clientFab = (FloatingActionButton) findViewById(R.id.fab);

        serverIp = (EditText) findViewById(R.id.server_ip);
        connectPhones = (Button) findViewById(R.id.connect_phones);
        connectPhones.setOnClickListener(connectListener);

    }

    private View.OnClickListener connectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected) {
                serverIpAddress = serverIp.getText().toString();
                if (!serverIpAddress.equals("")) {
                    Thread cThread = new Thread(new ReceiveServerMessage());
                    cThread.start();
                }
            }
        }
    };

    private void initFab() {
        clientFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public class ReceiveServerMessage implements Runnable {

        @Override
        public void run() {
            try {
                Socket socket = new Socket("192.168.0.14", 8890);
                Log.d("ClientActivity", "C: Connecting...");
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("hey server!");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String response = in.readLine();
                Log.d(TAG_CLIENT_ACTIVITY, "run: " + response);
                    while (response != null) {
                        Log.d("ServerActivity", response);
                        final String finalResponse = response;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                clientTextView.setText(finalResponse);
                                Log.d(TAG_CLIENT_ACTIVITY, "run: " + finalResponse);
                                Toast.makeText(ClientSocketActivity.this, "" + finalResponse, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                socket.close();
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

    public class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                clientSocket = new Socket(SERVERIP, MainActivity.SERVERPORT);
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

    public class ConnectPythonThread implements Runnable {

        public void run() {
            try {
//                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
//                Log.d("ClientActivity", "C: Connecting..." + serverAddr.toString());

                DataOutputStream dataOutputStream = null;
                DataInputStream dataInputStream = null;
                String response = "";
                clientSocket = new Socket("192.168.0.14", 8888);
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                connected = true;
                int count = 1;
                while (count == 1) {
//                    try {
//                        if (count == 1) {
//                            Log.d("ClientActivity", "C: Sending command.");
//                            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
//                            dataInputStream = new DataInputStream(clientSocket.getInputStream());
//                            dataOutputStream.writeUTF("Hey Server!");
//                            Log.d("ClientActivity", "C: Sent.");
//                            response = dataInputStream.readUTF();
//                            Log.d(TAG_CLIENT_ACTIVITY, "response = " + response);
//                        }
//                        count += 1;
//                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//                        Log.d(TAG_CLIENT_ACTIVITY, "run: response = " + in.readLine());
//                        String line = null;
//                        while ((line = in.readLine()) != null) {
//                            Log.d("ServerActivity", line);
//                            final String finalLine = line;
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    clientTextView.setText(finalLine);
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                        Log.e("ClientActivity", "S: Error", e);
//                    }

                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            Log.d("ServerActivity", line);
                            final String finalLine = line;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    clientTextView.setText(finalLine);
                                }
                            });
                        }
                        count += 1;
                        break;
                    } catch (Exception e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                clientServerStatusTextView.setText("Oops. Connection interrupted.");
                            }
                        });
                        e.printStackTrace();
                    }
                }
//                clientSocket.close();
//                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
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
        if (clientSocket != null) {
            try {
                clientSocket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
