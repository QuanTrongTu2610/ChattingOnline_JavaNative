package com.example.chattingonlineapplication.SocketMutipleThread;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientRequestHandler implements Callable<String> {

    private Socket socket;

    public ClientRequestHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public String call() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String message;
                while ((message = br.readLine()) != null) {
                    stringBuilder.append("\n" + message);
                }

                Log.i("Message Receive:", stringBuilder.toString());
                return stringBuilder.toString().trim();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }

}

