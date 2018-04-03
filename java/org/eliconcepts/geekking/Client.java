package org.eliconcepts.geekking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
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
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static android.content.Context.MODE_PRIVATE;
import static org.eliconcepts.geekking.WelcomeActivity.loading;

class MyTaskParams {

    String msg = "";
    byte[] d;
    String username = "";

    MyTaskParams(String msg, byte[] d, String username){
        this.msg = msg;
        this.d = d;
        this.username = username;
    }

    static class Client extends AsyncTask<MyTaskParams, Void, String> {
        Socket s;
        PrintWriter pw;
        String message = "";
        byte[] bytes;
        String username = "";

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

        String Go(String username, String my_emo) {
            String msg;
            msg = "GoChat: {\"" + username + "\": [\"" + my_emo + "\", \"" + my_emo + "\"]}";
            return msg;
        }

        String Chat(String sender, String receiver, String message) {
            //the final result: Chat: {"username_of_sender": ["username_of_receiver", "message"]}
            String msg;
            msg = "Chat: {\"" + sender + "\": [\"" + receiver + "\", \"" + message + "\"]}";
            return msg;
        }


        @Override
        protected String doInBackground(MyTaskParams... params) {
            try {
                message = params[0].msg;
                bytes = params[0].d;
                username = params[0].username;

                if (message == null) {
                    s = new Socket("10.0.0.2", 1234);
                    //s = new Socket("82.81.26.248", 1234);
                    OutputStream out = s.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(out);
                    dos.writeUTF("Image_face: " + bytes.length);
                    dos.write(bytes);
                }
                else {
                    s = new Socket("10.0.0.2", 1234);
                    //s = new Socket("82.81.26.248", 1234);
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
                    return "TrueLogin: " + username;
                }
                if (res.equals("SignUpTrue")) {
                    return "TrueSignUp: " + username;
                }
                else{
                    return res;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            final Context c = activityReference.get();

            if (data.contains("TrueLogin")){
                Pattern p = Pattern.compile("TrueLogin: (.*)");
                Matcher m = p.matcher(data);
                String username ="";
                if(m.find()){
                    MatchResult mr = m.toMatchResult();
                    username = mr.group(1);
                }

                Intent intent = new Intent(c, WelcomeActivity.class);
                c.startActivity(intent);
                //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
                SharedPreferences sp = c.getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("Unm","true: " + username);
                Ed.putString("Psw","true");
                Ed.apply();
            }

            if (data.equals("LoginFalse")) {
                Toast.makeText(c, "Wrong password or Username",
                        Toast.LENGTH_LONG).show();
            }

            if (data.contains("TrueSignUp")){
                Pattern p = Pattern.compile("TrueSingUp: (.*)");
                Matcher m = p.matcher(data);
                String username ="";
                if(m.find()){
                    MatchResult mr = m.toMatchResult();
                    username = mr.group(1);
                }

                Intent intent = new Intent(c, WelcomeActivity.class);
                c.startActivity(intent);
                SharedPreferences sp = c.getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("Unm","true: " + username);
                Ed.putString("Psw","true");
                Ed.apply();
            }

            if (data.equals("SignUpFalse")) {
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
                loading.setVisibility(View.GONE);
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
                loading.setVisibility(View.GONE);
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
                    final String finalEmo = emo;
                    alertDialogBuilder
                            .setMessage(emo)
                            .setCancelable(false)
                            .setPositiveButton("Face it", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(c, MainActivity.class);
                                    intent.putExtra("MY_EMO", finalEmo);
                                    c.startActivity(intent);
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

            if (data.equals("ThreadFalse")){
                //chatLoad.setVisibility(View.GONE);
                //loading.setVisibility(View.GONE);;
                //lookFor.setText("");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
                //LayoutInflater factory = LayoutInflater.from(c);
                //final View view = factory.inflate(R.layout.qmark, null);
                //alertDialogBuilder.setView(view);
                alertDialogBuilder.setTitle("Problem occurred");
                alertDialogBuilder
                        .setMessage("We sorry, there is no good match for you")
                        .setCancelable(false)
                        .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(c, WelcomeActivity.class);
                                c.startActivity(intent);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            if (data.contains("Thread") && !data.equals("ThreadFalse")){
                Intent intent = new Intent(c, Chat.class);
                intent.putExtra("USER_MATCH", data);
                c.startActivity(intent);
            }

            //Chat: {"username_of_sender": "message"}
            if (data.contains("Chat")){
                Pattern p = Pattern.compile("Chat: (.*), (.*)");
                Matcher m = p.matcher(data);
                String username ="";
                String msg = "";
                if(m.find()){
                    MatchResult mr = m.toMatchResult();
                    username = mr.group(1);
                    msg = mr.group(2);
                }
                String extra = "username: " + username + ", msg: " + msg;
                Intent intent = new Intent(c, Chat.class);
                intent.putExtra("NEW_MESSAGE", extra);
                c.startActivity(intent);
            }
        }
    }
}
