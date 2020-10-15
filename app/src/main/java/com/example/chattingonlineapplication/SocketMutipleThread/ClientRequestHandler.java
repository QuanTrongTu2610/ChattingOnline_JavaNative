package com.example.chattingonlineapplication.SocketMutipleThread;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRequestHandler extends Thread {

    private Socket socket;

    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        super.run();
        String text;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                text = br.readLine();
                Log.i("Message", text);
                //update Message to List
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}

