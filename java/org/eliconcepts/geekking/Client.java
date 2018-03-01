package org.eliconcepts.geekking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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



public class Client extends AsyncTask<String,Void,String> {
    Socket s;
    PrintWriter pw;
    String message = "";
    Context c;

    public Client(Context c){
        this.c = c;
    }

    public String signup(String username, String password, String Email) {
        String msg;
        msg = "SignUp: {\"username\": \"" + username + "\", \"password\": \"" +
                password + "\", \"email\": \"" + Email +" \"}";
        return msg;
    }

    public String login(String username, String password) {
        String msg;
        msg = "Login: {\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        return msg;
    }

    @Override
    protected String doInBackground(String...params) {
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
            if (res.equals("LoginTrue")) {
                Intent intent = new Intent(c, WelcomeActivity.class);
                c.startActivity(intent);}
            if (res.equals("LoginFalse")){
                return "FalseLogin";
            }
            if (res.equals("SignUpTrue")){
                Intent intent = new Intent(c, WelcomeActivity.class);
                c.startActivity(intent);
            }
            if (res.equals("SignUpFalse")){
                return "FalseSignUp";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @Override
    protected void onPostExecute(String data) {
        super.onPostExecute(data);

        if (data.equals("FalseLogin")){
            Toast.makeText(c, "Wrong password or Username",
                    Toast.LENGTH_LONG).show();
        }
        if (data.equals("FalseSignUp")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

            // set title
            alertDialogBuilder.setTitle("This username is already exist");
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Dismiss",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }

    }
}
