package org.eliconcepts.geekking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class tryactivity extends AppCompatActivity
{
    private Button b_login, b_signup;
    private EditText editname, editpass;
    private String GetEditText;
    private String GetEditPass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {   super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryactivity);
        b_login = (Button)findViewById(R.id.imglogin);
        b_signup = (Button)findViewById(R.id.imgsignup);
        editname = (EditText)findViewById(R.id.editname);
        editpass = (EditText)findViewById(R.id.editpass);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditText = editname.getText().toString();
                GetEditPass = editpass.getText().toString();
                if(TextUtils.isEmpty(GetEditText) || TextUtils.isEmpty(GetEditPass) )
                {
                    Toast.makeText(tryactivity.this, "Please enter username or password",
                            Toast.LENGTH_LONG).show();
                }
                else {

                    //send data to server to check if data is correct.
                    if(true)//change to parameter
                    {
                    Intent intent = new Intent(tryactivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    }
                }
            }
        });
        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditText = editname.getText().toString();
                GetEditPass = editpass.getText().toString();
                if (!TextUtils.isEmpty(GetEditText) || !TextUtils.isEmpty(GetEditPass)) {
                    Toast.makeText(tryactivity.this, "You shouldn't enter anything yet",
                            Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(tryactivity.this, signup.class);
                startActivity(intent);

            }
        });
    }
}




