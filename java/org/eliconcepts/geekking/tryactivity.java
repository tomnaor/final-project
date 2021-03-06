package org.eliconcepts.geekking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class tryactivity extends AppCompatActivity {
    private Button b_login, b_signup;
    private EditText editname, editpass;
    private String GetEditText;
    private String GetEditPass;
    private MyTaskParams.Client my_client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryactivity);
        b_login = (Button) findViewById(R.id.imglogin);
        b_signup = (Button) findViewById(R.id.imgsignup);
        editname = (EditText) findViewById(R.id.editname);
        editpass = (EditText) findViewById(R.id.editpass);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditText = editname.getText().toString();
                GetEditPass = editpass.getText().toString();
                if (TextUtils.isEmpty(GetEditText) || TextUtils.isEmpty(GetEditPass)) {
                    Toast.makeText(tryactivity.this, "Please enter username or password",
                            Toast.LENGTH_LONG).show();
                } else {
                    String msg;
                    my_client = new MyTaskParams.Client(tryactivity.this);
                    msg = my_client.login(GetEditText, GetEditPass);
                    MyTaskParams params = new MyTaskParams(msg, null, GetEditText);
                    my_client.execute(params);
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



