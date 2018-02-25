package org.eliconcepts.geekking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Client extends AsyncTask<String,Void,Void> {
    Socket s;
    PrintWriter pw;
    String message = "";
    Context c;

    public Client(Context c){
        this.c = c;
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

    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Toast.makeText(c, "Wrong password or Username",
                    Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected Void doInBackground(String...params) {
        try {
            message = params[0];

            s = new Socket("82.81.26.248", 1234);
            pw = new PrintWriter(s.getOutputStream());
            pw.write(message);
            pw.flush();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append(bufferedReader.readLine());

            String res = sb.toString();
            if (res.equals("True")) {
                Intent intent = new Intent(c, WelcomeActivity.class);
                c.startActivity(intent);}
            else{
                mHandler.sendEmptyMessage(0);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



}
