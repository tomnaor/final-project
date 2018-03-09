package org.eliconcepts.geekking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MyTaskParams {

    String msg = "";
    byte[] d;

    MyTaskParams(String msg, byte[] d){
        this.msg = msg;
        this.d = d;
    }

    static class Client extends AsyncTask<MyTaskParams, Void, String> {
        Socket s;
        PrintWriter pw;
        String message = "";
        byte[] bytes;

        private WeakReference<Context> activityReference;

        // only retain a weak reference to the activity
        Client(Context c) {
            activityReference = new WeakReference<>(c);
        }


        String signup(String username, String password, String Email) {
            String msg;
            msg = "SignUp: {\"username\": \"" + username + "\", \"password\": \"" +
                    password + "\", \"email\": \"" + Email + " \"}";
            return msg;
        }

        String login(String username, String password) {
            String msg;
            msg = "Login: {\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
            return msg;
        }

        String Image(byte[] imgBytes) throws UnsupportedEncodingException {
            String msg;
            //for(byte b : imgBytes){
            //}
            String str = new String(imgBytes);
            msg = "Image_face: {\"data\": \"" + str + "\"}";
            return msg;
        }

        @Override
        protected String doInBackground(MyTaskParams... params) {
            try {
                message = params[0].msg;
                bytes = params[0].d;


                if (message == null) {
                    s = new Socket("192.168.43.3", 1234);
                    OutputStream out = s.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(out);
                    dos.writeUTF("Image_face: " + bytes.length);
                    dos.write(bytes);
                }
                else {
                    //s = new Socket("82.81.26.248", 1234);
                    s = new Socket("192.168.43.3", 1234);
                    pw = new PrintWriter(s.getOutputStream());
                    pw.write(message);
                    pw.flush();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                StringBuilder sb = new StringBuilder();
                sb.append(bufferedReader.readLine());
                String res = sb.toString();

                Context c = activityReference.get();

                if (res.equals("LoginTrue")) {
                    Intent intent = new Intent(c, WelcomeActivity.class);
                    c.startActivity(intent);
                }
                if (res.equals("LoginFalse")) {
                    return "FalseLogin";
                }
                if (res.equals("SignUpTrue")) {
                    Intent intent = new Intent(c, WelcomeActivity.class);
                    c.startActivity(intent);
                }
                if (res.equals("SignUpFalse")) {
                    return "FalseSignUp";
                }
                if (res.contains("Emotion")) {
                    return res;
                }
                if (res.equals("ImageFalse")){
                    return "ImageFalse";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            Context c = activityReference.get();

            if (data.equals("FalseLogin")) {
                Toast.makeText(c, "Wrong password or Username",
                        Toast.LENGTH_LONG).show();
            }
            if (data.equals("FalseSignUp")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

                // set title
                alertDialogBuilder.setTitle("This username is already exist");
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
            if (data.equals("ImageFalse")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                alertDialogBuilder.setTitle("We had some trouble to find face in this pic");
                alertDialogBuilder
                        .setMessage("Please try again")
                        .setCancelable(false)
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            if (data.contains("Emotion")){
                Pattern p = Pattern.compile("Emotion: (.*)");
                Matcher m = p.matcher(data);
                String emo ="";
                if(m.find()){
                    MatchResult mr = m.toMatchResult();
                    emo = mr.group(1);
                }
                if (emo.equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                    alertDialogBuilder.setTitle("We had some trouble to find face in this pic");
                    alertDialogBuilder
                            .setMessage("Please try again")
                            .setCancelable(false)
                            .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                    alertDialogBuilder.setTitle("Your Emotion is");
                    alertDialogBuilder
                            .setMessage(emo)
                            .setCancelable(false)
                            .setPositiveButton("Face it", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("Try Again",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }


        }
    }
}
