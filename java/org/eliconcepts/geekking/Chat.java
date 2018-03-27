package org.eliconcepts.geekking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Chat extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        //get the data from intent extra - data about the other user
        String data;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                data = null;
            } else {
                data = extras.getString("USER_MATCH");
            }
        } else {
            data = (String) savedInstanceState.getSerializable("USER_MATCH");
        }
        Toast.makeText(Chat.this, data,
                Toast.LENGTH_LONG).show();
    }
}
