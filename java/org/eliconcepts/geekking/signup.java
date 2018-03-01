package org.eliconcepts.geekking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class signup extends AppCompatActivity {
    private Button b_signup;
    private EditText editname, editmail, editpass, repass;
    private String GetEditText;
    private String GetEditPass;
    private String GetEditMail;
    private String GetEditConf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        b_signup = (Button) findViewById(R.id.imgsignup);
        editname = (EditText) findViewById(R.id.editname);
        editpass = (EditText) findViewById(R.id.editpass);
        editmail = (EditText) findViewById(R.id.mail);
        repass = (EditText) findViewById(R.id.Confpass);

        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetEditText = editname.getText().toString();
                GetEditPass = editpass.getText().toString();
                GetEditMail = editmail.getText().toString();
                GetEditConf = repass.getText().toString();

                if (TextUtils.isEmpty(GetEditText) || TextUtils.isEmpty(GetEditPass)
                        || TextUtils.isEmpty(GetEditMail) || TextUtils.isEmpty(GetEditConf)) {
                    Toast.makeText(signup.this, "Please enter all the fields",
                            Toast.LENGTH_LONG).show();
                } else {

                    if (GetEditConf.equals(GetEditPass)){
                        String msg;
                        Client my_client = new Client(signup.this);
                        msg = my_client.signup(GetEditText, GetEditPass, GetEditMail);
                        my_client.execute(msg);
                    }
                    else {
                        Toast.makeText(signup.this, "Please correct your password",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
