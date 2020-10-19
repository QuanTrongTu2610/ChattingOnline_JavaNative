package com.example.chattingonlineapplication.SocketMutipleThread;

import com.example.chattingonlineapplication.Plugins.Interface.IUpDateChatViewRecycler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientRequestHandler extends Thread {

    private Socket socket;
    private IUpDateChatViewRecycler iUpDateChatViewRecycler;

    public ClientRequestHandler(Socket socket, IUpDateChatViewRecycler i) {
        this.socket = socket;
        this.iUpDateChatViewRecycler = i;
    }

    @Override
    public void run() {
        String text;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String message;
            while ((message = br.readLine()) != null) {
                stringBuilder.append("\n" + message);
            }
            text = stringBuilder.toString().trim();
            //Callback event
            if (iUpDateChatViewRecycler != null) {
                iUpDateChatViewRecycler.updateItem(text);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

