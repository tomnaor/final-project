package org.eliconcepts.geekking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class tryactivity extends AppCompatActivity
{
    private Button b_login, b_signup;
    private EditText editname, editpass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryactivity);

        b_login = (Button)findViewById(R.id.imglogin);
        b_signup = (Button)findViewById(R.id.imgsignup);
        editname = (EditText)findViewById(R.id.editname);
        editpass = (EditText)findViewById(R.id.editpass);
                getWindow().setBackgroundDrawableResource(R.drawable.background);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
}
