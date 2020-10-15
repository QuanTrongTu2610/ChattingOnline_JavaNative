package com.example.chattingonlineapplication.SocketMutipleThread;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient extends AsyncTask<String, Void , String> {

    private Socket socket;

    public TCPClient (Socket socket) {
        this.socket = socket;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            printWriter.println(strings[0]);
            printWriter.flush();
            outputStream.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strings[0];
    }
}
