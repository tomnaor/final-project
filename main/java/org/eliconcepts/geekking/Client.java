package org.eliconcepts.geekking;

import android.content.Context;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


public class Client extends AsyncTask<String,Void,Void>
{
    Socket s;
    PrintWriter pw;
    String message;
    Context c;
    Handler h = new Handler();

    Client(Context c)
    {
        this.c=c;

    }

    public String signup(String username, String password, String Email) {
        String msg;
        msg = "SignUp: {'username': " + username + "'password': " + password + "'email': " + Email + "}";
        return msg;
    }

    public String login(String username, String password) {
        String msg;
        msg = "Login: {\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        return msg;
    }


    @Override
    protected Void doInBackground(String... params) {
        try
        {
            message = params[0];

            s = new Socket("82.81.26.248", 1234);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();
            h.post(new Runnable() {

                @Override
                public void run() {

                }
            });
            pw.close();
            String data;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = bufferedReader.readLine()) != null)
            {
                sb.append(str + "\n");
            }

            // close the reader, and return the results as a String
            bufferedReader.close();
            message = sb.toString();

        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}



